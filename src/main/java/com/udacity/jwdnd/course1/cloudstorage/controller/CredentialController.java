package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credential")
public class CredentialController {
    private final CredentialService credentialService;
    private final UserService userService;
    private final EncryptionService encryptionService;

    public CredentialController(CredentialService credentialService, UserService userService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/add-credential")
    public String addCredential(
            Authentication authentication, CredentialForm credentialForm, Model model) {
        String username = authentication.getName();
        String url = credentialForm.getUrl();
        String credentialId = credentialForm.getCredentialId();
        String password = credentialForm.getPassword();
        String credentialUsername = credentialForm.getUsername();
        String result = "success";
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        int rowChanged;
        if(credentialId.isEmpty()) {
            rowChanged = credentialService.addCredential(url, credentialUsername, encodedKey, encryptedPassword, username);
        } else {
            Credential credential = credentialService.getCredential(Integer.parseInt(credentialId));
            rowChanged = credentialService.updateCredential(credential.getCredentialId(), url, encodedKey, encryptedPassword, credentialUsername);
        }
        if(rowChanged == 0) {
            result = "notsaved";
        }
        User user = userService.getUser(authentication.getName());
        model.addAttribute("credentials", credentialService.getCredentialsList(user.getUserId()));
        model.addAttribute("result", result);
        return "result";
    }

    @GetMapping(value = "/delete-credential/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, Model model) {
        String result = "success";
        int rowChanged = credentialService.deleteCredential(credentialId);
        if(rowChanged == 0) {
            result = "notsaved";
        }
        model.addAttribute("result", result);
        return "result";
    }
}
