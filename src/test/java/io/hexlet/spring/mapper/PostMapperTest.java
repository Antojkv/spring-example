package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PostMapperTest {

    private PostMapper postMapper;

    @BeforeEach
    void setUp() {
        postMapper = Mappers.getMapper(PostMapper.class);
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

        // Выполнение
        PostDTO dto = postMapper.toDTO(post);

        // Проверка
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getTitle()).isEqualTo("Test Title");
        assertThat(dto.getContent()).isEqualTo("Test content with more than 10 characters");
        assertThat(dto.isPublished()).isTrue();
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

        // Выполнение
        Post post = postMapper.toEntity(dto);

        // Проверка
        assertThat(post).isNotNull();
        assertThat(post.getTitle()).isEqualTo("Test Title");
        assertThat(post.getContent()).isEqualTo("Test content with more than 10 characters");
        assertThat(post.isPublished()).isTrue();
    }

    @Test
    void testUpdateEntity_shouldUpdateExistingPost() {
        // Подготовка
        Post post = new Post();
        post.setTitle("Old Title");
        post.setContent("Old content");
        post.setPublished(false);

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
    }

    @Test
    void testUpdateEntity_shouldHandleNullValues() {
        // Подготовка
        Post post = new Post();
        post.setTitle("Old Title");
        post.setContent("Old content with more than 10 characters");
        post.setPublished(false);

        PostUpdateDTO updateDto = new PostUpdateDTO();
        updateDto.setTitle("New Title");
        // content и published не установлены

        // Выполнение
        postMapper.updateEntity(updateDto, post);

        // Проверка - обновляется только title
        assertThat(post.getTitle()).isEqualTo("New Title");
        assertThat(post.getContent()).isEqualTo("Old content with more than 10 characters");  // не изменилось
        assertThat(post.isPublished()).isFalse();  // не изменилось
    }
}
