package com.bigauth.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiErrorDTO {

    private int status;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;
    
    private String message;
    private String debugMessage;

    private ApiErrorDTO() {
        timestamp = LocalDateTime.now();
    }

    public ApiErrorDTO(int status) {
        this();
        this.status = status;
    }

    public ApiErrorDTO(int status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public ApiErrorDTO(int status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
}
