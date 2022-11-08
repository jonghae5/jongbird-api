package com.jonghae5.jongbirdapi.repository;

import com.jonghae5.jongbirdapi.domain.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
    PostHashtag save(PostHashtag postHashtag);
}
