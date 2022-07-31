package com.alhalel.topten.exceptions;

import com.alhalel.topten.model.MessageHolder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ControllerAdvice
public class DefaultExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody MessageHolder handleException(final Exception exception) {

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
