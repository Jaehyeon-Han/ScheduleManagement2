package hello.schedulemanagement2.global.error;

import hello.schedulemanagement2.global.error.exception.ForbiddenException;
import hello.schedulemanagement2.global.error.exception.IdenticalUserExistException;
import hello.schedulemanagement2.global.error.exception.LoginFailException;
import hello.schedulemanagement2.global.error.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 기본 404 예외 등도 응답 형식을 맞춰주려면 별도의 ErrorController가 필요
    
    // JSON 형식 에러
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJson(HttpServletRequest request) {
        // createErrorResponseResponseEntity()는 예외에 커스텀 예외를 가정하여 직접 메시지를 넣은 경우, 해당 메시지를 우선시하는데
        // HttpMessageNotReadableException은 ex에 Jackson의 예외 메시지가 담겨서 와서 별도 처리
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = new ErrorResponse(
            status.getReasonPhrase(),
            "입력값을 다시 확인하세요",
            status.value(),
            request.getRequestURI()
        );

        return ResponseEntity.status(status.value()).body(errorResponse);
    }

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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return createErrorResponseResponseEntity(ex, HttpStatus.NOT_FOUND, "리소스가 없습니다.", request);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex, HttpServletRequest request) {
        return createErrorResponseResponseEntity(ex, HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", request);
    }

    private ResponseEntity<ErrorResponse> createErrorResponseResponseEntity(
        Exception ex,
        HttpStatus status,
        String detail,
        HttpServletRequest request) {
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
