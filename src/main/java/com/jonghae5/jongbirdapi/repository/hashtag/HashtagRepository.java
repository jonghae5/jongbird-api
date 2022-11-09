package com.jonghae5.jongbirdapi.repository.hashtag;

import com.jonghae5.jongbirdapi.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Hashtag save(Hashtag hashtag);


    Optional<Hashtag> findByName(String name);
}
