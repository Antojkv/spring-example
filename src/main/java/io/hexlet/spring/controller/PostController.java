package io.hexlet.spring.controller;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostPatchDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostMapper postMapper;

    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> index() {
        List<Post> posts = postRepository.findAll();
        List<PostDTO> postDTOs = posts.stream()
                .map(postMapper::toDTO)
                .toList();
        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping
    public ResponseEntity<Page<PostDTO>> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> postsPage = postRepository.findByPublishedTrue(pageable);
        Page<PostDTO> postDTOPage = postsPage.map(postMapper::toDTO);

        return ResponseEntity.ok(postDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> show(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
        return ResponseEntity.ok(postMapper.toDTO(post));
    }

    @PostMapping
    public ResponseEntity<PostDTO> create(@Valid @RequestBody PostCreateDTO postCreateDTO) {
        // Находим пользователя по ID
        User user = userRepository.findById(postCreateDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + postCreateDTO.getUserId() + " not found"));

        // Создаем пост из DTO
        Post post = postMapper.toEntity(postCreateDTO);
        post.setUser(user);  // Устанавливаем связь
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.toDTO(savedPost));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateDTO postUpdateDTO) {

        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));

        postMapper.updateEntity(postUpdateDTO, existingPost);
        existingPost.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(existingPost);
        return ResponseEntity.ok(postMapper.toDTO(savedPost));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostDTO> patchPost(
            @PathVariable Long id,
            @RequestBody PostPatchDTO postPatchDTO) {

        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));

        postPatchDTO.getTitle().ifPresent(existingPost::setTitle);
        postPatchDTO.getContent().ifPresent(existingPost::setContent);
        postPatchDTO.getPublished().ifPresent(existingPost::setPublished);
        existingPost.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(existingPost);
        return ResponseEntity.ok(postMapper.toDTO(savedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post with id " + id + " not found");
        }
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
