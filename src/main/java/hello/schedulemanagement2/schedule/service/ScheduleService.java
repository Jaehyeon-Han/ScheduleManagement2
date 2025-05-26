package hello.schedulemanagement2.schedule.service;

import hello.schedulemanagement2.entity.Schedule;
import hello.schedulemanagement2.entity.User;
import hello.schedulemanagement2.global.error.exception.ForbiddenException;
import hello.schedulemanagement2.schedule.dto.request.CreateScheduleRequest;
import hello.schedulemanagement2.schedule.dto.request.UpdateScheduleRequest;
import hello.schedulemanagement2.schedule.dto.response.ScheduleResponse;
import hello.schedulemanagement2.schedule.repository.ScheduleRepository;
import hello.schedulemanagement2.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ScheduleResponse save(Long userId, @Valid CreateScheduleRequest request) {
        User user = userRepository.findByIdOrThrow(userId);

        Schedule schedule = new Schedule();
        schedule.setAuthor(user);
        schedule.setContent(request.getContent());
        schedule.setTitle(request.getTitle());

        Schedule savedSchedule = scheduleRepository.save(schedule);

        return ScheduleResponse.fromSchedule(savedSchedule);
    }

    public Page<ScheduleResponse> getSchedules(Pageable pageable) {
        return scheduleRepository.findAll(pageable).map(ScheduleResponse::fromSchedule);
    }

    public ScheduleResponse findById(Long id) {
        Schedule foundSchedule = scheduleRepository.findByIdOrThrow(id);
        return ScheduleResponse.fromSchedule(foundSchedule);
    }

    public ScheduleResponse updateScheduleById(
        Long userId,
        Long scheduleId,
        @Valid UpdateScheduleRequest request
    ) {
        // 자신의 할일만 수정 가능
        Schedule foundSchedule = getScheduleIfOwner(userId, scheduleId);

        String newTitle = request.getTitle();
        String newContent = request.getContent();
        if(newTitle != null) {
            foundSchedule.setTitle(newTitle);
        }
        if(newContent != null) {
            foundSchedule.setContent(newContent);
        }

        scheduleRepository.flush();
        return ScheduleResponse.fromSchedule(foundSchedule);
    }

    public void deleteById(Long userId, Long scheduleId) {
        // 자신의 할일만 삭제 가능
        Schedule foundSchedule = getScheduleIfOwner(userId, scheduleId);
        scheduleRepository.delete(foundSchedule);
    }

    private Schedule getScheduleIfOwner(Long userId, Long scheduleId) {
        Schedule foundSchedule = scheduleRepository.findByIdOrThrow(scheduleId);
        if(foundSchedule.getAuthor().getId() != userId) {
            throw new ForbiddenException();
        }
        return foundSchedule;
    }
}
