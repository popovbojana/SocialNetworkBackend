package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.CommentDTO;
import com.internship.socialnetwork.dto.NewPostDTO;
import com.internship.socialnetwork.dto.PostDTO;
import com.internship.socialnetwork.model.Post;

import java.util.List;

public interface PostService {

    PostDTO create(Long userId, NewPostDTO newPostDTO);

    List<PostDTO> getAllForUser(Long userId);

    PostDTO get(Long id);

    PostDTO update(Long id, NewPostDTO updatedPost);

    void delete(Long id);

    Post findById(Long id);

    List<CommentDTO> getAllCommentsForPost(Long id);

}
