package server.ourhood.global.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import server.ourhood.global.handler.response.BaseException;
import server.ourhood.global.handler.response.BaseResponse;
import server.ourhood.global.handler.response.BaseResponseStatus;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<BaseResponse> handleGeneralException(BaseException e) {
        log.error(e.getStatus().toString());
        return ResponseEntity.status(e.getStatus().getHttpStatus())
                .body(BaseResponse.fail(e.getStatus()));
    }

    @ExceptionHandler
    public ResponseEntity<BaseResponse> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler
    public ResponseEntity<BaseResponse> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR));
    }
}

