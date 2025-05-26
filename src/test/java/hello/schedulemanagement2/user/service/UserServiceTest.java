package hello.schedulemanagement2.user.service;

import hello.schedulemanagement2.config.PasswordEncoder;
import hello.schedulemanagement2.entity.User;
import hello.schedulemanagement2.global.error.exception.ForbiddenException;
import hello.schedulemanagement2.global.error.exception.IdenticalUserExistException;
import hello.schedulemanagement2.global.error.exception.NotFoundException;
import hello.schedulemanagement2.user.dto.request.ChangePasswordRequest;
import hello.schedulemanagement2.user.dto.request.CreateUserRequest;
import hello.schedulemanagement2.user.dto.request.DeleteUserRequest;
import hello.schedulemanagement2.user.dto.response.UserResponse;
import hello.schedulemanagement2.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private Long godUserId;

    private static final long INVALID_ID = 999999L;

    @BeforeEach
    void saveOneUser() {
        String email = "god@heaven.world";
        String password = "iamgod";
        String name = "god";

        User user = new User(name, email, passwordEncoder.encode(password));
        User savedUser = userRepository.save(user);

        godUserId = savedUser.getId();
    }

    @DisplayName("정상 회원가입 성공")
    @Test
    void saveUser_withUniqueNameAndEmail_shouldSucceed() {
        // given
        String email = "anothergod@heaven.world";
        String password = "iamgod";
        String name = "god";
        CreateUserRequest createUserRequest = new CreateUserRequest(email, password, name);

        // when
        UserResponse userResponse = userService.saveUser(createUserRequest);

        // then
        String savedEmail = userResponse.getEmail();
        String savedName = userResponse.getName();
        LocalDateTime createdAt = userResponse.getCreatedAt();
        LocalDateTime updateAt = userResponse.getLastUpdatedAt();

        assertThat(savedEmail).isEqualTo(email);
        assertThat(savedName).isEqualTo(name);
        assertThat(createdAt).isSameAs(updateAt);
    }
    
    @DisplayName("중복 회원가입 실패")
    @Test
    void saveUser_withIdenticalNameAndEmail_shouldThrow() {
        // given
        String email = "god@heaven.world";
        String password = "iamgod";
        String name = "anothergod";
        CreateUserRequest createUserRequest = new CreateUserRequest(email, password, name);

        // when-then
        assertThrows(IdenticalUserExistException.class, () -> userService.saveUser(createUserRequest));
    }

    @DisplayName("정상 조회")
    @Test
    void findUserById_withExistingId_shouldSucceed() {
        // given (@BeforeEach)

        // when
        UserResponse userResponse = userService.findUserById(godUserId);

        // then
        String savedEmail = userResponse.getEmail();
        String savedName = userResponse.getName();
        assertThat(savedEmail).isEqualTo("god@heaven.world");
        assertThat(savedName).isEqualTo("god");
    }

    @DisplayName("없는 id 조회")
    @Test
    void findUserById_withAbsentId_shouldThrow() {
        // given (@BeforeEach)

        // when-then
        assertThrows(NotFoundException.class, () -> userService.findUserById(INVALID_ID));
    }

    @DisplayName("비밀번호 정상 변경")
    @Test
    void changePasswordById_withCorrectPassword_shouldSucceed() {
        // given (+ @BeforeEach)
        ChangePasswordRequest request = new ChangePasswordRequest("iamgod", "iamtruegod");

        // when
        UserResponse userResponse = userService.changePasswordById(godUserId, request);

        // then
        String savedEmail = userResponse.getEmail();
        String savedName = userResponse.getName();
        LocalDateTime createdAt = userResponse.getCreatedAt();
        LocalDateTime lastUpdatedAt = userResponse.getLastUpdatedAt();

        assertThat(savedEmail).isEqualTo("god@heaven.world");
        assertThat(savedName).isEqualTo("god");
        assertThat(createdAt).isNotEqualTo(lastUpdatedAt);
    }

    @DisplayName("비밀번호 불일치로 변경 실패")
    @Test
    void changePasswordById_withIncorrectPassword_shouldThrow() {
        // given (+ @BeforeEach)
        ChangePasswordRequest request = new ChangePasswordRequest("iamnotgod", "iamtruegod");

        // when-then
        assertThrows(ForbiddenException.class, () -> userService.changePasswordById(godUserId, request));
    }

    @DisplayName("정상 삭제")
    @Test
    void deleteUserById_withCorrectPassword_shouldSucceed() {
        // given
        DeleteUserRequest request = new DeleteUserRequest("iamgod");

        // when
        userService.deleteUserById(godUserId, request);

        // then
        assertThrows(NotFoundException.class, () -> userService.findUserById(1));
    }

    @DisplayName("비밀번호 불일치로 삭제 실패")
    @Test
    void deleteUserById_withCorrectPassword_shouldThrowAndShouldNotDelete() {
        // given
        DeleteUserRequest request = new DeleteUserRequest("iamnotgod");

        // when-then
        assertThrows(ForbiddenException.class, () -> userService.deleteUserById(godUserId, request));
        UserResponse userResponse = userService.findUserById(godUserId);
        assertThat(userResponse.getName()).isEqualTo("god");
    }
}