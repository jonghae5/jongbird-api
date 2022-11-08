package com.jonghae5.jongbirdapi.repository;

import com.jonghae5.jongbirdapi.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
