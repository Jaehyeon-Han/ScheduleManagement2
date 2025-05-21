package hello.schedulemanagement2.global.error;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {
    private final String title;
    private final String detail;
    private final int status;
    private final String instance;
    private final List<FieldErrorResponse> errors = new ArrayList<>();

    public ErrorResponse(String title, String detail, int status, String instance) {
        this.title = title;
        this.detail = detail;
        this.status = status;
        this.instance = instance;
    }

    public void addFieldError(String field, String reason) {
        FieldErrorResponse fieldErrorResponse = new FieldErrorResponse(field, reason);
        errors.add(fieldErrorResponse);
    }
}
