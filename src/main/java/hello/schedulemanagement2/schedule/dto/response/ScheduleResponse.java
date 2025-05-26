package hello.schedulemanagement2.schedule.dto.response;

import hello.schedulemanagement2.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponse {
    private final Long id;
    private final String author;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastUpdatedAt;

    public static ScheduleResponse fromSchedule(Schedule savedSchedule) {
        Long id = savedSchedule.getId();
        String author = savedSchedule.getAuthor().getName();
        String title = savedSchedule.getTitle();
        String content = savedSchedule.getContent();
        LocalDateTime createdAt = savedSchedule.getCreatedAt();
        LocalDateTime lastUpdatedAt = savedSchedule.getLastUpdatedAt();

        return new ScheduleResponse(id, author, title, content, createdAt, lastUpdatedAt);
    }
}