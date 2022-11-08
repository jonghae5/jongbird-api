package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.*;
import com.jonghae5.jongbirdapi.file.FileStore;
import com.jonghae5.jongbirdapi.file.ImageFile;
import com.jonghae5.jongbirdapi.repository.HashtagRepository;
import com.jonghae5.jongbirdapi.repository.ImageRepository;
import com.jonghae5.jongbirdapi.repository.PostHashtagRepository;
import com.jonghae5.jongbirdapi.repository.post.PostQueryRepository;
import com.jonghae5.jongbirdapi.repository.post.PostRepository;
import com.jonghae5.jongbirdapi.view.post.AddPostResponse;
import com.jonghae5.jongbirdapi.view.post.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final HashtagRepository hashtagRepository;
    private final FileStore fileStore;


    public List<PostResponse> fetchPostPagesBy(Long lastPostId, User loginUser) {
        List<Post> posts = fetchPages(lastPostId);
        List<PostResponse> result = posts.stream().map(
                x -> new PostResponse(x)
        ).collect(Collectors.toList());

        return result;
    }

    private List<Post> fetchPages(Long lastPostId) {
        int size = 10;
        if (lastPostId>0) {
            return postQueryRepository.findByPostIdLessThanOrderByCreatedAtDesc(lastPostId, size);
        } else {
            return postQueryRepository.findAllOrderByCreatedAtDesc(size);
        }
//        PageRequest pageRequest = PageRequest.of(0, size);
//        List<Post> posts = postRepository.findByPostIdLessThanOrderByCreatedAtDesc(lastPostId, pageRequest);
    }

    public AddPostResponse addPost(List<String> imagePaths, String content, User user) {


        Post post = Post.builder()
                .content(content)
                .user(user)
                .build();

        if (imagePaths != null) {
            for (String imagePath : imagePaths) {
                //영속성 컨텍스트
                Image image = imageRepository.findBySrc(imagePath).orElseThrow(IllegalStateException::new);
                image.addPost(post);
            }
        }

        Pattern pattern = Pattern.compile("#(\\S+)"); // 검색할 문자열 패턴 : #
        Matcher m = pattern.matcher(content); // 문자열 설정

        while (m.find()) {
            Hashtag hashtag = Hashtag.builder()
                    .content(m.group(1).replace("#", "").toLowerCase())
                    .build();

            PostHashtag postHashtag = PostHashtag.builder()
                    .build();
            postHashtag.addPostHashtag(post, hashtag);

            postHashtagRepository.save(postHashtag);
            hashtagRepository.save(hashtag);
        }

        postRepository.save(post);

        //TODO
        //MultipartFile 변환
        //HASHTAG # 제거, 소문자 변경

        AddPostResponse addPostResponse = new AddPostResponse();
        addPostResponse.create(post);
        return addPostResponse;

    }

    public String addImage(MultipartFile image) throws IOException {
        ImageFile imageFile = fileStore.storeFile(image);
        Image storeImage = Image.builder()
                .src(imageFile.getStoreFilePath())
                .build();

        imageRepository.save(storeImage);
        return imageFile.getStoreFilePath();
    }
}
