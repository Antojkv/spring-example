package io.hexlet.spring.mapper;

import io.hexlet.spring.model.BaseEntity;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ReferenceMapper {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    // Общий метод для любой сущности, реализующей BaseEntity
    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    // Специальный метод для преобразования Long -> User
    public User toUser(Long id) {
        return id != null ? userRepository.findById(id).orElse(null) : null;
    }
}
