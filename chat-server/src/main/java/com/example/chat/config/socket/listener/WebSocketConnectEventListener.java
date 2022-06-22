package com.example.chat.config.socket.listener;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class WebSocketConnectEventListener implements ApplicationListener<SessionConnectedEvent> {

  private final Logger logger = LoggerFactory.getLogger(WebSocketConnectEventListener.class);

  @Override
  public void onApplicationEvent(@NonNull final SessionConnectedEvent event) {
    logger.info("Socket connect");
  }
}