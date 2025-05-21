package hello.schedulemanagement2.user.repository;

import hello.schedulemanagement2.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PureJpaUserRepository {

    @PersistenceContext
    private EntityManager em;

    public User save(User user) {
        return null;
    }

    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    public Optional<User> findByNameAndEmail(String name, String email) {
        return Optional.empty();
    }

    public List<User> findAll() {
        return List.of();
    }


}
