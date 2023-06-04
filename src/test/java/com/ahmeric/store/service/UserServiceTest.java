package com.ahmeric.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahmeric.store.entity.User;
import com.ahmeric.store.entity.UserType;
import com.ahmeric.store.exception.RetailStoreException;
import com.ahmeric.store.model.dto.UserDto;
import com.ahmeric.store.repository.UserRepository;
import com.ahmeric.store.utils.Mapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;
  @Mock
  private Mapper modelMapper;

  private User user1;
  private User user2;
  private UserDto userDto1;
  private UserDto userDto2;

  @BeforeEach
  void setUp() {
    user1 = User.builder().userName("username1")
        .password("password1")
        .userType(UserType.EMPLOYEE)
        .registrationDate(LocalDate.now()).build();
    user2 = User.builder().userName("username2")
        .password("password2")
        .userType(UserType.EMPLOYEE)
        .registrationDate(LocalDate.now()).build();
    userDto1 = UserDto.builder().userName("username1")
        .password("password1")
        .userType(UserType.EMPLOYEE)
        .registrationDate(LocalDate.now()).build();
    userDto2 = UserDto.builder().userName("username2")
        .password("password2")
        .userType(UserType.EMPLOYEE)
        .registrationDate(LocalDate.now()).build();

  }


  @Test
  void testFindByUserName_GivenValidUsername_ShouldReturnUserDTO() {
    when(userRepository.findByUserName("username1")).thenReturn(Optional.of(user1));
    when(modelMapper.map(user1, UserDto.class)).thenReturn(userDto1);

    UserDto result = userService.findByUserName("username1");

    assertEquals("username1", result.getUserName());
    assertEquals("password1", result.getPassword());
    verify(userRepository, times(1)).findByUserName("username1");
  }

  @Test
  void testFindByUserName_GivenInvalidUsername_ShouldThrowException() {
    when(userRepository.findByUserName("unknown")).thenReturn(Optional.empty());

    assertThrows(RetailStoreException.class, () -> userService.findByUserName("unknown"));
    verify(userRepository, times(1)).findByUserName("unknown");
  }

  @Test
  void testGetAllUsers_WhenUsersExist_ShouldReturnListOfUserDTOs() {
    when(userRepository.findAll()).thenReturn(List.of(user1, user2));
    when(modelMapper.map(List.of(user1, user2), Mapper.USER_DTO_LIST_TYPE))
        .thenReturn(List.of(userDto1, userDto2));

    List<UserDto> result = userService.getAllUsers();

    assertEquals(2, result.size());
    assertEquals("username1", result.get(0).getUserName());
    assertEquals("password1", result.get(0).getPassword());
    assertEquals("username2", result.get(1).getUserName());
    assertEquals("password2", result.get(1).getPassword());
    verify(userRepository, times(1)).findAll();
  }
}

