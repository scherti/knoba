package com.scheran.knoba.repository;

import com.scheran.knoba.domain.Authority;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Authority entity.
 */
public interface AuthorityRepository extends MongoRepository<Authority, String> {
}
