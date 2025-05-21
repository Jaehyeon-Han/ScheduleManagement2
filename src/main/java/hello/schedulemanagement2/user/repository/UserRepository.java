package hello.schedulemanagement2.user.repository;

import hello.schedulemanagement2.entity.User;
import hello.schedulemanagement2.global.error.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    default User findUserByIdOrElseThrow(Long id) {
        return findById(id).stream().findAny().orElseThrow(UserNotFoundException::new);
    }

    Optional<User> findByNameAndEmail(String name, String email);
}
