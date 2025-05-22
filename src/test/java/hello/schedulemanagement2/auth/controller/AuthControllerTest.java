package hello.schedulemanagement2.auth.controller;

import hello.schedulemanagement2.auth.service.AuthService;
import hello.schedulemanagement2.config.PasswordEncoder;
import hello.schedulemanagement2.entity.User;
import hello.schedulemanagement2.user.dto.request.CreateUserRequest;
import hello.schedulemanagement2.user.dto.response.UserResponse;
import hello.schedulemanagement2.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AuthControllerTest {



    @Test
    void logout() {
    }
}