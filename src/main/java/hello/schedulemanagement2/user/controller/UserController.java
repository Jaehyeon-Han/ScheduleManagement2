package hello.schedulemanagement2.user.controller;

import hello.schedulemanagement2.global.constant.SessionConst;
import hello.schedulemanagement2.user.dto.request.CreateUserRequest;
import hello.schedulemanagement2.user.dto.request.ChangePasswordRequest;
import hello.schedulemanagement2.user.dto.request.DeleteUserRequest;
import hello.schedulemanagement2.user.dto.response.UserResponse;
import hello.schedulemanagement2.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserResponse> userResponsePage = userService.getUsers(pageable);
        return ResponseEntity.ok(userResponsePage);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> saveUser(
        @RequestBody @Valid CreateUserRequest createUserRequest) {
        UserResponse userResponse = userService.saveUser(createUserRequest);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> findUserById(@SessionAttribute(name = SessionConst.LOGIN_USER) Long userId) {
        UserResponse userResponse = userService.findUserById(userId);
        return ResponseEntity.ok(userResponse);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateUserById(
        @SessionAttribute(name = SessionConst.LOGIN_USER) Long userId,
        @RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        UserResponse userResponse = userService.changePasswordById(userId, changePasswordRequest);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUserById(
        @SessionAttribute(name = SessionConst.LOGIN_USER) Long userId,
        @RequestBody @Valid DeleteUserRequest deleteUserRequest) {
        userService.deleteUserById(userId, deleteUserRequest);
        return ResponseEntity.noContent().build();
    }
}
