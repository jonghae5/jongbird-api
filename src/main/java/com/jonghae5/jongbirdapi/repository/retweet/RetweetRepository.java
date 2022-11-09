package com.jonghae5.jongbirdapi.repository.retweet;

import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.Retweet;
import com.jonghae5.jongbirdapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    Optional<Retweet> findByUserAndPost(User user, Post post);
}
