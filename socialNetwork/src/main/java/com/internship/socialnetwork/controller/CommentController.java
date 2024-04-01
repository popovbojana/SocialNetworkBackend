package com.internship.socialnetwork.controller;

import com.internship.socialnetwork.dto.CommentDTO;
import com.internship.socialnetwork.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommentDTO> get(@PathVariable Long id) {
        return new ResponseEntity<>(commentService.get(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@authServiceImpl.hasAccessForComment(#id)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
