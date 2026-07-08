package io.hexlet.spring.service;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostParamsDTO;
import io.hexlet.spring.dto.PostPatchDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.UserRepository;
import io.hexlet.spring.specification.PostSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostSpecification postSpecification;

    // Получить все посты
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(postMapper::toDTO)
                .toList();
    }

    // Получить посты с фильтрацией и пагинацией
    public Page<PostDTO> getPosts(PostParamsDTO params, int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Sort sort = Sort.by(sortDirection, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        var spec = postSpecification.build(params);
        Page<Post> postsPage = postRepository.findAll(spec, pageRequest);
        return postsPage.map(postMapper::toDTO);
    }

    // Получить опубликованные посты
    public Page<PostDTO> getPublishedPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> postsPage = postRepository.findByPublishedTrue(pageRequest);
        return postsPage.map(postMapper::toDTO);
    }

    // Получить пост по ID
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
        return postMapper.toDTO(post);
    }

    // Создать новый пост
    public PostDTO createPost(PostCreateDTO postCreateDTO) {
        User user = userRepository.findById(postCreateDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + postCreateDTO.getUserId() + " not found"));

        Post post = postMapper.toEntity(postCreateDTO);
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);
        return postMapper.toDTO(savedPost);
    }

    // Полностью обновить пост
    public PostDTO updatePost(Long id, PostUpdateDTO postUpdateDTO) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));

        postMapper.updateEntity(postUpdateDTO, existingPost);
        existingPost.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(existingPost);
        return postMapper.toDTO(savedPost);
    }

    // Частично обновить пост (PATCH)
    public PostDTO patchPost(Long id, PostPatchDTO postPatchDTO) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));

        postPatchDTO.getTitle().ifPresent(existingPost::setTitle);
        postPatchDTO.getContent().ifPresent(existingPost::setContent);
        postPatchDTO.getPublished().ifPresent(existingPost::setPublished);
        existingPost.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(existingPost);
        return postMapper.toDTO(savedPost);
    }

    // Удалить пост
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post with id " + id + " not found");
        }
        postRepository.deleteById(id);
    }
}
