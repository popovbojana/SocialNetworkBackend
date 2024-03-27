package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.NewPostDTO;
import com.internship.socialnetwork.dto.PostDTO;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.PostRepository;
import com.internship.socialnetwork.repository.UserRepository;
import com.internship.socialnetwork.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.internship.socialnetwork.dto.NewPostDTO.toPost;
import static com.internship.socialnetwork.dto.PostDTO.toPostDTO;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private static final String USER_NOT_FOUND_MESSAGE = "User with id %s doesn't exist!";

    private static final String POST_NOT_FOUND_MESSAGE = "Post with id %s doesn't exist!";

    @Override
    public PostDTO create(Long userId, NewPostDTO newPostDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));
        return toPostDTO(postRepository.save(toPost(user, newPostDTO)));
    }

    @Override
    public List<PostDTO> getAllForUser(Long userId) {
        return postRepository.findAllByPostedById(userId)
                .stream()
                .map(PostDTO::toPostDTO)
                .toList();
    }

    @Override
    public PostDTO get(Long postId) {
        return toPostDTO(postRepository.findById(postId).orElseThrow(() -> new NotFoundException(String.format(POST_NOT_FOUND_MESSAGE, postId))));
    }

    @Override
    public PostDTO update(Long postId, NewPostDTO updatedPost) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException(String.format(POST_NOT_FOUND_MESSAGE, postId)));
        post.setDescription(updatedPost.getDescription());
        post.setMedia(updatedPost.getMedia());
        return toPostDTO(postRepository.save(post));
    }

    @Override
    public void delete(Long postId) {
        postRepository.delete(postRepository.findById(postId).orElseThrow(() -> new NotFoundException(String.format(POST_NOT_FOUND_MESSAGE, postId))));
    }

}
