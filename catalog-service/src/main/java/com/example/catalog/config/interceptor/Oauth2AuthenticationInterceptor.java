package com.example.catalog.config.interceptor;

import static com.google.common.base.Strings.nullToEmpty;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@GrpcGlobalServerInterceptor
@Order(60)
@Slf4j
public class Oauth2AuthenticationInterceptor implements ServerInterceptor {

  private final ResourceServerTokenServices tokenServices;

  @Autowired
  public Oauth2AuthenticationInterceptor(ResourceServerTokenServices tokenServices) {
    this.tokenServices = tokenServices;
  }

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call,
      Metadata headers,
      ServerCallHandler<ReqT, RespT> next) {
    String authHeader = nullToEmpty(
        headers.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)));
    if (!(authHeader.startsWith("Bearer ") || authHeader.startsWith("bearer "))) {
      return next.startCall(call, headers);
    }

    try {
      String token = authHeader.substring(7);

      log.debug("Bearer Token Authorization header found");

      if (authenticationIsRequired()) {
        Authentication authResult = tokenServices.loadAuthentication(token);

        log.debug("Authentication success: {}", authResult);

        SecurityContextHolder.getContext().setAuthentication(authResult);
      }
    } catch (AuthenticationException | OAuth2Exception e) {
      SecurityContextHolder.clearContext();

      log.debug("Authentication request failed: {}", e.getMessage());

      throw Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e)
          .asRuntimeException();
    }

    return next.startCall(call, headers);
  }

  private boolean authenticationIsRequired() {
    Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.isNull(existingAuth) || !existingAuth.isAuthenticated()) {
      return true;
    }
    return existingAuth instanceof AnonymousAuthenticationToken;
  }

}

