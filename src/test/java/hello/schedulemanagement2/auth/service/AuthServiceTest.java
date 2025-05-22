package hello.schedulemanagement2.auth.service;

import hello.schedulemanagement2.config.PasswordEncoder;
import hello.schedulemanagement2.entity.User;
import hello.schedulemanagement2.global.error.exception.LoginFailException;
import hello.schedulemanagement2.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private Long godUserId;

    @BeforeEach
    void saveOneUser() {
        String email = "god@heaven.world";
        String password = "iamgod";
        String name = "god";

        User user = new User(name, email, passwordEncoder.encode(password));
        User savedUser = userRepository.save(user);

        godUserId = savedUser.getId();
    }

    @DisplayName("정상 로그인")
    @Test
    void login_withValidPassword_willSucceed() {
        // given
        String email = "god@heaven.world";
        String password = "iamgod";

        // when
        Long loginUserId = authService.login(email, password);

        // then
        assertThat(loginUserId).isEqualTo(godUserId);
    }

    @DisplayName("없는 이메일로 로그인 시 로그인 실패")
    @Test
    void login_withNonExistentEmail_willThrow() {
        // given
        String email = "notUser@email.com";
        String password = "password";

        // when-then
        assertThrows(LoginFailException.class, () -> authService.login(email, password));
    }

    @DisplayName("비밀번호 불일치 시 로그인 실패")
    @Test
    void login_withInvalidPassword_willThrow() {
        // given
        String email = "god@heaven.world";
        String password = "iamfalsegod";

        // when-then
        assertThrows(LoginFailException.class, () -> authService.login(email, password));
    }
}