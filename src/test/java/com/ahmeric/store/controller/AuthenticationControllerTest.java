package com.ahmeric.store.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahmeric.store.entity.UserType;
import com.ahmeric.store.model.dto.UserDto;
import com.ahmeric.store.model.request.AuthenticationRequest;
import com.ahmeric.store.model.request.UserRequest;
import com.ahmeric.store.model.response.AuthenticationResponse;
import com.ahmeric.store.service.AuthenticationService;
import com.ahmeric.store.utils.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

  @Mock
  private AuthenticationService service;

  @Mock
  private Mapper modelMapper;

  @InjectMocks
  private AuthenticationController controller;

  private UserRequest userRequest;
  private AuthenticationRequest authRequest;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    userRequest = UserRequest.builder()
        .userName("userName")
        .userType(UserType.EMPLOYEE)
        .password("password")
        .build();
    authRequest = AuthenticationRequest.builder()
        .userName("userName")
        .password("password").build();

    userDto = UserDto.builder()
        .id("1")
        .userType(UserType.EMPLOYEE)
        .userName("userName")
        .password("password")
        .build();
  }

  @Test
  void givenUserRequest_whenRegister_thenInvokeServiceAndReturnStatus() {
    when(modelMapper.map(userRequest, UserDto.class)).thenReturn(userDto);

    ResponseEntity<Void> response = controller.register(userRequest);

    verify(modelMapper, times(1)).map(userRequest, UserDto.class);
    verify(service, times(1)).register(userDto);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void givenAuthRequest_whenAuthenticate_thenInvokeServiceAndReturnToken() {
    String token = "token";
    when(service.authenticate(authRequest.getUserName(), authRequest.getPassword())).thenReturn(
        token);

    ResponseEntity<AuthenticationResponse> response = controller.authenticate(authRequest);

    verify(service, times(1)).authenticate(authRequest.getUserName(), authRequest.getPassword());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(token, response.getBody().getToken());
  }

}

