package com.ahmeric.store.entity;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This class represents a User in the system. It is used to model a User that has an id, username,
 * user type, registration date, and password.
 */
@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

  @Id
  private String id;
  @Indexed(unique = true)
  @NotNull
  private String userName;
  @NotNull
  private UserType userType;
  @NotNull
  private LocalDate registrationDate;
  @NotNull
  private String password;
}
