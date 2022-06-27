package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping(value = "/add-note")
    public String addNote(
            Authentication authentication, FileForm fileForm, NoteForm noteForm,
            CredentialForm credentialForm, Model model) {
        String result = "success";
        String username = authentication.getName();
        String noteTitle = noteForm.getNoteTitle();
        String noteId = noteForm.getNoteId();
        String noteDescription = noteForm.getNoteDescription();
        int rowChanged;
        if(noteId.isEmpty()) {
            rowChanged = noteService.addNote(noteTitle, noteDescription, username);
        } else {
            Note note = noteService.getNote(Integer.parseInt(noteId));
            rowChanged = noteService.updateNote(note.getNoteId(), noteTitle, noteDescription);
        }
        if(rowChanged == 0) {
            result = "notsaved";
        }
        model.addAttribute("result", result);
        return "result";
    }

    @GetMapping(value = "/delete-note/{noteId}")
    public String deleteNote(
            Authentication authentication, @PathVariable Integer noteId,
            FileForm fileForm, NoteForm noteForm, CredentialForm credentialForm,
            Model model) {
        String result = "success";
        int rowChanged = noteService.deleteNote(noteId);
        if(rowChanged == 0) {
            result = "notsaved";
        }
        model.addAttribute("result", result);
        return "result";
    }
}
