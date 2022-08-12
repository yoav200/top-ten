package com.alhalel.topten.exceptions;

import com.alhalel.topten.model.MessageHolder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Log4j2
@ControllerAdvice
@AllArgsConstructor
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleException(Exception exception, HttpServletRequest request) {
        log.error(exception);
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.getMessages().add(
                MessageHolder.Message.builder()
                        .severity(MessageHolder.Severity.ERROR)
                        .title("Something went wrong")
                        .text(exception.getMessage())
                        .build()
        );

        if (isAjax(request)) {
            //If yes, return the error message directly
            return messageHolder;
        } else {
            // return error page name
            return new ModelAndView("error", Map.of("messages", messageHolder));
        }
    }


    private boolean isAjax(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null &&
                "XMLHttpRequest".equals(request.getHeader("X-Requested-With")));
    }

}
