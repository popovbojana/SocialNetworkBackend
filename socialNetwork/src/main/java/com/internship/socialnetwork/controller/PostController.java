package com.internship.socialnetwork.controller;

import com.internship.socialnetwork.dto.NewPostDTO;
import com.internship.socialnetwork.dto.PostDTO;
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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class PostController {

    private final PostService postService;

    @PostMapping(value = "users/{userId}/posts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDTO> create(@PathVariable Long userId, @Valid @RequestBody NewPostDTO newPostDTO) {
        return new ResponseEntity<>(postService.create(userId, newPostDTO), HttpStatus.CREATED);
    }

    @GetMapping(value = "users/{userId}/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PostDTO>> getAllForUser(@PathVariable Long userId) {
        return new ResponseEntity<>(postService.getAllForUser(userId), HttpStatus.OK);
    }

    @GetMapping(value = "posts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDTO> get(@PathVariable Long id) {
        return new ResponseEntity<>(postService.get(id), HttpStatus.OK);
    }

    @PutMapping(value = "posts/{postId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDTO> update(@PathVariable Long postId, @Valid @RequestBody NewPostDTO updatedPost) {
        return new ResponseEntity<>(postService.update(postId, updatedPost), HttpStatus.OK);
    }

    @DeleteMapping(value = "posts/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.delete(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
