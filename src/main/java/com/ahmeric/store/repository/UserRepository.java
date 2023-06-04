package com.ahmeric.store.repository;

import com.ahmeric.store.entity.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Users. Extends MongoRepository and works with User objects and their
 * String ids. Includes additional method to find User by username.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

  /**
   * Finds a User entity by its username.
   *
   * @param username The username of the user.
   * @return An Optional User which may contain the User entity.
   */
  Optional<User> findByUserName(String username);
}

