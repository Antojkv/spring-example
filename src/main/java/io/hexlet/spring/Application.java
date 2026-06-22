package io.hexlet.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.hexlet.spring.model.Post;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
@RestController
@RequestMapping("/posts")
public class Application {

    private List<Post> posts = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // GET /posts - список всех постов
    @GetMapping
    public List<Post> index() {
        return posts;
    }

    // GET /posts/{title} - получить пост по названию
    @GetMapping("/{title}")
    public Post show(@PathVariable String title) {
        for (Post post : posts) {
            if (post.getTitle().equals(title)) {
                return post;
            }
        }
        return null;
    }

    // POST /posts - создать пост
    @PostMapping
    public Post create(@RequestBody Post post) {
        post.setCreatedAt(LocalDateTime.now());
        posts.add(post);
        return post;
    }

    // PUT /posts/{title} - обновить пост по названию
    @PutMapping("/{title}")
    public Post update(@PathVariable String title, @RequestBody Post updatedPost) {
        for (Post post : posts) {
            if (post.getTitle().equals(title)) {
                post.setContent(updatedPost.getContent());
                post.setAuthor(updatedPost.getAuthor());
                return post;
            }
        }
        return null;
    }

    // DELETE /posts/{title} - удалить пост по названию
    @DeleteMapping("/{title}")
    public void destroy(@PathVariable String title) {
        posts.removeIf(post -> post.getTitle().equals(title));
    }

    // Дополнительные маршруты
    @GetMapping("/")
    String home() {
        return "Hello World!";
    }

    @GetMapping("/about")
    public String about() {
        return "This is simple Spring blog!\n"
                + "I am learning Spring Boot with Hexlet!";
    }
}
