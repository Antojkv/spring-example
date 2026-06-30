package io.hexlet.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.spring.model.User;
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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Faker faker;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testGetAllUsers_returns200() throws Exception {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        userRepository.save(user);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserById_returns200() throws Exception {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        User savedUser = userRepository.save(user);

        mockMvc.perform(get("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser_returns201() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", faker.internet().emailAddress());
        userData.put("firstName", faker.name().firstName());
        userData.put("lastName", faker.name().lastName());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateUser_invalidData_returns422() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "invalid-email");
        userData.put("firstName", "");
        userData.put("lastName", "");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testUpdateUser_returns200() throws Exception {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        User savedUser = userRepository.save(user);

        Map<String, String> updateData = new HashMap<>();
        updateData.put("firstName", "Updated");
        updateData.put("lastName", "Name");
        updateData.put("email", savedUser.getEmail());

        mockMvc.perform(put("/api/users/{id}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();
        assertThat(updatedUser.getFirstName()).isEqualTo("Updated");
    }

    @Test
    void testDeleteUser_returns204() throws Exception {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        User savedUser = userRepository.save(user);

        mockMvc.perform(delete("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
    }

    @Test
    void testDeleteUser_notFound_returns404() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
