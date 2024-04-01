package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.CommentDTO;
import com.internship.socialnetwork.dto.NewCommentDTO;
import com.internship.socialnetwork.model.Comment;

public interface CommentService {

    Comment findById(Long id);

    CommentDTO create(Long id, NewCommentDTO newCommentDTO);

    CommentDTO get(Long id);

    void delete(Long id);

}
