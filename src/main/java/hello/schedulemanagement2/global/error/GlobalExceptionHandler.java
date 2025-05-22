package hello.schedulemanagement2.global.error;

import hello.schedulemanagement2.global.error.exception.ForbiddenException;
import hello.schedulemanagement2.global.error.exception.IdenticalUserExistException;
import hello.schedulemanagement2.global.error.exception.LoginFailException;
import hello.schedulemanagement2.global.error.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 빈 검증 에러
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = new ErrorResponse(
            status.getReasonPhrase(),
            "입력값을 다시 확인하세요",
            status.value(),
            request.getRequestURI()
        );

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorResponse.addFieldErrorResponse(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // 로그인 실패 시 실패 이유는 숨김
    @ExceptionHandler(LoginFailException.class)
    public ResponseEntity<ErrorResponse> handleLoginFailException(LoginFailException ex, HttpServletRequest request) {
        return createErrorResponseResponseEntity(ex, HttpStatus.UNAUTHORIZED, "아이디와 비밀번호를 다시 확인하세요.", request);
    }

    // 이메일이 중복되면 중복 가입으로 간주
    @ExceptionHandler(IdenticalUserExistException.class)
    public ResponseEntity<ErrorResponse> handleIdenticalUserException(IdenticalUserExistException ex, HttpServletRequest request) {
        return createErrorResponseResponseEntity(ex, HttpStatus.CONFLICT, "해당 이메일로 가입한 사용자가 있습니다.", request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(HttpServletRequest request) {
        // Todo
        return null;
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex, HttpServletRequest request) {
        return createErrorResponseResponseEntity(ex, HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", request);
    }

    private ResponseEntity<ErrorResponse> createErrorResponseResponseEntity(
        Exception ex,
        HttpStatus status,
        String detail,
        HttpServletRequest request)
    {
        if (ex.getMessage() != null) {
            detail = ex.getMessage();
        }

        ErrorResponse errorResponse = new ErrorResponse(
            status.getReasonPhrase(),
            detail,
            status.value(),
            request.getRequestURI()
        );

        return ResponseEntity.status(status.value()).body(errorResponse);
    }
}
