package com.ahmeric.store.controller;

import com.ahmeric.store.model.dto.UserDto;
import com.ahmeric.store.model.response.UserListResponse;
import com.ahmeric.store.model.response.UserResponse;
import com.ahmeric.store.service.UserService;
import com.ahmeric.store.utils.Mapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class manages user-related operations. It provides a REST endpoint for retrieving all
 * users.
 */
@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class UserController {

  /**
   * The UserService to handle user-related operations. The Mapper to convert between various model
   * classes.
   */
  private final UserService userService;
  private final Mapper modelMapper;

  /**
   * Endpoint for retrieving all users.
   *
   * @return A response entity containing a list of all users.
   */
  @GetMapping
  public ResponseEntity<UserListResponse> getAllUsers() {
    List<UserDto> users = userService.getAllUsers();
    List<UserResponse> userList = modelMapper.map(users, Mapper.USER_RESPONSE_LIST_TYPE);
    return ResponseEntity.ok(UserListResponse.builder().users(userList).build());
  }

}
