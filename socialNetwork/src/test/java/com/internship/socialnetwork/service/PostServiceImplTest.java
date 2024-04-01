package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.CommentDTO;
import com.internship.socialnetwork.dto.NewPostDTO;
import com.internship.socialnetwork.dto.PostDTO;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Comment;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.PostRepository;
import com.internship.socialnetwork.service.impl.PostServiceImpl;
import com.internship.socialnetwork.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.internship.socialnetwork.dto.CommentDTO.toCommentDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    private static final String TEST_DESCRIPTION = "Test description";

    private static final String TEST_MEDIA = "Test media";

    private static final Long USER_ID = 1L;

    private static final Long POST_ID = 1L;

    private static final String NOT_FOUND_USER_MESSAGE = "User with id 1 doesn't exist!";

    private static final String NOT_FOUND_POST_MESSAGE = "Post with id 1 doesn't exist!";

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    void shouldReturnPost_whenCreate_ifUserExists() {
        // given
        LocalDateTime createdAt = LocalDateTime.now();

        NewPostDTO newPostDTO = NewPostDTO.builder()
                .description(TEST_DESCRIPTION)
                .media(TEST_MEDIA)
                .build();

        User user = createUser();

        Post post = createPost(user, createdAt);

        PostDTO postDTO = createPostDTO(TEST_DESCRIPTION, TEST_MEDIA, createdAt);

        when(userService.findById(any())).thenReturn(user);
        when(postRepository.save(any())).thenReturn(post);

        // when
        PostDTO savedPost = postService.create(USER_ID, newPostDTO);

        // then
        assertEquals(postDTO, savedPost);

        // and
        verify(userService).findById(any());
        verify(postRepository).save(any());
    }

    @Test
    void shouldThrowNotFoundException_whenCreate_ifUserDoesntExist() {
        // given
        NewPostDTO newPostDTO = NewPostDTO.builder()
                .description(TEST_DESCRIPTION)
                .media(TEST_MEDIA)
                .build();

        when(userService.findById(any())).thenThrow(new NotFoundException(NOT_FOUND_USER_MESSAGE));

        // when
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            postService.create(USER_ID, newPostDTO);
        });

        // then
        assertEquals(NOT_FOUND_USER_MESSAGE, exception.getMessage());

        // and
        verify(userService).findById(any());
        verify(postRepository, never()).save(any());
    }

    @Test
    void shouldReturnAllPosts_whenGetAllForUser_ifUserExists() {
        // given
        LocalDateTime createdAt = LocalDateTime.now();

        User user = createUser();

        Post post = createPost(user, createdAt);

        List<Post> posts = List.of(post);

        PostDTO postDTO = createPostDTO(TEST_DESCRIPTION, TEST_MEDIA, createdAt);

        List<PostDTO> expectedPosts = new ArrayList<>();
        expectedPosts.add(postDTO);

        when(postRepository.findAllByPostedById(any())).thenReturn(posts);

        // when
        List<PostDTO> foundPosts = postService.getAllForUser(USER_ID);

        // then
        assertEquals(expectedPosts, foundPosts);

        // and
        verify(postRepository).findAllByPostedById(any());
    }

    @Test
    void shouldReturnPost_whenGet_ifPostExists() {
        // given
        User user = User.builder().id(USER_ID).build();
        LocalDateTime postedAt = LocalDateTime.now();
        Post post = createPost(user, postedAt);
        PostDTO postDTO = createPostDTO(TEST_DESCRIPTION, TEST_MEDIA, postedAt);

        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        // when
        PostDTO foundPost = postService.get(POST_ID);

        // then
        assertEquals(postDTO, foundPost);

        // and
        verify(postRepository).findById(any());
    }

    @Test
    void shouldThrowNotFoundException_whenGet_ifPostDoesntExist() {
        // given
        when(postRepository.findById(any())).thenReturn(Optional.empty());

        // when
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            postService.get(POST_ID);
        });

        // then
        assertEquals(NOT_FOUND_POST_MESSAGE, exception.getMessage());

        // and
        verify(postRepository).findById(any());
    }

    @Test
    void shouldReturnPost_whenUpdate_ifPostExists() {
        // given
        User user = User.builder().id(USER_ID).build();
        String newDescription = "New description";
        String newMedia = "New media";
        LocalDateTime postedAt = LocalDateTime.now();

        Post post = createPost(user, postedAt);
        PostDTO postDTO = createPostDTO(newDescription, newMedia, postedAt);

        NewPostDTO updatePostDTO = NewPostDTO.builder()
                .description(newDescription)
                .media(newMedia)
                .build();

        when(postRepository.findById(any())).thenReturn(Optional.of(post));
        when(postRepository.save(any())).thenReturn(post);

        // when
        PostDTO foundPost = postService.update(POST_ID, updatePostDTO);

        // then
        assertEquals(postDTO, foundPost);

        // and
        verify(postRepository).findById(any());
        verify(postRepository).save(any());
    }

    @Test
    void shouldThrowNotFoundException_whenUpdate_ifPostDoesntExist() {
        // given
        when(postRepository.findById(any())).thenReturn(Optional.empty());

        // when
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            postService.update(POST_ID, new NewPostDTO());
        });

        // then
        assertEquals(NOT_FOUND_POST_MESSAGE, exception.getMessage());

        // and
        verify(postRepository).findById(any());
        verify(postRepository, never()).save(any());
    }

    @Test
    void shouldDeletePost_whenDelete_ifPostExists() {
        // given
        User user = createUser();
        Post post = createPostByIdAndUser(user);

        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        // when
        postService.delete(POST_ID);

        // then
        verify(postRepository).findById(any());
        verify(postRepository).delete(any());
    }

    @Test
    void shouldThrowNotFoundException_whenDelete_ifPostDoesntExist() {
        // given
        when(postRepository.findById(any())).thenReturn(Optional.empty());

        // when
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            postService.delete(POST_ID);
        });

        // then
        assertEquals(NOT_FOUND_POST_MESSAGE, exception.getMessage());

        // and
        verify(postRepository).findById(any());
        verify(postRepository, never()).delete(any());
    }

    @Test
    void shouldReturnComments_whenGetAllCommentsForPost_ifPostExists() {
        // given
        User user = createUser();
        Post post = createPostByIdAndUser(user);
        Comment comment = Comment.builder()
                        .post(post)
                        .commentedBy(user)
                        .build();
        post.setComments(List.of(comment));

        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        // when
        List<CommentDTO> foundComments = postService.getAllCommentsForPost(POST_ID);

        // then
        assertEquals(List.of(toCommentDTO(comment)), foundComments);

        // and
        verify(postRepository).findById(any());
    }

    private Post createPost(User user, LocalDateTime createdAt) {
        return Post.builder()
                .postedBy(user)
                .description(TEST_DESCRIPTION)
                .media(TEST_MEDIA)
                .postedAt(createdAt)
                .comments(new ArrayList<>())
                .build();
    }

    private Post createPostByIdAndUser(User user) {
        return Post.builder()
                .id(POST_ID)
                .postedBy(user)
                .build();
    }

    private PostDTO createPostDTO(String description, String media, LocalDateTime createdAt) {
        return PostDTO.builder()
                .userId(USER_ID)
                .description(description)
                .media(media)
                .postedAt(createdAt)
                .comments(new ArrayList<>())
                .build();
    }

    private User createUser() {
        return User.builder()
                .id(USER_ID)
                .build();
    }

}
