package hello.schedulemanagement2.global.error;

import hello.schedulemanagement2.global.error.exception.IdenticalUserExistException;
import hello.schedulemanagement2.global.error.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 이메일과 이름이 동시에 중복되면 중복 가입으로 간주
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleIdenticalUserException(
        IdenticalUserExistException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        ErrorResponse errorResponse = new ErrorResponse(status.getReasonPhrase(),
            "해당 이메일과 이름으로 가입한 사용자가 있습니다.",
            status.value(),
            request.getRequestURI());

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
        UserNotFoundException ex, HttpServletRequest request) {
        // Todo
        return null;
    }
}
