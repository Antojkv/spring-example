package io.hexlet.spring.repository;

import io.hexlet.spring.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Page<Post> findByPublishedTrue(Pageable pageable);

    // Метод для поиска с фильтрацией и пагинацией
    Page<Post> findAll(Specification<Post> spec, Pageable pageable);
}
