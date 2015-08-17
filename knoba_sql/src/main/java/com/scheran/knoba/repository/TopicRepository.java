package com.scheran.knoba.repository;

import com.scheran.knoba.domain.Topic;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Topic entity.
 */
public interface TopicRepository extends JpaRepository<Topic,Long> {

}
