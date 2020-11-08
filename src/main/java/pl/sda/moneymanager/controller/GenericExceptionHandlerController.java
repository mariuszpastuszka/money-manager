package pl.sda.moneymanager.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import static pl.sda.moneymanager.controller.ControllersConstants.ERROR_DESC;

@Slf4j
@ControllerAdvice
public class GenericExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    public ModelAndView errorPage(Exception exc) {
        log.warn("exception occurred", exc);

        ModelAndView result = new ModelAndView("error-page");
        result.addObject(ERROR_DESC, exc.getMessage());

        return result;
    }
}
