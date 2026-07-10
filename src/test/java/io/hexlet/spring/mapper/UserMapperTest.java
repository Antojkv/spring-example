package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.UserCreateDTO;
import io.hexlet.spring.dto.UserDTO;
import io.hexlet.spring.dto.UserUpdateDTO;
import io.hexlet.spring.model.User;
import io.hexlet.spring.util.TestUtils;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import io.hexlet.spring.config.TestConfig;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Faker faker;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testToDTO_shouldConvertUserToUserDTO() {
        User user = TestUtils.createTestUser(passwordEncoder);
        user.setId(1L);
        user.setBirthday(LocalDate.of(1990, 1, 1));

        UserDTO dto = userMapper.toDTO(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getEmail()).isEqualTo(user.getEmail());
        assertThat(dto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(user.getLastName());
        assertThat(dto.getBirthday()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    void testToEntity_shouldConvertUserCreateDTOToUser() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail(faker.internet().emailAddress());
        dto.setFirstName(faker.name().firstName());
        dto.setLastName(faker.name().lastName());
        dto.setPasswordDigest("password123");

        User user = userMapper.toEntity(dto);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
        assertThat(user.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(dto.getLastName());
        assertThat(user.getPasswordDigest()).isEqualTo("password123");
    }

    @Test
    void testUpdateEntity_shouldUpdateExistingUser() {
        User user = TestUtils.createTestUser(passwordEncoder);

        UserUpdateDTO updateDto = new UserUpdateDTO();
        updateDto.setEmail("new@example.com");
        updateDto.setFirstName("New");
        updateDto.setLastName("Name");

        userMapper.updateEntity(updateDto, user);

        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getFirstName()).isEqualTo("New");
        assertThat(user.getLastName()).isEqualTo("Name");
        assertThat(user.getPasswordDigest()).isNotNull();
    }

    @Test
    void testUpdateEntity_shouldHandleNullValues() {
        User user = TestUtils.createTestUser(passwordEncoder);

        UserUpdateDTO updateDto = new UserUpdateDTO();
        updateDto.setEmail("new@example.com");

        userMapper.updateEntity(updateDto, user);

        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getFirstName()).isNotEqualTo("New");
        assertThat(user.getLastName()).isNotEqualTo("Name");
        assertThat(user.getPasswordDigest()).isNotNull();
    }
}
