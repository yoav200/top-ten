package com.alhalel.topten.exceptions;

import com.alhalel.topten.model.MessageHolder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Log4j2
//@ControllerAdvice
@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody MessageHolder handleException(Exception exception, HttpServletRequest request) {

        log.error(exception);
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.getMessages().add(
                MessageHolder.Message.builder()
                        .severity(MessageHolder.Severity.ERROR)
                        .title("Something bad happened...")
                        .text(exception.getMessage())
                        .build()
        );

        return messageHolder;
    }
}
