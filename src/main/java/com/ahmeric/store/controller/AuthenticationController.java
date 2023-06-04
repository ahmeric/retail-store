package com.ahmeric.store.controller;

import com.ahmeric.store.model.dto.UserDto;
import com.ahmeric.store.model.request.AuthenticationRequest;
import com.ahmeric.store.model.request.UserRequest;
import com.ahmeric.store.model.response.AuthenticationResponse;
import com.ahmeric.store.service.AuthenticationService;
import com.ahmeric.store.utils.Mapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class handles user authentication operations. It provides REST endpoints for user
 * registration and authentication.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  /**
   * The AuthenticationService to handle authentication-related operations. The Mapper to convert
   * between various model classes.
   */
  private final AuthenticationService service;
  private final Mapper modelMapper;

  /**
   * Endpoint for user registration.
   *
   * @param request The request containing user details for registration.
   * @return HTTP status indicating the result of the operation.
   */
  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody @Valid UserRequest request) {
    UserDto userDto = modelMapper.map(request, UserDto.class);
    service.register(userDto);
    return ResponseEntity.ok().build();
  }

  /**
   * Endpoint for user authentication.
   *
   * @param request The request containing user credentials for authentication.
   * @return A response entity containing the authentication token.
   */
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody @Valid AuthenticationRequest request) {
    String token = service.authenticate(request.getUserName(), request.getPassword());
    return ResponseEntity.ok(AuthenticationResponse.builder().token(token).build());

  }

}
