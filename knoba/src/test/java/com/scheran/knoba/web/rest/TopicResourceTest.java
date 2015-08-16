package com.scheran.knoba.web.rest;

import com.scheran.knoba.Application;
import com.scheran.knoba.domain.Topic;
import com.scheran.knoba.repository.TopicRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TopicResource REST controller.
 *
 * @see TopicResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TopicResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private TopicRepository topicRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restTopicMockMvc;

    private Topic topic;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TopicResource topicResource = new TopicResource();
        ReflectionTestUtils.setField(topicResource, "topicRepository", topicRepository);
        this.restTopicMockMvc = MockMvcBuilders.standaloneSetup(topicResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        topicRepository.deleteAll();
        topic = new Topic();
        topic.setName(DEFAULT_NAME);
    }

    @Test
    public void createTopic() throws Exception {
        int databaseSizeBeforeCreate = topicRepository.findAll().size();

        // Create the Topic

        restTopicMockMvc.perform(post("/api/topics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(topic)))
                .andExpect(status().isCreated());

        // Validate the Topic in the database
        List<Topic> topics = topicRepository.findAll();
        assertThat(topics).hasSize(databaseSizeBeforeCreate + 1);
        Topic testTopic = topics.get(topics.size() - 1);
        assertThat(testTopic.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = topicRepository.findAll().size();
        // set the field null
        topic.setName(null);

        // Create the Topic, which fails.

        restTopicMockMvc.perform(post("/api/topics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(topic)))
                .andExpect(status().isBadRequest());

        List<Topic> topics = topicRepository.findAll();
        assertThat(topics).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllTopics() throws Exception {
        // Initialize the database
        topicRepository.save(topic);

        // Get all the topics
        restTopicMockMvc.perform(get("/api/topics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(topic.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    public void getTopic() throws Exception {
        // Initialize the database
        topicRepository.save(topic);

        // Get the topic
        restTopicMockMvc.perform(get("/api/topics/{id}", topic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(topic.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    public void getNonExistingTopic() throws Exception {
        // Get the topic
        restTopicMockMvc.perform(get("/api/topics/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateTopic() throws Exception {
        // Initialize the database
        topicRepository.save(topic);

		int databaseSizeBeforeUpdate = topicRepository.findAll().size();

        // Update the topic
        topic.setName(UPDATED_NAME);
        

        restTopicMockMvc.perform(put("/api/topics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(topic)))
                .andExpect(status().isOk());

        // Validate the Topic in the database
        List<Topic> topics = topicRepository.findAll();
        assertThat(topics).hasSize(databaseSizeBeforeUpdate);
        Topic testTopic = topics.get(topics.size() - 1);
        assertThat(testTopic.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void deleteTopic() throws Exception {
        // Initialize the database
        topicRepository.save(topic);

		int databaseSizeBeforeDelete = topicRepository.findAll().size();

        // Get the topic
        restTopicMockMvc.perform(delete("/api/topics/{id}", topic.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Topic> topics = topicRepository.findAll();
        assertThat(topics).hasSize(databaseSizeBeforeDelete - 1);
    }
}
