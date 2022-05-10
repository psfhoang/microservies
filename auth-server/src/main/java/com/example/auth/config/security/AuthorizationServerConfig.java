package com.example.auth.config.security;

import java.security.KeyPair;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  @Setter(onMethod = @__({@Autowired}))
  @Qualifier("customClientDetailService")
  private ClientDetailsService clientDetailsService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  @Setter(onMethod = @__({@Autowired}))
  @Qualifier("customUserDetailsService")
  private UserDetailsService userDetailsService;

  @Setter(onMethod = @__({@Autowired}))
  private AuthenticationManager authenticationManager;

  @Setter(onMethod = @__({@Autowired}))
  private RedisConnectionFactory redisConnectionFactory;

  @Setter(onMethod = @__({@Autowired}))
  private KeyUtil keyUtil;

//  @Setter(onMethod = @__({@Autowired}))
//  private DataSource dataSource;

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security
        .checkTokenAccess("permitAll()")
        .tokenKeyAccess("permitAll()")
        .allowFormAuthenticationForClients()
        .passwordEncoder(passwordEncoder());
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .withClientDetails(clientDetailsService);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints
        .authenticationManager(authenticationManager)
        .tokenStore(redisTokenStore())
        .tokenEnhancer(tokenEnhancer())
        .userDetailsService(userDetailsService)
    ;
  }

  // Lưu token vào redis
  @Bean
  public TokenStore redisTokenStore() {
    return new RedisTokenStore(redisConnectionFactory);
  }

//  @Bean
//  public TokenStore tokenStore() {
//    return new JwtTokenStore(tokenEnhancer());
//  }

  //Lưu token vào data base để sử dụng api revoke token
//  @Bean
//  public TokenStore tokenStore() {
//    return new JdbcTokenStore(dataSource);
//  }

  @Bean
  public JwtAccessTokenConverter tokenEnhancer() {
    JwtAccessTokenConverter converter = new CustomAccessTokenConverter();
    converter.setKeyPair(keyPair());
    return converter;
  }

  public KeyPair keyPair() {
    return new KeyPair(keyUtil.getPublicKey(), keyUtil.getPrivateKey());
  }
}
