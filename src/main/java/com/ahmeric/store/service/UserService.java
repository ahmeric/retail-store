package com.ahmeric.store.service;

import com.ahmeric.store.exception.ErrorRegistry;
import com.ahmeric.store.exception.RetailStoreException;
import com.ahmeric.store.model.dto.UserDto;
import com.ahmeric.store.repository.UserRepository;
import com.ahmeric.store.utils.Mapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for User operations. Handles user retrieval.
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final Mapper modelMapper;

  /**
   * Returns a user by its username. Throws an exception if the user with the provided username
   * cannot be found.
   *
   * @param username The username of the user.
   * @return UserDto for the requested user.
   */
  public UserDto findByUserName(String username) {
    var user = userRepository.findByUserName(username).orElseThrow(() -> new RetailStoreException(
        ErrorRegistry.USER_NOT_FOUND));
    return modelMapper.map(user, UserDto.class);
  }

  /**
   * Returns a list of all users.
   *
   * @return List of UserDto.
   */

  public List<UserDto> getAllUsers() {
    return modelMapper.map(userRepository.findAll(), Mapper.USER_DTO_LIST_TYPE);
  }

}
