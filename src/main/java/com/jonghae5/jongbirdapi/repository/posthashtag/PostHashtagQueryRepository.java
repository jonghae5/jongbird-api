package com.jonghae5.jongbirdapi.repository.posthashtag;

import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.PostHashtag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PostHashtagQueryRepository {

    private final EntityManager em;

    public List<PostHashtag> findByHashtagAndPostIdLessThanOrderByCreatedAtDesc(String name, int size, Long lastPostId) {
        return em.createQuery(
                        "select ph from PostHashtag ph " +
                                "join fetch ph.post p " +
                                "join fetch ph.hashtag h " +
                                "where h.name=:name and " +
                                "p.postId <:id order by p.createdAt DESC", PostHashtag.class)
                .setParameter("name", name)
                .setParameter("id", lastPostId)
                .setMaxResults(size)
                .getResultList();

    }

    public List<PostHashtag> findByHashtagOrderByCreatedAtDesc(String name, int size) {
        return em.createQuery(
                        "select ph from PostHashtag ph " +
                                "join fetch ph.post p " +
                                "join fetch ph.hashtag h " +
                                "where h.name=:name " +
                                "order by p.createdAt DESC", PostHashtag.class)
                .setParameter("name", name)
                .setMaxResults(size)
                .getResultList();
    }
}
