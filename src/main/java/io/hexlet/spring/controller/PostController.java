package io.hexlet.spring.controller;

import io.hexlet.spring.model.Post;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private List<Post> posts = new ArrayList<>();

    // GET /posts - список всех постов
    @GetMapping
    public ResponseEntity<List<Post>> index() {
        return ResponseEntity.ok(posts); // 200 OK
    }

    // GET /posts/{title} - получить пост по названию
    @GetMapping("/{title}")
    public ResponseEntity<Post> show(@PathVariable String title) {
        Optional<Post> post = posts.stream()
                .filter(p -> p.getTitle().equals(title))
                .findFirst();
        return ResponseEntity.of(post);
    }

    // POST /posts - создать пост
    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        post.setCreatedAt(LocalDateTime.now());
        posts.add(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post); // 201 Created
    }

    // PUT /posts/{title} - обновить пост
    @PutMapping("/{title}")
    public ResponseEntity<Post> update(@PathVariable String title, @RequestBody Post updatedPost) {
        Optional<Post> post = posts.stream()
                .filter(p -> p.getTitle().equals(title))
                .findFirst();
        if (post.isPresent()) {
            Post existingPost = post.get();
            existingPost.setContent(updatedPost.getContent());
            existingPost.setAuthor(updatedPost.getAuthor());
        }
        return ResponseEntity.of(post);
    }

    // DELETE /posts/{title} - удалить пост
    @DeleteMapping("/{title}")
    public ResponseEntity<Void> destroy(@PathVariable String title) {
        boolean removed = posts.removeIf(post -> post.getTitle().equals(title));
        if (removed) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build();
    }// 404 Not Found
}
