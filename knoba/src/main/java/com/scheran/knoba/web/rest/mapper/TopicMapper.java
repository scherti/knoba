package com.scheran.knoba.web.rest.mapper;

import com.scheran.knoba.domain.*;
import com.scheran.knoba.web.rest.dto.TopicDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Topic and its DTO TopicDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TopicMapper {

    TopicDTO topicToTopicDTO(Topic topic);

    Topic topicDTOToTopic(TopicDTO topicDTO);
}
