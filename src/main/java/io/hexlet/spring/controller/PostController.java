package io.hexlet.spring.controller;

import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.PostRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    // GET /api/posts - список всех постов (без пагинации)
    @GetMapping("/all")
    public ResponseEntity<List<Post>> index() {
        return ResponseEntity.ok(postRepository.findAll());
    }

    // GET /api/posts - список опубликованных постов с пагинацией и сортировкой
    @GetMapping
    public ResponseEntity<Page<Post>> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        // Определяем направление сортировки
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        // Создаем объект сортировки
        Sort sort = Sort.by(sortDirection, sortBy);

        // Создаем объект пагинации с сортировкой
        Pageable pageable = PageRequest.of(page, size, sort);

        // Получаем только опубликованные посты
        Page<Post> postsPage = postRepository.findByPublishedTrue(pageable);

        return ResponseEntity.ok(postsPage);
    }

    // GET /api/posts/{id} - получить пост по ID
    @GetMapping("/{id}")
    public ResponseEntity<Post> show(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
        return ResponseEntity.ok(post);
    }

    // POST /api/posts - создать пост
    @PostMapping
    public ResponseEntity<Post> create(@Valid @RequestBody Post post) {
        Post savedPost = postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    // PUT /api/posts/{id} - обновить пост
    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @Valid @RequestBody Post updatedPost) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setPublished(updatedPost.isPublished());

        Post savedPost = postRepository.save(existingPost);
        return ResponseEntity.ok(savedPost);
    }

    // DELETE /api/posts/{id} - удалить пост
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post with id " + id + " not found");
        }
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
