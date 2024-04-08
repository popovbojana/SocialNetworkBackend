package com.internship.socialnetwork.service;

import com.internship.socialnetwork.config.ApplicationConfig;
import com.internship.socialnetwork.exception.FileUploadException;
import com.internship.socialnetwork.model.FileData;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.FileDataRepository;
import com.internship.socialnetwork.service.impl.FileDataServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileDataServiceImplTest {

    private static final String FILE_NAME = "test.webp";

    private static final String FILE_TYPE = "image/webp";

    private static final String FILE_PATH = "src/test/resources/";

    @Mock
    private FileDataRepository fileDataRepository;

    @Mock
    private ApplicationConfig applicationConfig;

    @InjectMocks
    private FileDataServiceImpl fileDataService;

    @Test
    void shouldReturnFileData_whenCreate_ifPathIsValid()  {
        // given
        MultipartFile multipartFile = createMultipartFile();
        User user = createUser();
        LocalDateTime createdAt = LocalDateTime.now();
        Post post = createPost(user, createdAt);
        FileData fileData = createFileData(post);

        when(applicationConfig.getFilesPath()).thenReturn(FILE_PATH);
        when(fileDataRepository.save(any())).thenReturn(fileData);

        // when
        FileData foundFileData = fileDataService.create(multipartFile, post);

        // then
        assertEquals(fileData, foundFileData);

        // and
        verify(applicationConfig).getFilesPath();
        verify(fileDataRepository).save(any());
    }

    @Test
    void shouldThrowFileUploadException_whenCreate_ifPathIsntValid() throws IOException {
        // given
        User user = createUser();
        LocalDateTime createdAt = LocalDateTime.now();
        Post post = createPost(user, createdAt);
        MultipartFile multipartFile = mock(MultipartFile.class);

        when(applicationConfig.getFilesPath()).thenReturn("invalid_path");
        doThrow(IOException.class).when(multipartFile).transferTo(any(File.class));

        // when
        FileUploadException exception = assertThrows(FileUploadException.class, () -> {
            fileDataService.create(multipartFile, post);
        });

        // then
        assertEquals("File upload failed!", exception.getMessage());

        // and
        verify(multipartFile).transferTo(any(File.class));
        verify(fileDataRepository, never()).save(any());
    }


    private MultipartFile createMultipartFile() {
        return new MockMultipartFile(
                "test",
                FILE_NAME,
                FILE_TYPE,
                "Test content".getBytes());
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .build();
    }

    private Post createPost(User user, LocalDateTime createdAt) {
        return Post.builder()
                .postedBy(user)
                .description("test_description")
                .postedAt(createdAt)
                .comments(new ArrayList<>())
                .build();
    }

    private FileData createFileData(Post post) {
        return FileData.builder()
                .post(post)
                .name(FILE_NAME)
                .type(FILE_TYPE)
                .filePath(FILE_PATH + FILE_NAME)
                .build();
    }

}
