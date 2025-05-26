package hello.schedulemanagement2.user.repository;

import hello.schedulemanagement2.entity.User;
import hello.schedulemanagement2.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    default User findByIdOrThrow(Long id) {
        return findById(id).stream().findAny().orElseThrow(NotFoundException::new);
    }

    Optional<User> findByEmail(String email);
}
