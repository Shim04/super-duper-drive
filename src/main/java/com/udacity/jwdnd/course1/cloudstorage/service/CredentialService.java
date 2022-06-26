package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final UserMapper userMapper;

    public CredentialService(CredentialMapper credentialMapper, UserMapper userMapper) {
        this.credentialMapper = credentialMapper;
        this.userMapper = userMapper;
    }

    public void addCredential(String url, String credentialUsername, String key, String password, String username) {
        Integer userId = userMapper.getUser(username).getUserId();
        Credential credential = new Credential(null, url, credentialUsername, key, password, userId);
        credentialMapper.insert(credential);
    }

    public List<Credential> getCredentialsList(Integer userId) {
        return credentialMapper.getCredentialsByUser(userId);
    }

    public Credential getCredential(Integer credentialId) {
        return credentialMapper.getCredential(credentialId);
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.delete(credentialId);
    }

    public void updateCredential(Integer credentialId, String url, String key, String password, String username) {
        credentialMapper.update(credentialId, url, key, password, username);
    }
}
