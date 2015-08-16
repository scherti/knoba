package com.scheran.knoba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.scheran.knoba.domain.Topic;
import com.scheran.knoba.repository.TopicRepository;
import com.scheran.knoba.web.rest.dto.TopicDTO;
import com.scheran.knoba.web.rest.mapper.TopicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Topic.
 */
@RestController
@RequestMapping("/api")
public class TopicResource {

    private final Logger log = LoggerFactory.getLogger(TopicResource.class);

    @Inject
    private TopicRepository topicRepository;

    @Inject
    private TopicMapper topicMapper;

    /**
     * POST  /topics -> Create a new topic.
     */
    @RequestMapping(value = "/topics",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TopicDTO> create(@Valid @RequestBody TopicDTO topicDTO) throws URISyntaxException {
        log.debug("REST request to save Topic : {}", topicDTO);
        if (topicDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new topic cannot already have an ID").body(null);
        }
        Topic topic = topicMapper.topicDTOToTopic(topicDTO);
        Topic result = topicRepository.save(topic);
        return ResponseEntity.created(new URI("/api/topics/" + topicDTO.getId())).body(topicMapper.topicToTopicDTO(result));
    }

    /**
     * PUT  /topics -> Updates an existing topic.
     */
    @RequestMapping(value = "/topics",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TopicDTO> update(@Valid @RequestBody TopicDTO topicDTO) throws URISyntaxException {
        log.debug("REST request to update Topic : {}", topicDTO);
        if (topicDTO.getId() == null) {
            return create(topicDTO);
        }
        Topic topic = topicMapper.topicDTOToTopic(topicDTO);
        Topic result = topicRepository.save(topic);
        return ResponseEntity.ok().body(topicMapper.topicToTopicDTO(result));
    }

    /**
     * GET  /topics -> get all the topics.
     */
    @RequestMapping(value = "/topics",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<TopicDTO> getAll() {
        log.debug("REST request to get all Topics");
        return topicRepository.findAll().stream()
            .map(topic -> topicMapper.topicToTopicDTO(topic))
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * GET  /topics/:id -> get the "id" topic.
     */
    @RequestMapping(value = "/topics/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TopicDTO> get(@PathVariable String id) {
        log.debug("REST request to get Topic : {}", id);
        return Optional.ofNullable(topicRepository.findOne(id))
            .map(topicMapper::topicToTopicDTO)
            .map(topicDTO -> new ResponseEntity<>(
                topicDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /topics/:id -> delete the "id" topic.
     */
    @RequestMapping(value = "/topics/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete Topic : {}", id);
        topicRepository.delete(id);
    }
}
