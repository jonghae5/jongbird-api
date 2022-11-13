package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.*;
import com.jonghae5.jongbirdapi.exception.post.InvalidatePostException;
import com.jonghae5.jongbirdapi.repository.hashtag.HashtagRepository;
import com.jonghae5.jongbirdapi.repository.image.ImageRepository;
import com.jonghae5.jongbirdapi.repository.post.PostRepository;
import com.jonghae5.jongbirdapi.repository.posthashtag.PostHashtagRepository;
import com.jonghae5.jongbirdapi.repository.user.UserRepository;
import com.jonghae5.jongbirdapi.view.post.AddPostRequest;
import com.jonghae5.jongbirdapi.view.post.AddPostResponse;
import com.jonghae5.jongbirdapi.view.post.UpdatePostRequest;
import com.jonghae5.jongbirdapi.view.post.UpdatePostResponse;
import com.jonghae5.jongbirdapi.web.file.FileStore;
import com.jonghae5.jongbirdapi.web.file.ImageFile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    private static User loginUser;
    private static Image image1;
    private static Image image2;
    private static Post post;
    private String content = "#리액트 #노드 test content";

    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private HashtagRepository hashtagRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private PostHashtagRepository postHashtagRepository;
    @Spy
    private FileStore fileStore = new MockFileStore();
    @InjectMocks
    private PostService postService;

    @BeforeAll
    static void setupBeforeAll() {
        loginUser =
                User.builder()
                        .userId(1L)
                        .nickname("test Nickname")
                        .email("test1@naver.com")
                        .password("testEncodePW1@")
                        .build();

        image1 = Image.builder()
                .imageId(1L)
                .src("/test/image1.jpeg")
                .build();

        image2 = Image.builder()
                .imageId(2L)
                .src("/test/image2.jpeg")
                .build();
    }

    @BeforeEach
    void setupBeforeEach() {
        post = Post.builder()
                .content(content)
                .user(loginUser)
                .images(List.of(image1,image2))
                .postId(10l)
                .build();
    }

    @Test
    @DisplayName("이미지 등록 성공")
    void addImage() throws IOException {
        String filename = "image1";
        String contentType = "png";
        String filePath= "src/test/resources/image1.png";

        MockMultipartFile multipartFile = getMockMultipartFile(filename, contentType, filePath);

        when(imageRepository.save(any(Image.class))).thenReturn(image1);

        //when
        ImageFile imageFile = postService.addImage(multipartFile);

        //then
        verify(imageRepository, times(1)).save(any());
        assertEquals(imageFile.getUploadFilePath(),filename + "." + contentType);
        assertEquals(imageFile.getStoreFilePath(), new StringBuilder(filename).reverse().toString() + "." + contentType);

    }



    private MockMultipartFile getMockMultipartFile(String filename, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(filename, filename + "." + contentType, contentType, fileInputStream);


    }

    class MockFileStore extends FileStore {
        @Override
        public ImageFile storeFile(MultipartFile multipartFile) throws IOException {
            // 없으면 기본 이미지 생성
            if (multipartFile.isEmpty()) {
                return null;
            }
            String originalFilename = multipartFile.getOriginalFilename();
            String storeFileName = createStoreFileName(originalFilename);
//            multipartFile.transferTo(new File(getFullPath(storeFileName)));
            return new ImageFile(originalFilename, storeFileName);
        }

        private String createStoreFileName(String originalFilename) {
            int pos = originalFilename.lastIndexOf(".");
            String ext = extractExt(originalFilename);
            String reverseFilename = new StringBuilder(originalFilename.substring(0,pos)).reverse().toString();
            return reverseFilename + "." + ext;
        }
        private String extractExt(String originalFilename) {
            int pos = originalFilename.lastIndexOf(".");
            return originalFilename.substring(pos + 1);
        }
    }
    @Test
    @DisplayName("게시글 등록")
    void addPost() {

        AddPostRequest addPostRequest = AddPostRequest.builder()
                .imagePaths(List.of(image1.getSrc(), image2.getSrc()))
                .content(content)
                .build();

//        Long fakePostId = 11L;
//        ReflectionTestUtils.setField(post, "postId", fakePostId);

        Hashtag hashtag = new Hashtag(100L, "리액트", new ArrayList<>());
        PostHashtag postHashtag = new PostHashtag(1000L, post, hashtag);
        post.getPostHashtags().add(postHashtag);
        hashtag.getPostHashtags().add(postHashtag);

        when(imageRepository.findBySrc(image1.getSrc())).thenReturn(Optional.ofNullable(image1));
        when(imageRepository.findBySrc(image2.getSrc())).thenReturn(Optional.ofNullable(image2));
//
        when(hashtagRepository.findByName("리액트")).thenReturn(Optional.ofNullable(hashtag));
        when(hashtagRepository.save(any(Hashtag.class))).thenReturn(new Hashtag(101L,"노드", List.of(postHashtag)));
        when(postHashtagRepository.save(any(PostHashtag.class))).thenReturn(postHashtag);

        when(postRepository.save(any(Post.class))).thenReturn(post);

        //when
        AddPostResponse addPostResponse = postService.addPost(addPostRequest, loginUser);

        //then
        verify(imageRepository, times(2)).findBySrc(any());
        verify(hashtagRepository, times(1)).findByName("리액트");
        verify(hashtagRepository, times(1)).save(any());
        verify(postHashtagRepository, times(2)).save(any());
        verify(postRepository, times(1)).save(any());

        assertEquals(loginUser.getNickname(), addPostResponse.getUser().getNickname());
        assertEquals(post.getImages(), addPostResponse.getImages());
        assertEquals(post.getContent(), addPostResponse.getContent());
    }

    @Test
    @DisplayName("게시글 등록 실패 (이미지 경로 X)")
    void addPostExImageSrc() {
        AddPostRequest addPostRequest = AddPostRequest.builder()
                .imagePaths(List.of(image1.getSrc(), image2.getSrc()))
                .content(content)
                .build();

        when(imageRepository.findBySrc(image1.getSrc())).thenReturn(Optional.ofNullable(null));
        assertThrows(IllegalArgumentException.class, ()->  postService.addPost(addPostRequest, loginUser));

    }



    @Test
    @DisplayName("게시글 수정")
    void updatePost() {

        String updateContent = "update Content #구글";
        UpdatePostRequest updatePostRequest = new UpdatePostRequest();
        updatePostRequest.setPostId(post.getPostId());
        updatePostRequest.setContent(updateContent);

        Hashtag hashtag = new Hashtag(100L, "리액트", new ArrayList<>());
        PostHashtag postHashtag = new PostHashtag(1000L, post, hashtag);
        hashtag.getPostHashtags().add(postHashtag);
        post.getPostHashtags().add(postHashtag);

        Hashtag newHashtag = new Hashtag(102L, "구글", new ArrayList<>());
        PostHashtag newPostHashtag = new PostHashtag(1001L, post, newHashtag);
        newHashtag.getPostHashtags().add(newPostHashtag);

        when(postRepository.findById(post.getPostId())).thenReturn(Optional.ofNullable(post));

        when(hashtagRepository.findByName("구글")).thenReturn(Optional.ofNullable(null));

        when(hashtagRepository.save(any(Hashtag.class))).thenReturn(newHashtag);
        when(postHashtagRepository.save(any(PostHashtag.class))).thenReturn(newPostHashtag);

        //when
        UpdatePostResponse updatePostResponse = postService.updatePost(updatePostRequest, loginUser);
        //then
        verify(hashtagRepository, times(1)).findByName(any());
        verify(hashtagRepository, times(1)).save(any());
        verify(postHashtagRepository, times(1)).findByPost(any());
        verify(postHashtagRepository, times(1)).save(any());
        verify(postRepository, times(1)).findById(any());

        assertEquals(updatePostRequest.getContent(), updatePostResponse.getContent());

    }

    @Test
    @DisplayName("게시글 수정 실패 (Post X)")
    void updatePostExPost() {
        UpdatePostRequest updatePostRequest = new UpdatePostRequest();
        updatePostRequest.setContent("업데이트 내용");

        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(InvalidatePostException.class, ()->  postService.updatePost(updatePostRequest, loginUser));
    }

}



