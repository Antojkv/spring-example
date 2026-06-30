package io.hexlet.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.UserRepository;
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
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Faker faker;

    private User testUser;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();

        // Создаем тестового пользователя вручную
        testUser = new User();
        testUser.setEmail(faker.internet().emailAddress());
        testUser.setFirstName(faker.name().firstName());
        testUser.setLastName(faker.name().lastName());
        testUser = userRepository.save(testUser);
    }

    @Test
    void testGetPublishedPosts_returns200() throws Exception {
        // Создаем опубликованные посты
        for (int i = 0; i < 3; i++) {
            Post post = new Post();
            post.setTitle(faker.book().title());
            post.setContent(faker.lorem().paragraph(3));
            post.setPublished(true);
            post.setUser(testUser);
            postRepository.save(post);
        }

        mockMvc.perform(get("/api/posts")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllPosts_returns200() throws Exception {
        Post post = new Post();
        post.setTitle(faker.book().title());
        post.setContent(faker.lorem().paragraph(3));
        post.setPublished(true);
        post.setUser(testUser);
        postRepository.save(post);

        mockMvc.perform(get("/api/posts/all"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPostById_returns200() throws Exception {
        Post post = new Post();
        post.setTitle(faker.book().title());
        post.setContent(faker.lorem().paragraph(3));
        post.setPublished(true);
        post.setUser(testUser);
        Post savedPost = postRepository.save(post);

        mockMvc.perform(get("/api/posts/{id}", savedPost.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPostById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/posts/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePost_returns201() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("title", faker.book().title());
        postData.put("content", faker.lorem().paragraph(3));
        postData.put("published", true);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postData)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreatePost_invalidData_returns422() throws Exception {
        Map<String, String> postData = new HashMap<>();
        postData.put("title", "");
        postData.put("content", "short");

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postData)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testUpdatePost_returns200() throws Exception {
        Post post = new Post();
        post.setTitle(faker.book().title());
        post.setContent(faker.lorem().paragraph(3));
        post.setPublished(true);
        post.setUser(testUser);
        Post savedPost = postRepository.save(post);

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("title", "Updated Title");
        updateData.put("content", "Updated content with more than 10 characters");
        updateData.put("published", false);

        mockMvc.perform(put("/api/posts/{id}", savedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk());

        Post updatedPost = postRepository.findById(savedPost.getId()).orElseThrow();
        assertThat(updatedPost.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void testDeletePost_returns204() throws Exception {
        Post post = new Post();
        post.setTitle(faker.book().title());
        post.setContent(faker.lorem().paragraph(3));
        post.setPublished(true);
        post.setUser(testUser);
        Post savedPost = postRepository.save(post);

        mockMvc.perform(delete("/api/posts/{id}", savedPost.getId()))
                .andExpect(status().isNoContent());

        assertThat(postRepository.findById(savedPost.getId())).isEmpty();
    }

    @Test
    void testDeletePost_notFound_returns404() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
