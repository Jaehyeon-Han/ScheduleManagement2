package hello.schedulemanagement2.schedule.controller;

import hello.schedulemanagement2.global.constant.SessionConst;
import hello.schedulemanagement2.schedule.dto.request.CreateScheduleRequest;
import hello.schedulemanagement2.schedule.dto.request.UpdateScheduleRequest;
import hello.schedulemanagement2.schedule.dto.response.ScheduleResponse;
import hello.schedulemanagement2.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponse> saveSchedule(
        @SessionAttribute(name = SessionConst.LOGIN_USER) Long userId,
        @RequestBody @Valid CreateScheduleRequest request
    ) {
        ScheduleResponse scheduleResponse = scheduleService.save(userId, request);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(scheduleResponse.getId()).toUri();

        return ResponseEntity.created(location).body(scheduleResponse);
    }

    @GetMapping
    public ResponseEntity<Page<ScheduleResponse>> getSchedules(
        @PageableDefault(page = 0, size = 10, sort = "lastUpdatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(scheduleService.getSchedules(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> findScheduleById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(scheduleService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateScheduleById(
        @SessionAttribute(name = SessionConst.LOGIN_USER) Long userId,
        @PathVariable(name = "id") Long scheduleId,
        @RequestBody @Valid UpdateScheduleRequest request
    ) {
        return ResponseEntity.ok(scheduleService.updateScheduleById(userId, scheduleId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduleById(
        @SessionAttribute(name = SessionConst.LOGIN_USER) Long userId,
        @PathVariable(name = "id") Long scheduleId
    ) {
        scheduleService.deleteById(userId, scheduleId);
        return ResponseEntity.noContent().build();
    }
}
