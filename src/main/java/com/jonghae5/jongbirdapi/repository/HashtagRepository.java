package com.jonghae5.jongbirdapi.repository;

import com.jonghae5.jongbirdapi.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Hashtag save(Hashtag hashtag);
}
