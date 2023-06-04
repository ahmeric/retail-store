package com.ahmeric.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahmeric.store.config.JwtService;
import com.ahmeric.store.entity.User;
import com.ahmeric.store.entity.UserType;
import com.ahmeric.store.exception.RetailStoreException;
import com.ahmeric.store.model.dto.UserDto;
import com.ahmeric.store.repository.UserRepository;
import com.ahmeric.store.utils.Mapper;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

  @InjectMocks
  private AuthenticationService authenticationService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtService jwtService;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private Mapper modelMapper;

  private User user1;
  private UserDto userDto1;

  @BeforeEach
  void setUp() {
    user1 = User.builder().id("1").userName("username1").password("password1")
        .userType(UserType.EMPLOYEE).registrationDate(LocalDate.now()).build();
    userDto1 = UserDto.builder().id("1").userName("username1").password("password1")
        .userType(UserType.EMPLOYEE).registrationDate(LocalDate.now()).build();

  }

  @Test
  void testRegister_GivenValidUserDTO_ShouldNotThrowException() {
    when(userRepository.findByUserName("username1")).thenReturn(Optional.empty());
    when(modelMapper.map(userDto1, User.class)).thenReturn(user1);
    when(passwordEncoder.encode(userDto1.getPassword())).thenReturn("encodedPassword1");

    authenticationService.register(userDto1);

    verify(userRepository, times(1)).save(user1);
  }

  @Test
  void testRegister_GivenExistingUserDTO_ShouldThrowRetailStoreException() {
    when(userRepository.findByUserName("username1")).thenReturn(Optional.of(user1));

    assertThrows(RetailStoreException.class, () -> authenticationService.register(userDto1));
    verify(userRepository, times(1)).findByUserName("username1");
  }

  @Test
  void testAuthenticate_GivenValidUsernameAndPassword_ShouldReturnJwtToken() {
    when(userRepository.findByUserName("username1")).thenReturn(Optional.of(user1));
    when(jwtService.generateToken("username1")).thenReturn("mockJwtToken");

    String jwtToken = authenticationService.authenticate("username1", "password1");

    assertEquals("mockJwtToken", jwtToken);
    verify(authenticationManager, times(1))
        .authenticate(new UsernamePasswordAuthenticationToken("username1", "password1"));
  }

  @Test
  void testAuthenticate_GivenInvalidUsername_ShouldThrowRetailStoreException() {
    when(userRepository.findByUserName("unknown")).thenReturn(Optional.empty());

    assertThrows(RetailStoreException.class,
        () -> authenticationService.authenticate("unknown", "password1"));
    verify(authenticationManager, never()).authenticate(
        new UsernamePasswordAuthenticationToken("unknown", "password1"));
  }
}

