package com.ahmeric.store.utils;

import com.ahmeric.store.model.dto.BillDto;
import com.ahmeric.store.model.dto.ProductDto;
import com.ahmeric.store.model.dto.UserDto;
import com.ahmeric.store.model.response.BillResponse;
import com.ahmeric.store.model.response.ProductResponse;
import com.ahmeric.store.model.response.UserResponse;
import java.lang.reflect.Type;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

/**
 * Utility class for object mapping. Uses ModelMapper library to map between DTO and Response
 * classes.
 */
@Component
public class Mapper {

  public static final Type BILL_RESPONSE_LIST_TYPE = new TypeToken<List<BillResponse>>() {
  }.getType();
  public static final Type PRODUCT_RESPONSE_LIST_TYPE = new TypeToken<List<ProductResponse>>() {
  }.getType();
  public static final Type USER_RESPONSE_LIST_TYPE = new TypeToken<List<UserResponse>>() {
  }.getType();
  public static final Type PRODUCT_DTO_LIST_TYPE = new TypeToken<List<ProductDto>>() {
  }.getType();
  public static final Type USER_DTO_LIST_TYPE = new TypeToken<List<UserDto>>() {
  }.getType();
  public static final Type BILL_DTO_LIST_TYPE = new TypeToken<List<BillDto>>() {
  }.getType();
  private final ModelMapper modelMapper;

  public Mapper() {
    this.modelMapper = new ModelMapper();
  }

  /**
   * Maps an object to another object of a specified class.
   *
   * @param source          The object to map from.
   * @param destinationType The class of the object to map to.
   * @return The mapped object.
   */
  public <D> D map(Object source, Class<D> destinationType) {
    return modelMapper.map(source, destinationType);
  }

  /**
   * Maps an object to another object of a specified type.
   *
   * @param source          The object to map from.
   * @param destinationType The type of the object to map to.
   * @return The mapped object.
   */
  public <D> D map(Object source, Type destinationType) {
    return modelMapper.map(source, destinationType);
  }
}
