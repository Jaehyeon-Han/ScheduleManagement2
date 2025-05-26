package hello.schedulemanagement2.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateScheduleRequest {

    @NotBlank(message = "제목은 비어있을 수 없습니다.")
    @Size(min = 1, max = 30, message = "제목은 1글자 이상 30글자 이하여야 합니다.")
    private String title;

    @Size(max = 500, message = "내용은 500글자 이하여야 합니다.")
    private String content;
}
