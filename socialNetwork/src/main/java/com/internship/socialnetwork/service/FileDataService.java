package com.internship.socialnetwork.service;

import com.internship.socialnetwork.model.FileData;
import com.internship.socialnetwork.model.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileDataService {

    FileData create(MultipartFile file, Post post);

    byte[] downloadFile(Long id);

}
