package com.ahmeric.store.repository;

import com.ahmeric.store.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Products. Extends MongoRepository and works with Product objects and
 * their String ids.
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

}

