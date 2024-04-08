package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.config.ApplicationConfig;
import com.internship.socialnetwork.exception.FileUploadException;
import com.internship.socialnetwork.model.FileData;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.repository.FileDataRepository;
import com.internship.socialnetwork.service.FileDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileDataServiceImpl implements FileDataService {

    private final FileDataRepository fileDataRepository;

    private final ApplicationConfig appConfig;

    @Override
    public FileData create(MultipartFile file, Post post) {
        try {
            String filePath = appConfig.getFilesPath() + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            return fileDataRepository.save(buildFileDate(file, filePath, post));
        } catch (IOException ex) {
            throw new FileUploadException("File upload failed!");
        }
    }

    private FileData buildFileDate(MultipartFile file, String filePath, Post post) {
        return FileData.builder()
                .post(post)
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath)
                .build();
    }

}
