package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.*;
import com.jonghae5.jongbirdapi.exception.post.InvalidatePostException;
import com.jonghae5.jongbirdapi.web.file.FileStore;
import com.jonghae5.jongbirdapi.web.file.ImageFile;
import com.jonghae5.jongbirdapi.repository.comment.CommentRepository;
import com.jonghae5.jongbirdapi.repository.hashtag.HashtagRepository;
import com.jonghae5.jongbirdapi.repository.image.ImageRepository;
import com.jonghae5.jongbirdapi.repository.like.LikeRepository;
import com.jonghae5.jongbirdapi.repository.posthashtag.PostHashtagRepository;
import com.jonghae5.jongbirdapi.repository.post.PostQueryRepository;
import com.jonghae5.jongbirdapi.repository.post.PostRepository;
import com.jonghae5.jongbirdapi.view.post.DeletePostResponse;
import com.jonghae5.jongbirdapi.view.post.UpdatePostRequest;
import com.jonghae5.jongbirdapi.view.post.UpdatePostResponse;
import com.jonghae5.jongbirdapi.view.post.AddPostRequest;
import com.jonghae5.jongbirdapi.view.post.AddPostResponse;
import com.jonghae5.jongbirdapi.view.post.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

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

    public AddPostResponse addPost(AddPostRequest addPostRequest, User user) {

        List<String> imagePaths = addPostRequest.getImagePaths();
        String content = addPostRequest.getContent();

        Post post = addPostRequest.toEntity(user);

        if (imagePaths != null) {
            for (String imagePath : imagePaths) {
                //영속성 컨텍스트
                //TODO 이미지
                Image image = imageRepository.findBySrc(imagePath).orElseThrow(IllegalArgumentException::new);
                image.addPost(post);
            }
        }
        addHashtag(content, post);

        postRepository.save(post);

        AddPostResponse addPostResponse = new AddPostResponse();
        addPostResponse.create(post);
        return addPostResponse;

    }

    private void addHashtag(String content, Post post) {
        Pattern pattern = Pattern.compile("#(\\S+)"); // 검색할 문자열 패턴 : #
        Matcher m = pattern.matcher(content); // 문자열 설정

        while (m.find()) {
            
            String hashtagName = m.group(1).replace("#", "").toLowerCase();
            Optional<Hashtag> findHashtag = hashtagRepository.findByName(hashtagName);
            PostHashtag postHashtag = PostHashtag.builder()
                    .build();
            
            if (findHashtag.isPresent()) {
                postHashtag.addPostHashtag(post, findHashtag.get());
            } else {
                Hashtag newHashtag = Hashtag.builder()
                        .name(hashtagName)
                        .build();
                postHashtag.addPostHashtag(post, newHashtag);
                hashtagRepository.save(newHashtag);
            }
            postHashtagRepository.save(postHashtag);
        }
    }

    public ImageFile addImage(MultipartFile image) throws IOException {
        ImageFile imageFile = fileStore.storeFile(image);
        Image storeImage = Image.builder()
                .src(imageFile.getStoreFilePath())
                .build();

        imageRepository.save(storeImage);
        return imageFile;
    }

    public UpdatePostResponse updatePost(UpdatePostRequest updatePostRequest, User loginUser) {

        Post post = postRepository.findById(updatePostRequest.getPostId()).orElseThrow(InvalidatePostException::new);
        post.updateContent(updatePostRequest.getContent());


        // postHashtag 기존 데이터 삭제
        List<PostHashtag> postHashtags = postHashtagRepository.findByPost(post);

        for (PostHashtag postHashtag : postHashtags) {
            postHashtag.deletePostAndHashtag();
            postHashtagRepository.delete(postHashtag);
        }

        addHashtag(updatePostRequest.getContent(), post);

        return UpdatePostResponse.builder()
                .postId(post.getPostId())
                .content(post.getContent())
                .build();
    }

    public DeletePostResponse deletePost(User loginUser, Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(InvalidatePostException::new);

        List<Comment> comments = commentRepository.findByPost(post);
        for (Comment comment : comments) {
            comment.deleteUserAndPost();
            commentRepository.delete(comment);
        }
        List<Like> likes = likeRepository.findByPost(post);
        for (Like like : likes) {
            like.deleteUserAndPost();
            likeRepository.delete(like);
        }
        List<Image> images = imageRepository.findByPost(post);
        for (Image image : images) {
            image.deletePost();
            imageRepository.delete(image);
        }
        postRepository.delete(post);

        return new DeletePostResponse(postId);
    }

    public List<PostResponse> fetchPostPagesByUser(Long lastId, User loginUser, Long userId) {
        List<Post> posts = fetchPagesByUser(lastId, userId);

        return posts.stream().map(
                x -> new PostResponse(x)
        ).collect(Collectors.toList());
    }


    private List<Post> fetchPagesByUser(Long lastPostId, Long userId) {
        int size = 10;
        if (lastPostId>0) {
            return postQueryRepository.findByUserIdAndPostIdLessThanOrderByCreatedAtDesc(userId, size,lastPostId);
        } else {
            return postQueryRepository.findByUserIdOrderByCreatedAtDesc( userId, size);
        }

    }


}
