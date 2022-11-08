package com.jonghae5.jongbirdapi.repository;

import com.jonghae5.jongbirdapi.domain.Follow;
import com.jonghae5.jongbirdapi.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    long countByFollowing(User following);
    long countByFollower(User follower);

    List<Follow> findByFollower(User user);

    List<Follow> findByFollowing(User user);
}
