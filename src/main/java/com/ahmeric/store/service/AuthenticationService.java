package com.ahmeric.store.service;

import com.ahmeric.store.config.JwtService;
import com.ahmeric.store.entity.User;
import com.ahmeric.store.exception.ErrorRegistry;
import com.ahmeric.store.exception.RetailStoreException;
import com.ahmeric.store.model.dto.UserDto;
import com.ahmeric.store.repository.UserRepository;
import com.ahmeric.store.utils.Mapper;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for Authentication operations. Handles registration and authentication of users.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final Mapper modelMapper;

  /**
   * Registers a new user. Throws an exception if a user with the same username already exists.
   *
   * @param userDto DTO of the user to register.
   */
  public void register(UserDto userDto) {
    userRepository.findByUserName(userDto.getUserName()).ifPresent(user -> {
      throw new RetailStoreException(ErrorRegistry.USER_ALREADY_EXISTS);
    });
    var user = modelMapper.map(userDto, User.class);
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setRegistrationDate(LocalDate.now());
    userRepository.save(user);
  }

  /**
   * Authenticates a user. Throws an exception if the user doesn't exist or if the password is
   * incorrect.
   *
   * @param userName The username of the user.
   * @param password The password of the user.
   * @return A JWT token.
   */
  public String authenticate(String userName, String password) {
    var user = userRepository.findByUserName(userName)
        .orElseThrow(() -> new RetailStoreException(ErrorRegistry.USER_NOT_FOUND));

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            user.getUserName(),
            password
        )
    );

    return jwtService.generateToken(userName);
  }


}
