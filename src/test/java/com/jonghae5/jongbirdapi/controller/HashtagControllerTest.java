package com.jonghae5.jongbirdapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonghae5.jongbirdapi.domain.*;
import com.jonghae5.jongbirdapi.service.HashtagService;
import com.jonghae5.jongbirdapi.view.hashtag.GetPostWithHashtagResponse;
import com.jonghae5.jongbirdapi.view.result.ResultService;
import com.jonghae5.jongbirdapi.web.security.SecurityConfig;
import com.jonghae5.jongbirdapi.web.session.SessionConst;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(HashtagController.class)
@Import(SecurityConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
class HashtagControllerTest {

    private static User user;
    private static Comment comment;
    private static Post userPost;
    private static Image image1;
    private static Image image2;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    HashtagService hashtagService;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    ResultService resultService;

    private MockHttpSession session;
    @BeforeEach
    void setupBeforeEach() {


        user = User.builder()
                .userId(1L)
                .email("testEmail@naver.com")
                .password("testPassword@!@#123")
                .nickname("testNickname")
                .build();

        session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_USER, user);

        image1 = Image.builder()
                .imageId(1L)
                .src("/test/image1.jpeg")
                .build();

        image2 = Image.builder()
                .imageId(2L)
                .src("/test/image2.jpeg")
                .build();

        comment = Comment.builder()
                .commentId(11L)
                .content("test Comment")
                .build();

        userPost = Post.builder()
                .content("test Content")
                .user(user)
                .images(List.of(image1,image2))
                .postId(10l)
                .build();

        comment.addUserAndPost(user, userPost);
    }


    @Test
    @DisplayName("[API][GET][getPostWithHashtag] 해시태그 게시글 가져오기")
    void getPostWithHashtag() throws Exception {
        Post otherPost = Post.builder()
                .content("#리액트 ㅋㅋㅋ ㄱㅇㅇ")
                .user(user)
                .postId(99l)
                .build();

        PostHashtag postHashtag = PostHashtag.builder()
                .build();
        Hashtag hashtag = Hashtag.builder()
                .name("리액트")
                .hashtagId(123L)
                .build();
        postHashtag.addPostHashtag(otherPost, hashtag);

        GetPostWithHashtagResponse getPostWithHashtagResponse = new GetPostWithHashtagResponse(otherPost);
        given(hashtagService.fetchPostPagesByHashtag(any(), anyString(), any())).willReturn(List.of(getPostWithHashtagResponse));

//         RequestParam 방식
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("lastId", String.valueOf(1));

        mockMvc.perform(get("/hashtag/{hashtag}", "리액트")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].User").exists())
                .andExpect(jsonPath("$.data[0].Comments").exists())
                .andExpect(jsonPath("$.data[0].Images").exists())
                .andExpect(jsonPath("$.data[0].Likers").exists())
                .andDo(print());

        verify(hashtagService).fetchPostPagesByHashtag(any(), anyString(), any());





    }

}