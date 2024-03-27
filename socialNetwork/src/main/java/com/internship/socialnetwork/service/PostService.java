package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.NewPostDTO;
import com.internship.socialnetwork.dto.PostDTO;

import java.util.List;

public interface PostService {

    PostDTO create(Long userId, NewPostDTO newPostDTO);

    List<PostDTO> getAllForUser(Long userId);

    PostDTO get(Long postId);

    PostDTO update(Long postId, NewPostDTO updatedPost);

    void delete(Long postId);

}
