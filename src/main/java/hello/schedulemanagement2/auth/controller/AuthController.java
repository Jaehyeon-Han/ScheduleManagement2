package hello.schedulemanagement2.auth.controller;

import hello.schedulemanagement2.auth.dto.request.LoginRequest;
import hello.schedulemanagement2.auth.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static hello.schedulemanagement2.global.constant.SessionConst.LOGIN_USER;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
        @Valid @RequestBody LoginRequest loginRequest,
        HttpSession httpSession
    ) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // 실패 시 LoginFailException 예외 발생
        Long loginUserId = authService.login(email, password);

        httpSession.setAttribute(LOGIN_USER, loginUserId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
