package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.UserCreateDTO;
import io.hexlet.spring.dto.UserDTO;
import io.hexlet.spring.dto.UserUpdateDTO;
import io.hexlet.spring.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        // Используем Mappers.getMapper() для получения экземпляра маппера
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void testToDTO_shouldConvertUserToUserDTO() {
        // Подготовка
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // Выполнение
        UserDTO dto = userMapper.toDTO(user);

        // Проверка
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getEmail()).isEqualTo("test@example.com");
        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.getLastName()).isEqualTo("Doe");
        assertThat(dto.getBirthday()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    void testToEntity_shouldConvertUserCreateDTOToUser() {
        // Подготовка
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("test@example.com");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        // Выполнение
        User user = userMapper.toEntity(dto);

        // Проверка
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
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
        assertThat(user.getFirstName()).isEqualTo("Old");  // не изменилось
        assertThat(user.getLastName()).isEqualTo("Name");   // не изменилось
    }
}
