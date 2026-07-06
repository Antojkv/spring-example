package io.hexlet.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.spring.model.Tag;
import io.hexlet.spring.repository.TagRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Faker faker;

    @BeforeEach
    void setUp() {
        tagRepository.deleteAll();
    }

    @Test
    void testGetAllTags_returns200() throws Exception {
        Tag tag = new Tag();
        tag.setName(faker.lorem().word());
        tagRepository.save(tag);

        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTagById_returns200() throws Exception {
        Tag tag = new Tag();
        tag.setName(faker.lorem().word());
        Tag savedTag = tagRepository.save(tag);

        mockMvc.perform(get("/api/tags/{id}", savedTag.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTagById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/tags/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTag_returns201() throws Exception {
        Map<String, String> tagData = new HashMap<>();
        tagData.put("name", faker.lorem().word());

        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagData)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateTag_invalidData_returns422() throws Exception {
        Map<String, String> tagData = new HashMap<>();
        tagData.put("name", "");

        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagData)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testUpdateTag_returns200() throws Exception {
        Tag tag = new Tag();
        tag.setName(faker.lorem().word());
        Tag savedTag = tagRepository.save(tag);

        Map<String, String> updateData = new HashMap<>();
        updateData.put("name", "Updated Tag");

        mockMvc.perform(put("/api/tags/{id}", savedTag.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk());

        Tag updatedTag = tagRepository.findById(savedTag.getId()).orElseThrow();
        assertThat(updatedTag.getName()).isEqualTo("Updated Tag");
    }

    @Test
    void testDeleteTag_returns204() throws Exception {
        Tag tag = new Tag();
        tag.setName(faker.lorem().word());
        Tag savedTag = tagRepository.save(tag);

        mockMvc.perform(delete("/api/tags/{id}", savedTag.getId()))
                .andExpect(status().isNoContent());

        assertThat(tagRepository.findById(savedTag.getId())).isEmpty();
    }

    @Test
    void testDeleteTag_notFound_returns404() throws Exception {
        mockMvc.perform(delete("/api/tags/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
