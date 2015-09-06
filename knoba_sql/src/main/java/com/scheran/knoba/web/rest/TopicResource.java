package com.scheran.knoba.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.scheran.knoba.domain.Topic;
import com.scheran.knoba.repository.TopicRepository;
import com.scheran.knoba.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Topic.
 */
@RestController
@RequestMapping("/api")
public class TopicResource {

    private final Logger log = LoggerFactory.getLogger(TopicResource.class);

    @Inject
    private TopicRepository topicRepository;

    /**
     * POST  /topics -> Create a new topic.
     */
    @RequestMapping(value = "/topics",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Topic> create(@Valid @RequestBody Topic topic) throws URISyntaxException {
        log.debug("REST request to save Topic : {}", topic);
        if (topic.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new topic cannot already have an ID").body(null);
        }
        Topic result = topicRepository.save(topic);
        return ResponseEntity.created(new URI("/api/topics/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("topic", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /topics -> Updates an existing topic.
     */
    @RequestMapping(value = "/topics",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Topic> update(@Valid @RequestBody Topic topic) throws URISyntaxException {
        log.debug("REST request to update Topic : {}", topic);
        if (topic.getId() == null) {
            return create(topic);
        }
        Topic result = topicRepository.save(topic);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("topic", topic.getId().toString()))
                .body(result);
    }

    /**
     * GET  /topics -> get all the topics.
     */
    @RequestMapping(value = "/topics",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Topic> getAll() {
        log.debug("REST request to get all Topics");
        return topicRepository.findAll();
    }

    /**
     * GET  /topics/:id -> get the "id" topic.
     */
    @RequestMapping(value = "/topics/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Topic> get(@PathVariable Long id) {
        log.debug("REST request to get Topic : {}", id);
        return Optional.ofNullable(topicRepository.findOne(id))
            .map(topic -> new ResponseEntity<>(
                topic,
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Topic : {}", id);
        topicRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("topic", id.toString())).build();
    }
}
