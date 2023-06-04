package com.ahmeric.store.repository;

import com.ahmeric.store.entity.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Bills. Extends MongoRepository and works with Bill objects and their
 * String ids.
 */
@Repository
public interface BillRepository extends MongoRepository<Bill, String> {

}

