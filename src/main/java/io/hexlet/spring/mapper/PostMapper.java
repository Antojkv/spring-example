package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.dto.TagDTO;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        uses = { ReferenceMapper.class },
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PostMapper {

    // Post -> PostDTO
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "tags", ignore = true)
    PostDTO toDTO(Post post);

    // PostCreateDTO -> Post
    @Mapping(target = "user", source = "userId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tags", ignore = true)
    Post toEntity(PostCreateDTO dto);

    // PostUpdateDTO -> существующий Post
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tags", ignore = true)
    void updateEntity(PostUpdateDTO dto, @MappingTarget Post post);

    // Вспомогательный метод для преобразования списка тегов в DTO
    default List<TagDTO> mapTagsToDTO(List<Tag> tags) {
        if (tags == null) {
            return new ArrayList<>();
        }
        // В реальном проекте здесь должен быть вызов TagMapper
        return new ArrayList<>();
    }
}
