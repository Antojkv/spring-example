package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.UserCreateDTO;
import io.hexlet.spring.dto.UserDTO;
import io.hexlet.spring.dto.UserUpdateDTO;
import io.hexlet.spring.model.User;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest  // ← Важно: используем Spring контекст
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Faker faker;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail(faker.internet().emailAddress());
        testUser.setFirstName(faker.name().firstName());
        testUser.setLastName(faker.name().lastName());
    }

    @Test
    void testToDTO_shouldConvertUserToUserDTO() {
        // Подготовка
        testUser.setId(1L);
        testUser.setBirthday(LocalDate.of(1990, 1, 1));

        // Выполнение
        UserDTO dto = userMapper.toDTO(testUser);

        // Проверка
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(dto.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(dto.getBirthday()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    void testToEntity_shouldConvertUserCreateDTOToUser() {
        // Подготовка
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail(testUser.getEmail());
        dto.setFirstName(testUser.getFirstName());
        dto.setLastName(testUser.getLastName());

        // Выполнение
        User user = userMapper.toEntity(dto);

        // Проверка
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(user.getLastName()).isEqualTo(testUser.getLastName());
    }

    @Test
    void testUpdateEntity_shouldUpdateExistingUser() {
        // Подготовка
        User user = new User();
        user.setEmail("old@example.com");
        user.setFirstName("Old");
        user.setLastName("Name");

        UserUpdateDTO updateDto = new UserUpdateDTO();
        updateDto.setEmail("new@example.com");
        updateDto.setFirstName("New");
        updateDto.setLastName("Name");

        // Выполнение
        userMapper.updateEntity(updateDto, user);

        // Проверка
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getFirstName()).isEqualTo("New");
        assertThat(user.getLastName()).isEqualTo("Name");
    }

    @Test
    void testUpdateEntity_shouldHandleNullValues() {
        // Подготовка
        User user = new User();
        user.setEmail("old@example.com");
        user.setFirstName("Old");
        user.setLastName("Name");

        UserUpdateDTO updateDto = new UserUpdateDTO();
        updateDto.setEmail("new@example.com");
        // firstName и lastName не установлены

        // Выполнение
        userMapper.updateEntity(updateDto, user);

        // Проверка - обновляется только email
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getFirstName()).isEqualTo("Old");
        assertThat(user.getLastName()).isEqualTo("Name");
    }
}
