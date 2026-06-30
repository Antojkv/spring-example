package io.hexlet.spring.repository;

import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByPublishedTrue(Pageable pageable);

    List<Post> findByUser(User user);
    List<Post> findByPublishedTrue();
}
