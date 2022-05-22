package com.example.chat.client;


import com.example.chat.dto.UserDTO;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-server")
public interface AuthServiceFeignClient {

  @GetMapping(value = "/auth-server/api/v1/user/me")
  UserDTO getCurrentUser();

  @PostMapping(value = "/auth-server/oauth/token")
  Map<String, Object> login(@RequestParam Map<String, String> parameters);

  @GetMapping(value = "/auth-server/api/v1/user/get-all")
  List<UserDTO> getAll();
}
