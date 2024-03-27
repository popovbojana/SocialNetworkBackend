package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.CommentDTO;
import com.internship.socialnetwork.dto.NewCommentDTO;
import com.internship.socialnetwork.dto.NewPostDTO;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Comment;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.CommentRepository;
import com.internship.socialnetwork.repository.PostRepository;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.internship.socialnetwork.dto.CommentDTO.toCommentDTO;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    // TODO: change to user service
    private final UserRepository userRepository;

    private static final String USER_NOT_FOUND_MESSAGE = "User with id %s doesn't exist!";

    private static final String POST_NOT_FOUND_MESSAGE = "Post with id %s doesn't exist!";

    @Override
    public CommentDTO create(Long id, NewCommentDTO newCommentDTO) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(POST_NOT_FOUND_MESSAGE, id)));
        User user = userRepository.findById(newCommentDTO.getUserId()).orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, newCommentDTO.getUserId())));
        Comment comment = Comment.builder()
                .post(post)
                .commentedBy(user)
                .commentedAt(LocalDateTime.now())
                .comment(newCommentDTO.getComment())
                .parentComment(commentRepository.findById(newCommentDTO.getParentCommentId()).orElse(null))
                .build();
        return toCommentDTO(commentRepository.save(comment));
    }

}
