package io.hexlet.spring.util;

import io.hexlet.spring.model.User;
import net.datafaker.Faker;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestUtils {

    private static final Faker faker = new Faker();

    public static User createTestUser(PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setPasswordDigest(passwordEncoder.encode("password123"));
        return user;
    }

    public static User createTestUserWithPassword(PasswordEncoder passwordEncoder, String password) {
        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setPasswordDigest(passwordEncoder.encode(password));
        return user;
    }
}
