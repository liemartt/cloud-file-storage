package com.liemartt.cloud;

import com.liemartt.cloud.exception.*;
import org.bouncycastle.jce.exception.ExtCertificateEncodingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class CloudControllerAdvice {
    
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public RedirectView handleNonUniqueUsernameException(UsernameAlreadyExistsException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return new RedirectView("/signup");
    }
    
    @ExceptionHandler(InvalidUserSignUpRequestException.class)
    public RedirectView handleInvalidUserRequestException(InvalidUserSignUpRequestException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return new RedirectView("/signup");
    }
    
    @ExceptionHandler(FileOperationException.class)
    public RedirectView handleFileOperationException(FileOperationException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return new RedirectView("/");
    }
    
    @ExceptionHandler(FolderOperationException.class)
    public RedirectView handleFolderOperationException(FolderOperationException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return new RedirectView("/");
    }
    
    @ExceptionHandler(SearchOperationException.class)
    public RedirectView handleSearchOperationException(SearchOperationException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return new RedirectView("/");
    }
    
    @ExceptionHandler(NotEnoughSpaceException.class)
    public RedirectView notEnoughSpaceException(NotEnoughSpaceException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return new RedirectView("/");
    }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public RedirectView maxUploadSizeException(MaxUploadSizeExceededException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage() + "(max object size - 100Mb)");
        return new RedirectView("/");
    }
}
