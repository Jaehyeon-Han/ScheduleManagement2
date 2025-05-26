package hello.schedulemanagement2.schedule.repository;

import hello.schedulemanagement2.entity.Schedule;
import hello.schedulemanagement2.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    default Schedule findByIdOrThrow(Long id) {
        return findById(id).stream().findAny().orElseThrow(NotFoundException::new);
    }
}
