package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NoteService {
    private final NoteMapper noteMapper;
    private final UserMapper userMapper;

    public NoteService(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    public int addNote(String noteTitle, String noteDescription, String username) {
        Integer userId = userMapper.getUser(username).getUserId();
        Note note = new Note(null, noteTitle, noteDescription, userId);
        return noteMapper.insert(note);
    }

    public List<Note> getNotesList(Integer userId) {
        return noteMapper.getNotesByUser(userId);
    }

    public Note getNote(Integer noteId) {
        return noteMapper.getNote(noteId);
    }

    public int deleteNote(Integer noteId) {
        return noteMapper.delete(noteId);
    }

    public int updateNote(Integer noteId, String noteTitle, String noteDescription) {
        return noteMapper.update(noteId, noteTitle, noteDescription);
    }
}
