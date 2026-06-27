package io.hexlet.spring.controller;

import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.PostRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.hexlet.spring.exception.ResourceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    // GET /api/posts - список всех постов
    @GetMapping
    public ResponseEntity<List<Post>> index() {
        return ResponseEntity.ok(postRepository.findAll());
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
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post with id " + id + " not found");
        }
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
