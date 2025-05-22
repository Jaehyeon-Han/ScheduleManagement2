package hello.schedulemanagement2.auth.service;

import hello.schedulemanagement2.config.PasswordEncoder;
import hello.schedulemanagement2.entity.User;
import hello.schedulemanagement2.global.error.exception.ForbiddenException;
import hello.schedulemanagement2.global.error.exception.LoginFailException;
import hello.schedulemanagement2.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long login(String email, String password) {
        Optional<User> optionalFoundUser = userRepository.findByEmail(email);

        if(optionalFoundUser.isEmpty()) {
            throw new LoginFailException();
        }

        User foundUser = optionalFoundUser.get();
        checkPasswordMatchesOrElseThrow(foundUser.getPasswordHash(), password);

        return foundUser.getId();
    }

    private void checkPasswordMatchesOrElseThrow(String savedPasswordHash, String requestPassword) {
        boolean passwordDiffers = !passwordEncoder.matches(requestPassword, savedPasswordHash);

        if (passwordDiffers) {
            throw new LoginFailException();
        }
    }
}
