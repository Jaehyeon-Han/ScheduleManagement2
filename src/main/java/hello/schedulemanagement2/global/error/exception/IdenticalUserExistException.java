package hello.schedulemanagement2.global.error.exception;

public class IdenticalUserExistException extends RuntimeException{

    public IdenticalUserExistException() {
    }

    public IdenticalUserExistException(String message) {
        super(message);
    }
}
