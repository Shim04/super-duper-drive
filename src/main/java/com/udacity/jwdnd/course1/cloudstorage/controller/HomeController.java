package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final UserService userService;
    private final EncryptionService encryptionService;
    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;

    public HomeController(UserService userService, EncryptionService encryptionService, FileService fileService, NoteService noteService, CredentialService credentialService) {
        this.userService = userService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping
    public String getHomePage(
            Authentication authentication, FileForm fileForm, NoteForm noteForm,
            CredentialForm credentialForm, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        if(user != null) {
            Integer userId = user.getUserId();
            model.addAttribute("files", fileService.getFilesList(userId));
            model.addAttribute("notes", noteService.getNotesList(userId));
            model.addAttribute("credentials", credentialService.getCredentialsList(userId));
            model.addAttribute("encryptionService", encryptionService);
            return "home";
        }
        return "signup";
    }
}
