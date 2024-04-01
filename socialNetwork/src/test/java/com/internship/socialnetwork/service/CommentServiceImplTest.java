package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.CommentDTO;
import com.internship.socialnetwork.dto.NewCommentDTO;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Comment;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.CommentRepository;
import com.internship.socialnetwork.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    private static final Long USER_ID = 1L;

    private static final Long COMMENT_ID = 1L;

    private static final Long POST_ID = 1L;

    private static final String COMMENT_NOT_FOUND_MESSAGE = "Comment with id 1 doesn't exist!";

    private static final String COMMENT = "Test comment";

    private static final String POST_NOT_FOUND_MESSAGE = "Post with id 1 doesn't exist!";

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void shouldReturnComment_whenFindById_ifCommentExists() {
        // given
        Comment comment = new Comment();

        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        // when
        Comment foundComment = commentService.findById(COMMENT_ID);

        // then
        assertEquals(comment, foundComment);

        // and
        verify(commentRepository).findById(any());
    }

    @Test
    void shouldThrowNotFoundException_whenFindById_ifCommentDoesntExist() {
        // given
        when(commentRepository.findById(any())).thenReturn(Optional.empty());

        // when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            commentService.findById(COMMENT_ID);
        });

        // then
        assertEquals(COMMENT_NOT_FOUND_MESSAGE, exception.getMessage());

        // and
        verify(commentRepository).findById(any());
    }

    @Test
    void shouldSaveComment_whenCreateComment_ifPostExists() {
        // given
        NewCommentDTO newCommentDTO = createNewCommentDTO();
        Post post = createPost();
        User user = createUser();
        Comment comment = createComment(post, user);
        CommentDTO commentDTO = createCommentDTO();

        when(postService.findById(any())).thenReturn(post);
        when(userService.findById(any())).thenReturn(user);
        when(commentRepository.save(any())).thenReturn(comment);

        // when
        CommentDTO savedComment = commentService.create(POST_ID, newCommentDTO);

        // then
        assertEquals(commentDTO.getPostId(), savedComment.getPostId());
        assertEquals(commentDTO.getUserId(), savedComment.getUserId());
        assertEquals(commentDTO.getComment(), savedComment.getComment());


        // and
        verify(postService).findById(any());
        verify(userService).findById(any());
        verify(commentRepository).save(any());
    }

    @Test
    void shouldSaveComment_whenCreateComment_ifParentCommentExists() {
        // given
        NewCommentDTO newCommentDTO = createNewCommentDTO();
        newCommentDTO.setParentCommentId(COMMENT_ID);
        Post post = createPost();
        User user = createUser();
        Comment parentComment = createComment(post, user);
        parentComment.setReplies(new ArrayList<>());
        Comment comment = createComment(post, user);
        comment.setParentComment(parentComment);
        CommentDTO commentDTO = createCommentDTO();

        when(postService.findById(any())).thenReturn(post);
        when(userService.findById(any())).thenReturn(user);
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(parentComment));
        when(commentRepository.save(any())).thenReturn(comment);

        // when
        CommentDTO savedComment = commentService.create(POST_ID, newCommentDTO);

        // then
        assertEquals(commentDTO.getPostId(), savedComment.getPostId());
        assertEquals(commentDTO.getUserId(), savedComment.getUserId());
        assertEquals(commentDTO.getComment(), savedComment.getComment());


        // and
        verify(postService).findById(any());
        verify(userService).findById(any());
        verify(commentRepository).findById(any());
        verify(commentRepository).save(any());
    }

    @Test
    void shouldThrowNotFoundException_whenCreateComment_ifPostDoesntExist(){
        // given
        when(postService.findById(any())).thenThrow(new NotFoundException(POST_NOT_FOUND_MESSAGE));

        // when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            commentService.create(COMMENT_ID, new NewCommentDTO());
        });

        // then
        assertEquals(POST_NOT_FOUND_MESSAGE, exception.getMessage());

        // and
        verify(postService).findById(any());
        verify(userService, never()).findById(any());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void shouldReturnComment_whenGetComment_ifCommentExists() {
        // given
        Post post = createPost();
        User user = createUser();
        Comment comment = createComment(post, user);
        CommentDTO commentDTO = createCommentDTO();

        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        // when
        CommentDTO foundComment = commentService.get(COMMENT_ID);

        // then
        assertEquals(commentDTO, foundComment);

        // and
        verify(commentRepository).findById(any());
    }

    @Test
    void shouldDeleteComment_whenDeleteComment_ifCommentExists() {
        // given
        Post post = createPost();
        User user = createUser();
        Comment comment = createComment(post, user);

        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        // when
        commentService.delete(COMMENT_ID);

        // then
        verify(commentRepository).findById(any());
    }

    private User createUser() {
        return User.builder()
                .id(USER_ID)
                .build();
    }

    private Post createPost() {
        return Post.builder()
                .id(POST_ID)
                .build();
    }

    private Comment createComment(Post post, User user) {
        return Comment.builder()
                .post(post)
                .commentedBy(user)
                .comment(COMMENT)
                .build();
    }

    private NewCommentDTO createNewCommentDTO() {
        return NewCommentDTO.builder()
                .userId(USER_ID)
                .comment(COMMENT)
                .build();
    }

    private CommentDTO createCommentDTO() {
        return CommentDTO.builder()
                .postId(POST_ID)
                .userId(USER_ID)
                .comment(COMMENT)
                .build();
    }

}
