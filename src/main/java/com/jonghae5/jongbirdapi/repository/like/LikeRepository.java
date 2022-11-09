package com.jonghae5.jongbirdapi.repository.like;

import com.jonghae5.jongbirdapi.domain.Like;
import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like save(Like like);
    Optional<Like> findByUserAndPost(User user, Post post);
    List<Like> findByPost(Post post);
}
