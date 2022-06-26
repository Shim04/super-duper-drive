package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> getCredentialsByUser(Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    Credential getCredential(Integer credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid)" +
            "VALUES (#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    @Delete("DELETE * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    void delete(Integer credentialId);

    @Update("UPDATE CREDENTIALS SET url = #{url}, key = #{key}, password = #{password}, username = #{username} WHERE credentialid = #{credentialId}")
    void update(Integer credentialId, String url, String key, String password, String username);
}