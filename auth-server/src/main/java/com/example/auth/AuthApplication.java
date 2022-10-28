package com.example.auth;


import com.example.auth.config.security.client.ClientDetailRepository;
import com.example.auth.domain.authentication.OauthClientDetailEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;

@EnableCaching
@SpringCloudApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan(value = "com.example.auth")
public class AuthApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthApplication.class, args);
  }

  @Bean
  public CommandLineRunner init(ClientDetailRepository oauthClientDetailRepository) {
    return args -> {
      if (!oauthClientDetailRepository.existsByClientId("hoang")) {
        OauthClientDetailEntity clientDetailEntity = new OauthClientDetailEntity();
        clientDetailEntity.setClientId("hoang");
        clientDetailEntity.setAuthorizedGrantTypes(
            "password,authorization_code,refresh_token,client_credentials");
        clientDetailEntity.setAutoApprove(true);
        clientDetailEntity.setClientSecret("1");
        clientDetailEntity.setResourceIds(
            "hoang,auth-server,order-server,chat-server,catalog-service,shopping-cart-service,file-service,patient-service");
        clientDetailEntity.setScope("read,write,server");
        clientDetailEntity.setWebServerRedirectUri(
            "http://192.168.238.1:8085/chat-server/oauth2/callback/hoang,http://localhost:3000/sso,http://localhost:8085/chat-server/oauth2/callback/hoang");
        oauthClientDetailRepository.save(clientDetailEntity);
      }
    };
  }

}

