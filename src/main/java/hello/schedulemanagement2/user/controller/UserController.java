package hello.schedulemanagement2.user.controller;

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

    @PostMapping
    public ResponseEntity<UserResponse> saveUser(
        @RequestBody @Valid CreateUserRequest createUserRequest) {
        UserResponse userResponse = userService.saveUser(createUserRequest);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable long userId) {
        UserResponse userResponse = userService.findUserById(userId);
        return ResponseEntity.ok(userResponse);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUserById(
        @PathVariable long userId,
        @RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        UserResponse userResponse = userService.changePasswordById(userId, changePasswordRequest);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(
        @PathVariable long userId,
        @RequestBody @Valid DeleteUserRequest deleteUserRequest) {
        userService.deleteUserById(userId, deleteUserRequest);
        return ResponseEntity.noContent().build();
    }
}
