package hello.schedulemanagement2.user.service;

import hello.schedulemanagement2.config.PasswordEncoder;
import hello.schedulemanagement2.entity.User;
import hello.schedulemanagement2.global.error.exception.ForbiddenException;
import hello.schedulemanagement2.global.error.exception.IdenticalUserExistException;
import hello.schedulemanagement2.user.dto.request.ChangePasswordRequest;
import hello.schedulemanagement2.user.dto.request.CreateUserRequest;
import hello.schedulemanagement2.user.dto.request.DeleteUserRequest;
import hello.schedulemanagement2.user.dto.response.UserResponse;
import hello.schedulemanagement2.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse saveUser(@Valid CreateUserRequest createUserRequest) {
        String name = createUserRequest.getName();
        String email = createUserRequest.getEmail();
        String password = createUserRequest.getPassword();

        // 중복 회원 존재 여부 확인
        Optional<User> identicalUser = userRepository.findByEmail(email);
        if (identicalUser.isPresent()) {
            throw new IdenticalUserExistException();
        }

        // 비밀번호 암호화
        String passwordHash = passwordEncoder.encode(password);
        
        User newUser = new User(name, email, passwordHash);
        User savedUser = userRepository.save(newUser);
        return UserResponse.fromUser(savedUser);
    }

    public Page<UserResponse> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponse::fromUser);
    }

    public UserResponse findUserById(long userId) {
        User foundUser = userRepository.findByIdOrThrow(userId);

        return UserResponse.fromUser(foundUser);
    }

    public UserResponse changePasswordById(long userId, @Valid ChangePasswordRequest changePasswordRequest) {

        // 비밀번호 확인, 불일치 시 ForbiddenException 발생
        User foundUser = userRepository.findByIdOrThrow(userId);
        String currentPassword = changePasswordRequest.getCurrentPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        checkPasswordMatchesOrElseThrowForbidden(foundUser.getPasswordHash(), currentPassword);

        String newPasswordHash = passwordEncoder.encode(newPassword);
        foundUser.setPasswordHash(newPasswordHash);

        userRepository.flush(); // flush 안 하면 테스트에서 auditing 실패

        return UserResponse.fromUser(foundUser);
    }

    public void deleteUserById(long userId, DeleteUserRequest deleteUserRequest) {
        // 삭제 시 비밀번호 재확인, 불일치 시 ForbiddenException 발생
        User foundUser = userRepository.findByIdOrThrow(userId);
        String requestPassword = deleteUserRequest.getCurrentPassword();
        checkPasswordMatchesOrElseThrowForbidden(foundUser.getPasswordHash(), requestPassword);

        userRepository.delete(foundUser);
    }

    private void checkPasswordMatchesOrElseThrowForbidden(String savedPasswordHash, String requestPassword) {
        boolean passwordMatches = passwordEncoder.matches(requestPassword, savedPasswordHash);

        // 비밀번호 불일치 시 요청 거절
        if (!passwordMatches) {
            throw new ForbiddenException("비밀번호가 일치하지 않습니다.");
        }
    }
}
