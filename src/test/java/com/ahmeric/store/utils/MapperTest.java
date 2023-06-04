package com.ahmeric.store.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ahmeric.store.model.dto.UserDto;
import com.ahmeric.store.model.response.UserResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class MapperTest {

  @InjectMocks
  private Mapper mapper;

  @Mock
  private ModelMapper modelMapper;

  @Test
  void shouldConvertSourceToDestination_whenValidSourceAndDestinationClassProvided() {
    // Given
    UserDto userDto = new UserDto();
    userDto.setId("id");
    userDto.setUserName("TestUser");

    UserResponse userResponseExpected = new UserResponse();
    userResponseExpected.setId("id");
    userResponseExpected.setUserName("TestUser");

    // When
    UserResponse userResponseActual = mapper.map(userDto, UserResponse.class);

    // Then
    assertNotNull(userResponseActual);
    assertEquals(userResponseExpected.getId(), userResponseActual.getId());
    assertEquals(userResponseExpected.getUserName(), userResponseActual.getUserName());
  }

  @Test
  void shouldThrowException_whenNullSourceProvided() {
    // Given
    UserDto userDto = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> mapper.map(userDto, UserResponse.class));
  }

  @Test
  void shouldThrowException_whenNullDestinationClassProvided() {
    // Given
    UserDto userDto = new UserDto();

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> mapper.map(userDto, null));
  }

  @Test
  void shouldConvertSourceToDestination_whenValidSourceAndDestinationTypeProvided() {
    // Given
    UserDto userDto = new UserDto();
    userDto.setId("id");
    userDto.setUserName("TestUser");

    UserResponse userResponseExpected = new UserResponse();
    userResponseExpected.setId("id");
    userResponseExpected.setUserName("TestUser");

    // When
    List<UserResponse> userResponseActual = mapper.map(List.of(userDto),
        Mapper.USER_RESPONSE_LIST_TYPE);

    // Then
    assertNotNull(userResponseActual);
    assertEquals(userResponseExpected.getId(), userResponseActual.get(0).getId());
    assertEquals(userResponseExpected.getUserName(), userResponseActual.get(0).getUserName());
  }

  @Test
  void shouldThrowException_whenNullDestinationTypeProvided() {
    // Given
    UserDto userDto = new UserDto();

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> mapper.map(userDto, null));
  }
}

