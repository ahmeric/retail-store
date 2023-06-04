package com.ahmeric.store.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahmeric.store.entity.UserType;
import com.ahmeric.store.model.dto.UserDto;
import com.ahmeric.store.model.response.UserListResponse;
import com.ahmeric.store.model.response.UserResponse;
import com.ahmeric.store.service.UserService;
import com.ahmeric.store.utils.Mapper;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock
  private UserService userService;

  @Mock
  private Mapper modelMapper;

  @InjectMocks
  private UserController controller;

  private UserResponse userResponse;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    userDto = UserDto.builder()
        .id("1")
        .userType(UserType.AFFILIATE)
        .userName("userName")
        .password("password")
        .build();
    userResponse = UserResponse.builder().userName("userName")
        .userType(UserType.AFFILIATE).build();
  }

  @Test
  void whenGetAllUsers_thenInvokeServiceAndReturnUserListResponse() {
    List<UserDto> userDtoList = List.of(userDto);
    when(userService.getAllUsers()).thenReturn(userDtoList);
    when(modelMapper.map(userDtoList, Mapper.USER_RESPONSE_LIST_TYPE)).thenReturn(
        List.of(userResponse));

    ResponseEntity<UserListResponse> response = controller.getAllUsers();

    verify(userService, times(1)).getAllUsers();
    verify(modelMapper, times(1)).map(userDtoList, Mapper.USER_RESPONSE_LIST_TYPE);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(List.of(userResponse), Objects.requireNonNull(response.getBody()).getUsers());
  }

  @Test
  void whenGetAllUsers_thenReturnEmptyList() throws Exception {
    when(userService.getAllUsers()).thenReturn(Collections.emptyList());
    when(modelMapper.map(anyList(), eq(Mapper.USER_RESPONSE_LIST_TYPE))).thenReturn(
        Collections.emptyList());

    ResponseEntity<UserListResponse> response = controller.getAllUsers();

    verify(userService, times(1)).getAllUsers();
    verify(modelMapper, times(1)).map(Collections.emptyList(), Mapper.USER_RESPONSE_LIST_TYPE);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(Collections.emptyList(), Objects.requireNonNull(response.getBody()).getUsers());
  }

}


