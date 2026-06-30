package io.hexlet.spring.component;

import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModelGenerator {

    @Autowired
    private Faker faker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @PostConstruct
    public void generateData() {
        if (userRepository.count() > 0) {
            System.out.println("Data already exists, skipping generation.");
            return;
        }

        System.out.println("Generating test data...");

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setEmail(faker.internet().emailAddress());
            user.setBirthday(faker.date().birthday().toLocalDateTime().toLocalDate());

            User savedUser = userRepository.save(user);
            users.add(savedUser);
        }

        for (int i = 0; i < 20; i++) {
            Post post = new Post();
            post.setTitle(faker.book().title());
            post.setContent(faker.lorem().paragraph(3));
            post.setPublished(faker.bool().bool());

            User randomUser = users.get(faker.number().numberBetween(0, users.size()));
            post.setUser(randomUser);

            postRepository.save(post);
        }

        System.out.println("Generated " + userRepository.count() + " users and " + postRepository.count() + " posts.");
    }
}
