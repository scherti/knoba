package com.scheran.knoba.repository;

import com.scheran.knoba.domain.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Topic entity.
 */
public interface TopicRepository extends MongoRepository<Topic,String> {

}
