package com.jonghae5.jongbirdapi.repository.post;

import com.jonghae5.jongbirdapi.domain.Hashtag;
import com.jonghae5.jongbirdapi.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PostQueryRepository {

    private final EntityManager em;
    public List<Post> findByPostIdLessThanOrderByCreatedAtDesc(Long id, int size) {
        return em.createQuery(
                "select p from Post p " +
                "join fetch p.user u " +
                "where p.postId <:id order by p.createdAt DESC", Post.class)
                .setParameter("id", id)
                .setMaxResults(size)
                .getResultList();

    }

    public List<Post> findAllOrderByCreatedAtDesc(int size) {
        return em.createQuery(
                        "select p from Post p " +
                                "join fetch p.user u " +
                                "order by p.createdAt DESC", Post.class)
                .setMaxResults(size)
                .getResultList();
    }

    public List<Post> findByUserIdAndPostIdLessThanOrderByCreatedAtDesc(Long userId, int size, Long lastPostId) {
        return em.createQuery(
                        "select p from Post p " +
                                "join fetch p.user u " +
                                "where u.userId=:userId " +
                                "and p.postId <:lastPostId order by p.createdAt DESC", Post.class)
                .setParameter("userId", userId)
                .setParameter("lastPostId", lastPostId)
                .setMaxResults(size)
                .getResultList();
    }

    public List<Post> findByUserIdOrderByCreatedAtDesc(Long userId, int size) {
        return em.createQuery(
                        "select p from Post p " +
                                "join fetch p.user u " +
                                "where u.userId=:userId " +
                                "order by p.createdAt DESC", Post.class)
                .setParameter("userId", userId)
                .setMaxResults(size)
                .getResultList();
    }
}
