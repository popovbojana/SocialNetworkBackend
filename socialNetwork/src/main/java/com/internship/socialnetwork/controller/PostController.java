package com.internship.socialnetwork.controller;

import com.internship.socialnetwork.dto.CommentDTO;
import com.internship.socialnetwork.dto.NewCommentDTO;
import com.internship.socialnetwork.dto.NewPostDTO;
import com.internship.socialnetwork.dto.PostDTO;
import com.internship.socialnetwork.service.CommentService;
import com.internship.socialnetwork.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    private final CommentService commentService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDTO> get(@PathVariable Long id) {
        return new ResponseEntity<>(postService.get(id), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PostDTO> update(@PathVariable Long id, @Valid @RequestBody NewPostDTO updatedPost) {
        return new ResponseEntity<>(postService.update(id, updatedPost), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/{id}/comments")
    public ResponseEntity<CommentDTO> create(@PathVariable Long id, @Valid @RequestBody NewCommentDTO newCommentDTO) {
        return new ResponseEntity<>(commentService.create(id, newCommentDTO), HttpStatus.CREATED);
    }

}
