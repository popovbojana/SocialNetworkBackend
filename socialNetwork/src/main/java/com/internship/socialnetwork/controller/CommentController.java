package com.internship.socialnetwork.controller;

import com.internship.socialnetwork.dto.CommentDTO;
import com.internship.socialnetwork.dto.NewCommentDTO;
import com.internship.socialnetwork.dto.NewPostDTO;
import com.internship.socialnetwork.dto.PostDTO;
import com.internship.socialnetwork.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class CommentController {

    private final CommentService commentService;

    @PostMapping(value = "posts/{id}/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDTO> create(@PathVariable Long id, @Valid @RequestBody NewCommentDTO newCommentDTO) {
        return new ResponseEntity<>(commentService.create(id, newCommentDTO), HttpStatus.CREATED);
    }

}
