package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.Hashtag;
import com.jonghae5.jongbirdapi.domain.PostHashtag;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.repository.posthashtag.PostHashtagQueryRepository;
import com.jonghae5.jongbirdapi.repository.hashtag.HashtagRepository;
import com.jonghae5.jongbirdapi.repository.posthashtag.PostHashtagRepository;
import com.jonghae5.jongbirdapi.view.hashtag.GetPostWithHashtagResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final PostHashtagQueryRepository postHashtagQueryRepository;
    private final PostHashtagRepository postHashtagRepository;

    public List<GetPostWithHashtagResponse> fetchPostPagesByHashtag(User loginUser, String hashtag, Long lastId) {
        String replaceHashtagName = hashtag.replace("#", "").toLowerCase();
        Hashtag findHashtag = hashtagRepository.findByName(replaceHashtagName).orElseThrow(IllegalArgumentException::new);

        List<PostHashtag> findPostHashtags = fetchPagesByHashtag(findHashtag, replaceHashtagName, lastId);

        return findPostHashtags.stream()
                .map(x -> new GetPostWithHashtagResponse(x.getPost()))
                .collect(Collectors.toList());

    }


    private List<PostHashtag> fetchPagesByHashtag(Hashtag hashtag, String name, Long lastPostId) {
        int size = 10;
        if (lastPostId>0) {
            return postHashtagQueryRepository.findByHashtagAndPostIdLessThanOrderByCreatedAtDesc(name, size, lastPostId);
        } else {
            return postHashtagQueryRepository.findByHashtagOrderByCreatedAtDesc(name, size);
        }
    }
}
