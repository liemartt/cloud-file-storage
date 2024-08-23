package com.liemartt.cloud;

import com.liemartt.cloud.dto.UserDto;
import com.liemartt.cloud.entity.User;
import com.liemartt.cloud.exception.UsernameAlreadyExistsException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class CloudControllerAdvice {
    
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public RedirectView handleNonUniqueUsernameException(UsernameAlreadyExistsException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return new RedirectView("/signup");
    }
}
