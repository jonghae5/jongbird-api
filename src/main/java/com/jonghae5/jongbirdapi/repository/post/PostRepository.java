package com.jonghae5.jongbirdapi.repository.post;

import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Long countByUser(User user);

    List<Post> findByUser(User user);
    List<Post> findByPostIdLessThanOrderByCreatedAtDesc(Long id, PageRequest pageRequest);
}
