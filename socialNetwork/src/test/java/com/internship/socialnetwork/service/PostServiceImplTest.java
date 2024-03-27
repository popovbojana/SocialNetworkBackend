package com.internship.socialnetwork.service;

import com.internship.socialnetwork.dto.NewPostDTO;
import com.internship.socialnetwork.dto.PostDTO;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.PostRepository;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.impl.PostServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private final String TEST_DESCRIPTION = "Test description";

    private final String TEST_MEDIA = "Test media";

    private final Long USER_ID = 1L;

    private final Long POST_ID = 1L;

    private final String NOT_FOUND_USER_MESSAGE = "User with id 1 doesn't exist!";
    
    private final String NOT_FOUND_POST_MESSAGE = "Post with id 1 doesn't exist!";

    @Test
    void shouldCreatePost() {
        // given
        LocalDateTime createdAt = LocalDateTime.now();

        NewPostDTO newPostDTO = NewPostDTO.builder()
                .description(TEST_DESCRIPTION)
                .media(TEST_MEDIA)
                .build();

        User user = createUser(USER_ID);

        Post post = createPost(user, TEST_DESCRIPTION, TEST_MEDIA, createdAt);

        PostDTO postDTO = createPostDTO(USER_ID, TEST_DESCRIPTION, TEST_MEDIA, createdAt);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(postRepository.save(any())).thenReturn(post);

        // when
        PostDTO savedPost = postService.create(USER_ID, newPostDTO);

        // then
        assertEquals(postDTO, savedPost);

        // and
        verify(userRepository).findById(any());
        verify(postRepository).save(any());
    }

    @Test
    void shouldThrowNotFoundException_whenCreatePost_ifUserDoesntExist() {
        // given
        NewPostDTO newPostDTO = NewPostDTO.builder()
                .description(TEST_DESCRIPTION)
                .media(TEST_MEDIA)
                .build();

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // when
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            postService.create(USER_ID, newPostDTO);
        });

        // then
        assertEquals(NOT_FOUND_USER_MESSAGE, exception.getMessage());

        // and
        verify(userRepository).findById(any());
        verify(postRepository, never()).save(any());
    }

    @Test
    void shouldGetAllPostsForUser() {
        // given
        LocalDateTime createdAt = LocalDateTime.now();

        User user = createUser(USER_ID);

        Post post = createPost(user, TEST_DESCRIPTION, TEST_MEDIA, createdAt);

        List<Post> posts = List.of(post);

        PostDTO postDTO = createPostDTO(USER_ID, TEST_DESCRIPTION, TEST_MEDIA, createdAt);

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
    void shouldGetPost() {
        // given
        User user = User.builder().id(USER_ID).build();
        LocalDateTime postedAt = LocalDateTime.now();
        Post post = createPost(user, TEST_DESCRIPTION, TEST_MEDIA, postedAt);
        PostDTO postDTO = createPostDTO(USER_ID, TEST_DESCRIPTION, TEST_MEDIA, postedAt);

        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        // when
        PostDTO foundPost = postService.get(POST_ID);

        // then
        assertEquals(postDTO, foundPost);

        // and
        verify(postRepository).findById(any());
    }

    @Test
    void shouldThrowNotFoundException_whenGetPost_ifPostDoesntExist() {
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
    void shouldUpdatePost() {
        // given
        User user = User.builder().id(USER_ID).build();
        String newDescription = "New description";
        String newMedia = "New media";
        LocalDateTime postedAt = LocalDateTime.now();

        Post post = createPost(user, TEST_DESCRIPTION, TEST_MEDIA, postedAt);
        PostDTO postDTO = createPostDTO(USER_ID, newDescription, newMedia, postedAt);

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
    void shouldThrowNotFoundException_whenUpdatePost_ifPostDoesntExist() {
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
    void shouldDeletePost() {
        // given
        User user = createUser(USER_ID);
        Post post = createPostByIdAndUser(POST_ID, user);

        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        // when
        postService.delete(POST_ID);

        // then

        // and
        verify(postRepository).findById(any());
        verify(postRepository).delete(any());
    }

    @Test
    void shouldThrowNotFoundException_whenDeletePost_ifPostDoesntExist() {
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

    private Post createPost(User user, String description, String media, LocalDateTime createdAt) {
        return Post.builder()
                .postedBy(user)
                .description(description)
                .media(media)
                .postedAt(createdAt)
                .comments(new ArrayList<>())
                .build();
    }

    private Post createPostByIdAndUser(Long postId, User user) {
        return Post.builder()
                .id(postId)
                .postedBy(user)
                .build();
    }

    private PostDTO createPostDTO(Long userId, String description, String media, LocalDateTime createdAt) {
        return PostDTO.builder()
                .userId(userId)
                .description(description)
                .media(media)
                .postedAt(createdAt)
                .comments(new ArrayList<>())
                .build();
    }

    private User createUser(Long id) {
        return User.builder()
                .id(id)
                .build();
    }

}
