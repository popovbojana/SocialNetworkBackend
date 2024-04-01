package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.CommentDTO;
import com.internship.socialnetwork.dto.NewCommentDTO;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Comment;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.CommentRepository;
import com.internship.socialnetwork.service.CommentService;
import com.internship.socialnetwork.service.PostService;
import com.internship.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.internship.socialnetwork.dto.CommentDTO.toCommentDTO;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final String COMMENT_NOT_FOUND_MESSAGE = "Comment with id %s doesn't exist!";

    private final CommentRepository commentRepository;

    private final PostService postService;

    private final UserService userService;

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(COMMENT_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    public CommentDTO create(Long id, NewCommentDTO newCommentDTO) {
        Post post = postService.findById(id);
        User user = userService.findById(newCommentDTO.getUserId());
        Comment parentComment = Optional.ofNullable(newCommentDTO.getParentCommentId())
                .map(this::findById)
                .orElse(null);
        return toCommentDTO(save(post, user, newCommentDTO.getComment(), parentComment));
    }

    @Override
    public CommentDTO get(Long id) {
        return toCommentDTO(findById(id));
    }

    @Override
    public void delete(Long id) {
        commentRepository.delete(findById(id));
    }

    private Comment save(Post post, User user, String comment, Comment parentComment) {
        Comment newComment = Comment.builder()
                .post(post)
                .commentedBy(user)
                .commentedAt(LocalDateTime.now())
                .comment(comment)
                .parentComment(parentComment)
                .build();
        if (parentComment != null) {
            parentComment.getReplies().add(newComment);
            commentRepository.save(parentComment);
        } else {
            commentRepository.save(newComment);
        }
        return newComment;
    }

}
