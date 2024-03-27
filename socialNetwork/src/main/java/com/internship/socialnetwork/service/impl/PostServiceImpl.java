package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.NewPostDTO;
import com.internship.socialnetwork.dto.PostDTO;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.model.User;
import com.internship.socialnetwork.repository.PostRepository;
import com.internship.socialnetwork.service.PostService;
import com.internship.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.internship.socialnetwork.dto.NewPostDTO.toPost;
import static com.internship.socialnetwork.dto.PostDTO.toPostDTO;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final UserService userService;

    private static final String POST_NOT_FOUND_MESSAGE = "Post with id %s doesn't exist!";

    @Override
    public PostDTO create(Long userId, NewPostDTO newPostDTO) {
        User user = userService.findById(userId);
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
    public PostDTO get(Long id) {
        return toPostDTO(findById(id));
    }

    @Override
    public PostDTO update(Long id, NewPostDTO updatedPost) {
        Post post = findById(id);
        post.setDescription(updatedPost.getDescription());
        post.setMedia(updatedPost.getMedia());
        return toPostDTO(postRepository.save(post));
    }

    @Override
    public void delete(Long id) {
        postRepository.delete(findById(id));
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(POST_NOT_FOUND_MESSAGE, id)));
    }

}
