package io.hexlet.spring.specification;

import io.hexlet.spring.dto.PostParamsDTO;
import io.hexlet.spring.model.Post;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PostSpecification {

    public Specification<Post> build(PostParamsDTO params) {
        return withTitleCont(params.getTitleCont())
                .and(withContentCont(params.getContentCont()))
                .and(withUserId(params.getUserId()))
                .and(withPublished(params.getPublished()))
                .and(withCreatedAtGt(params.getCreatedAtGt()))
                .and(withCreatedAtLt(params.getCreatedAtLt()));
    }

    // Поиск по вхождению в заголовок (без учета регистра)
    private Specification<Post> withTitleCont(String title) {
        return (root, query, cb) -> {
            if (title == null || title.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    // Поиск по вхождению в содержание (без учета регистра)
    private Specification<Post> withContentCont(String content) {
        return (root, query, cb) -> {
            if (content == null || content.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("content")), "%" + content.toLowerCase() + "%");
        };
    }

    // Фильтр по автору
    private Specification<Post> withUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("user").get("id"), userId);
        };
    }

    // Фильтр по статусу публикации
    private Specification<Post> withPublished(Boolean published) {
        return (root, query, cb) -> {
            if (published == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("published"), published);
        };
    }

    // Фильтр по дате создания (после)
    private Specification<Post> withCreatedAtGt(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            return cb.greaterThan(root.get("createdAt"), date.atStartOfDay());
        };
    }

    // Фильтр по дате создания (до)
    private Specification<Post> withCreatedAtLt(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            return cb.lessThan(root.get("createdAt"), date.atStartOfDay());
        };
    }
}
