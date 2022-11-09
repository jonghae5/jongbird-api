package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.Follow;
import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.repository.follow.FollowRepository;
import com.jonghae5.jongbirdapi.repository.post.PostRepository;
import com.jonghae5.jongbirdapi.repository.user.UserRepository;
import com.jonghae5.jongbirdapi.view.user.CreateUserRequest;
import com.jonghae5.jongbirdapi.view.user.LoginUserRequest;
import com.jonghae5.jongbirdapi.view.user.UserWithoutPasswordResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;


    @Transactional
    public User join(CreateUserRequest createUserRequest) {
        validateDuplicateEmail(createUserRequest.getEmail());

        log.info("signup={}", createUserRequest.getEmail());
        log.info("signup={}", createUserRequest.getNickname());

        //TODO
        // SECURITY 적용
        User newUser = User.builder()
                .nickname(createUserRequest.getNickname())
                .email(createUserRequest.getEmail())
                .password(createUserRequest.getPassword()).build();

        User saveUser = userRepository.save(newUser);
        return saveUser;
    }

    private void validateDuplicateEmail(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public User login(LoginUserRequest loginUserRequest) {
        User loginUser = userRepository.findByEmail(loginUserRequest.getEmail()).orElseThrow(IllegalStateException::new);
//        if (loginUser.isEmpty()) {
//            throw new IllegalStateException("존재하지 않는 회원입니다.");
//        }

        return loginUser;
    }

    public UserWithoutPasswordResponse findUserWithoutPassword(Long userId) {

        // 없으면 nullPointException
        User user = userRepository.findById(userId).orElseThrow(IllegalStateException::new);

        List<Post> posts = postRepository.findByUser(user);
        List<Follow> followers = followRepository.findByFollower(user);
        List<Follow> followings = followRepository.findByFollowing(user);

        UserWithoutPasswordResponse userWithoutPasswordResponse = new UserWithoutPasswordResponse();
        userWithoutPasswordResponse.create(user, posts, followers, followings);
        return userWithoutPasswordResponse;
    }

    public void changeNickname(User loginUser, String nickname) {
        Optional<User> userDuplicatedNickname = userRepository.findByNickname(nickname);
        if (userDuplicatedNickname.isPresent()) {
            log.error("같은 닉네임을 가지고 있습니다.");
        }
        User findUser = userRepository.findById(loginUser.getUserId()).orElseThrow(IllegalStateException::new);
        findUser.updateNickname(nickname);
    }
}
