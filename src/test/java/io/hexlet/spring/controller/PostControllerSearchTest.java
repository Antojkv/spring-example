package io.hexlet.spring.controller;

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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerSearchTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Faker faker;

    private User testUser;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setEmail(faker.internet().emailAddress());
        testUser.setFirstName(faker.name().firstName());
        testUser.setLastName(faker.name().lastName());
        testUser = userRepository.save(testUser);

        // Создаем тестовые посты
        for (int i = 0; i < 5; i++) {
            Post post = new Post();
            post.setTitle("Spring Boot " + i);
            post.setContent("This is a post about Spring Boot and Java");
            post.setPublished(i % 2 == 0);
            post.setUser(testUser);
            post.setCreatedAt(LocalDateTime.now().minusDays(i));
            postRepository.save(post);
        }
    }

    @Test
    void testSearchByTitle() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("titleCont", "Spring")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchByContent() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("contentCont", "Java")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testFilterByPublished() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("published", "true")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testFilterByUserId() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("userId", testUser.getId().toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchWithMultipleParams() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("titleCont", "Spring")
                        .param("published", "true")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchWithPagination() throws Exception {
        mockMvc.perform(get("/api/posts")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sortBy", "createdAt")
                        .param("direction", "desc"))
                .andExpect(status().isOk());
    }
}
