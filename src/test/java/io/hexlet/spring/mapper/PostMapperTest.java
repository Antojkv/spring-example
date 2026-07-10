package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.UserRepository;
import io.hexlet.spring.util.TestUtils;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PostMapperTest {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Faker faker;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        testUser = TestUtils.createTestUser(passwordEncoder);
        testUser = userRepository.save(testUser);
    }

    @Test
    void testToDTO_shouldConvertPostToPostDTO() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Title");
        post.setContent("Test content with more than 10 characters");
        post.setPublished(true);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setUser(testUser);

        PostDTO dto = postMapper.toDTO(post);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getTitle()).isEqualTo("Test Title");
        assertThat(dto.getContent()).isEqualTo("Test content with more than 10 characters");
        assertThat(dto.isPublished()).isTrue();
        assertThat(dto.getUserId()).isEqualTo(testUser.getId());
        assertThat(dto.getCreatedAt()).isNotNull();
        assertThat(dto.getUpdatedAt()).isNotNull();
    }

    @Test
    void testToEntity_shouldConvertPostCreateDTOToPost() {
        PostCreateDTO dto = new PostCreateDTO();
        dto.setTitle("Test Title");
        dto.setContent("Test content with more than 10 characters");
        dto.setPublished(true);
        dto.setUserId(testUser.getId());

        Post post = postMapper.toEntity(dto);

        assertThat(post).isNotNull();
        assertThat(post.getTitle()).isEqualTo("Test Title");
        assertThat(post.getContent()).isEqualTo("Test content with more than 10 characters");
        assertThat(post.isPublished()).isTrue();
        assertThat(post.getUser()).isNotNull();
        assertThat(post.getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void testUpdateEntity_shouldUpdateExistingPost() {
        Post post = new Post();
        post.setTitle("Old Title");
        post.setContent("Old content");
        post.setPublished(false);
        post.setUser(testUser);

        PostUpdateDTO updateDto = new PostUpdateDTO();
        updateDto.setTitle("New Title");
        updateDto.setContent("New content with more than 10 characters");
        updateDto.setPublished(true);

        postMapper.updateEntity(updateDto, post);

        assertThat(post.getTitle()).isEqualTo("New Title");
        assertThat(post.getContent()).isEqualTo("New content with more than 10 characters");
        assertThat(post.isPublished()).isTrue();
        assertThat(post.getUser()).isEqualTo(testUser);
    }

    @Test
    void testUpdateEntity_shouldHandleNullValues() {
        Post post = new Post();
        post.setTitle("Old Title");
        post.setContent("Old content with more than 10 characters");
        post.setPublished(false);
        post.setUser(testUser);

        PostUpdateDTO updateDto = new PostUpdateDTO();
        updateDto.setTitle("New Title");

        postMapper.updateEntity(updateDto, post);

        assertThat(post.getTitle()).isEqualTo("New Title");
        assertThat(post.getContent()).isEqualTo("Old content with more than 10 characters");
        assertThat(post.isPublished()).isFalse();
        assertThat(post.getUser()).isEqualTo(testUser);
    }
}
