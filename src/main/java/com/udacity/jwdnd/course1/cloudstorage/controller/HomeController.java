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
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;

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
        Integer userId = getUserId(authentication);
        model.addAttribute("files", fileService.getFilesList(userId));
        model.addAttribute("notes", noteService.getNotesList(userId));
        model.addAttribute("credentials", credentialService.getCredentialsList(userId));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @PostMapping
    public String uploadFile(
            Authentication authentication, FileForm fileForm, NoteForm noteForm,
            CredentialForm credentialForm, Model model) throws IOException {
        String error = null;
        String result = "success";

        Integer userId = getUserId(authentication);
        MultipartFile multipartFile = fileForm.getFile();
        String fileName = multipartFile.getOriginalFilename();
        if(!fileService.isFilenameAvailable(userId, fileName)) {
            error = "The filename already exists.";
            result = "error";
        }
        if(error == null) {
            int rowsAdded = fileService.addFile(multipartFile, authentication.getName());
            if(rowsAdded < 0) {
                result = "notsaved";
            }
        }
        model.addAttribute("result", result);
        if(error != null) {
            model.addAttribute("message", error);
        }
        return "result";
    }

    @GetMapping(
            value = "/get-file/{fileName}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public byte[] getFile(@PathVariable String fileName) {
        return fileService.getFile(fileName).getFileData();
    }

    @GetMapping(value = "/delete-file/{fileName}")
    public String deleteFile(
            Authentication authentication, @PathVariable String fileName,
            FileForm fileForm, NoteForm noteForm, CredentialForm credentialForm,
            Model model) {
        fileService.deleteFile(fileName);
        model.addAttribute("result", "success");
        return "result";
    }

    private Integer getUserId(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        return user.getUserId();
    }
}
