package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.CommentDTO;
import com.internship.socialnetwork.dto.NewCommentDTO;
import com.internship.socialnetwork.dto.NewPostDTO;

public interface CommentService {

    CommentDTO create(Long id, NewCommentDTO newCommentDTO);

}
