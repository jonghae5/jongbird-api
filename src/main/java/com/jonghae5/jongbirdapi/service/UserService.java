package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.Follow;
import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.exception.user.*;
import com.jonghae5.jongbirdapi.repository.follow.FollowRepository;
import com.jonghae5.jongbirdapi.repository.post.PostRepository;
import com.jonghae5.jongbirdapi.repository.user.UserRepository;
import com.jonghae5.jongbirdapi.view.user.ChangeNicknameResponse;
import com.jonghae5.jongbirdapi.view.user.CreateUserRequest;
import com.jonghae5.jongbirdapi.view.user.LoginUserRequest;
import com.jonghae5.jongbirdapi.view.user.UserWithoutPasswordResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public User join(CreateUserRequest createUserRequest) {
        validateDuplicateEmail(createUserRequest.getEmail());

        log.info("signup={}", createUserRequest.getEmail());
        log.info("signup={}", createUserRequest.getNickname());


        //암호화
        String encodePw = passwordEncoder.encode(createUserRequest.getPassword());
        // SECURITY 적용
        User newUser = User.builder()
                .nickname(createUserRequest.getNickname())
                .email(createUserRequest.getEmail())
                .password(encodePw).build();

        User saveUser = userRepository.save(newUser);
        return saveUser;
    }

    private void validateDuplicateEmail(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()) {
            throw new DuplicateIdException();

        }
    }

    public User login(LoginUserRequest loginUserRequest) {


        User loginUser = userRepository.findByEmail(loginUserRequest.getEmail()).orElseThrow(InvalidateEmailException::new);
        String encodePw = loginUser.getPassword();

        if(passwordEncoder.matches(loginUserRequest.getPassword(), encodePw)) {
            return loginUser;
        } else {
            throw new InvalidatePasswordException();
        }

    }

    public UserWithoutPasswordResponse findUserWithoutPassword(Long userId) {

        // 없으면 nullPointException
        User user = userRepository.findById(userId).orElseThrow(InvalidateUserException::new);

        List<Post> posts = postRepository.findByUser(user);
        List<Follow> followers = followRepository.findByFollower(user);
        List<Follow> followings = followRepository.findByFollowing(user);

        UserWithoutPasswordResponse userWithoutPasswordResponse = new UserWithoutPasswordResponse();
        userWithoutPasswordResponse.create(user, posts, followers, followings);
        return userWithoutPasswordResponse;
    }

    public ChangeNicknameResponse changeNickname(User loginUser, String nickname) {
        Optional<User> duplicatedUser = userRepository.findByNickname(nickname);
        if (duplicatedUser.isPresent()) {
            //TODO
            if (!loginUser.getNickname().equals(duplicatedUser.get().getNickname())) {
                throw new DuplicateNicknameException();
            }
        }
        User findUser = userRepository.findById(loginUser.getUserId()).orElseThrow(IllegalStateException::new);
        findUser.updateNickname(nickname);

        return new ChangeNicknameResponse(findUser.getNickname());

    }
}
