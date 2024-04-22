package com.internship.socialnetwork.controller;

import com.internship.socialnetwork.dto.CommentDTO;
import com.internship.socialnetwork.dto.NewCommentDTO;
import com.internship.socialnetwork.dto.PostDTO;
import com.internship.socialnetwork.dto.UpdatePostDTO;
import com.internship.socialnetwork.service.CommentService;
import com.internship.socialnetwork.service.FileDataService;
import com.internship.socialnetwork.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    private final CommentService commentService;

    private final FileDataService fileDataService;

    @PostMapping(value = "/{id}/comments")
    @PreAuthorize("@authServiceImpl.hasAccess(#newCommentDTO.userId)")
    public ResponseEntity<CommentDTO> create(@PathVariable Long id, @Valid @RequestBody NewCommentDTO newCommentDTO) {
        return new ResponseEntity<>(commentService.create(id, newCommentDTO), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDTO> get(@PathVariable Long id) {
        return new ResponseEntity<>(postService.get(id), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("@authServiceImpl.hasAccessForPost(#id)")
    public ResponseEntity<PostDTO> update(@PathVariable Long id, @Valid @RequestBody UpdatePostDTO updatedPost) {
        return new ResponseEntity<>(postService.update(id, updatedPost), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@authServiceImpl.hasAccessForPost(#id)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForPost(@PathVariable Long id) {
        return new ResponseEntity<>(postService.getAllCommentsForPost(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/files")
    public ResponseEntity<?> downloadFile(@PathVariable Long id) {
        return new ResponseEntity<>(fileDataService.downloadFile(id), HttpStatus.OK);
    }

}
