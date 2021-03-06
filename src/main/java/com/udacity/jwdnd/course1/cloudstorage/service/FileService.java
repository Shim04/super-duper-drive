package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    public int addFile(MultipartFile multipartFile, String username) throws IOException {
        InputStream fis = multipartFile.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;
        while((nRead = fis.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        byte[] fileData = buffer.toByteArray();

        String fileName = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        String fileSize = String.valueOf(multipartFile.getSize());
        Integer userId = userMapper.getUser(username).getUserId();
        File file = new File(null, fileName, contentType, fileSize, userId, fileData);
        return fileMapper.insert(file);
    }

    public List<String> getFilesList(Integer userId) {
        return fileMapper.getFilesByUser(userId);
    }

    public File getFile(String fileName, Integer userId) {
        return fileMapper.getFile(fileName, userId);
    }

    public boolean isFilenameAvailable(String fileName, Integer userId) {
        if(getFile(fileName, userId) != null) {
            return false;
        } else {
            return true;
        }
    }

    public int deleteFile(String fileName) {
        return fileMapper.delete(fileName);
    }
}
