package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping
    public String uploadFile(
            Authentication authentication, FileForm fileForm, Model model) throws IOException {
        String error = null;
        String result = "success";

        Integer userId = getUserId(authentication);
        MultipartFile multipartFile = fileForm.getFile();
        String fileName = multipartFile.getOriginalFilename();
        if(!fileService.isFilenameAvailable(fileName, userId)) {
            error = "The filename already exists.";
            result = "error";
        } else if(fileName.isEmpty()) {
            error = "The filename should not be empty.";
            result = "error";
        }
        if(error == null) {
            int rowsChanged = fileService.addFile(multipartFile, authentication.getName());
            if(rowsChanged == 0) {
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
    public byte[] getFile(Authentication authentication, @PathVariable String fileName) {
        Integer userId = getUserId(authentication);
        return fileService.getFile(fileName, userId).getFileData();
    }

    @GetMapping(value = "/delete-file/{fileName}")
    public String deleteFile(@PathVariable String fileName, Model model) {
        String result = "success";
        int rowsChanged = fileService.deleteFile(fileName);
        if(rowsChanged == 0) {
            result = "notsaved";
        }
        model.addAttribute("result", result);
        return "result";
    }

    private Integer getUserId(Authentication authentication) {
        User user = userService.getUser(authentication.getName());
        return user.getUserId();
    }
}
