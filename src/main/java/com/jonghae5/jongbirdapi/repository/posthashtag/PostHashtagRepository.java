package com.jonghae5.jongbirdapi.repository.posthashtag;

import com.jonghae5.jongbirdapi.domain.Hashtag;
import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
    PostHashtag save(PostHashtag postHashtag);

    List<PostHashtag> findTop10ByHashtagOrderByCreatedAtDesc(Hashtag findHashtag);

    List<PostHashtag> findByPost(Post post);
}
