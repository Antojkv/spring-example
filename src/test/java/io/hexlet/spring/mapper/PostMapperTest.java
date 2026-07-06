package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.UserRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest  // ← Важно: используем Spring контекст
class PostMapperTest {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Faker faker;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = new User();
        testUser.setEmail(faker.internet().emailAddress());
        testUser.setFirstName(faker.name().firstName());
        testUser.setLastName(faker.name().lastName());
        testUser = userRepository.save(testUser);
    }

    @Test
    void testToDTO_shouldConvertPostToPostDTO() {
        // Подготовка
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Title");
        post.setContent("Test content with more than 10 characters");
        post.setPublished(true);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setUser(testUser);

        // Выполнение
        PostDTO dto = postMapper.toDTO(post);

        // Проверка
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
        // Подготовка
        PostCreateDTO dto = new PostCreateDTO();
        dto.setTitle("Test Title");
        dto.setContent("Test content with more than 10 characters");
        dto.setPublished(true);
        dto.setUserId(testUser.getId());

        // Выполнение
        Post post = postMapper.toEntity(dto);

        // Проверка
        assertThat(post).isNotNull();
        assertThat(post.getTitle()).isEqualTo("Test Title");
        assertThat(post.getContent()).isEqualTo("Test content with more than 10 characters");
        assertThat(post.isPublished()).isTrue();
        assertThat(post.getUser()).isNotNull();
        assertThat(post.getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void testUpdateEntity_shouldUpdateExistingPost() {
        // Подготовка
        Post post = new Post();
        post.setTitle("Old Title");
        post.setContent("Old content");
        post.setPublished(false);
        post.setUser(testUser);

        PostUpdateDTO updateDto = new PostUpdateDTO();
        updateDto.setTitle("New Title");
        updateDto.setContent("New content with more than 10 characters");
        updateDto.setPublished(true);

        // Выполнение
        postMapper.updateEntity(updateDto, post);

        // Проверка
        assertThat(post.getTitle()).isEqualTo("New Title");
        assertThat(post.getContent()).isEqualTo("New content with more than 10 characters");
        assertThat(post.isPublished()).isTrue();
        assertThat(post.getUser()).isEqualTo(testUser);  // пользователь не изменился
    }

    @Test
    void testUpdateEntity_shouldHandleNullValues() {
        // Подготовка
        Post post = new Post();
        post.setTitle("Old Title");
        post.setContent("Old content with more than 10 characters");
        post.setPublished(false);
        post.setUser(testUser);

        PostUpdateDTO updateDto = new PostUpdateDTO();
        updateDto.setTitle("New Title");
        // content и published не установлены

        // Выполнение
        postMapper.updateEntity(updateDto, post);

        // Проверка - обновляется только title
        assertThat(post.getTitle()).isEqualTo("New Title");
        assertThat(post.getContent()).isEqualTo("Old content with more than 10 characters");
        assertThat(post.isPublished()).isFalse();
        assertThat(post.getUser()).isEqualTo(testUser);
    }
}
