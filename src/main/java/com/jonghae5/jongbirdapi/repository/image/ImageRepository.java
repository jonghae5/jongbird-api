package com.jonghae5.jongbirdapi.repository.image;

import com.jonghae5.jongbirdapi.domain.Image;
import com.jonghae5.jongbirdapi.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findBySrc(String src);
    Image save(Image image);

    List<Image> findByPost(Post post);
}
