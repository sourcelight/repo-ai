/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.store.products.errorHandling;

import ai.example.store.products.exceptions.ResourceNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        ModelAndView mav = new ModelAndView("error-404");
        mav.addObject("message", ex.getMessage());
        return mav;
    }


    @ExceptionHandler(Exception.class)
    public ModelAndView globalExceptionHandler(Exception ex, Model model) {
        ModelAndView mav = new ModelAndView("error-generic");
        mav.addObject("message", ex.getMessage());
        return mav;
    }


}