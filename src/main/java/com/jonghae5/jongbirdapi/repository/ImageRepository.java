package com.jonghae5.jongbirdapi.repository;

import com.jonghae5.jongbirdapi.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findBySrc(String src);
    Image save(Image image);
}
