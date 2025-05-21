package hello.schedulemanagement2.user.dto.response;

import hello.schedulemanagement2.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UserResponse {

    private final Long id;

    private final String email;

    private final String name;

    private final LocalDateTime createdAt;

    private final LocalDateTime lastUpdatedAt;

    public static UserResponse fromUser(User user) {
        Long id = user.getId();
        String email = user.getEmail();
        String name = user.getName();
        LocalDateTime createdAt = user.getCreatedAt();
        LocalDateTime updatedAt = user.getLastUpdatedAt();

        return new UserResponse(id, email, name, createdAt, updatedAt);
    }
}
