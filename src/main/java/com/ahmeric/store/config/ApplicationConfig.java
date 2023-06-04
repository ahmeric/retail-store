package com.ahmeric.store.config;

import com.ahmeric.store.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This configuration class manages the creation of several beans for user authentication. It is
 * responsible for the creation of beans like UserDetailsService, AuthenticationProvider,
 * AuthenticationManager and PasswordEncoder. Annotated as a Spring configuration class.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  /**
   * The UserService to interact with user-related operations.
   */
  private final UserService userService;

  /**
   * This method is responsible for creating a UserDetailsService instance.
   *
   * @return a UserDetailsService instance
   */
  @Bean
  public UserDetailsService userDetailsService() {
    return this::loadUserByUsername;
  }

  /**
   * This method loads user details by username.
   *
   * @param username the username of the user
   * @return a UserDetails instance
   */
  public UserDetails loadUserByUsername(String username) {
    var user = userService.findByUserName(username);
    List<GrantedAuthority> authorities = List.of(
        new SimpleGrantedAuthority(user.getUserType().name()));
    return new org.springframework.security.core.userdetails.User(user.getUserName(),
        user.getPassword(), authorities);
  }

  /**
   * This method is responsible for creating an AuthenticationProvider instance.
   *
   * @return an AuthenticationProvider instance
   */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  /**
   * This method is responsible for creating an AuthenticationManager instance.
   *
   * @param config the AuthenticationConfiguration instance
   * @return an AuthenticationManager instance
   * @throws Exception if an error occurs while getting the authentication manager
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  /**
   * This method is responsible for creating a PasswordEncoder instance.
   *
   * @return a PasswordEncoder instance
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
