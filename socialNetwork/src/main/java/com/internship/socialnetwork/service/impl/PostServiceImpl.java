package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.dto.CommentDTO;
import com.internship.socialnetwork.dto.NewPostDTO;
import com.internship.socialnetwork.dto.PostDTO;
import com.internship.socialnetwork.dto.UpdatePostDTO;
import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.FileData;
import com.internship.socialnetwork.model.Post;
import com.internship.socialnetwork.repository.PostRepository;
import com.internship.socialnetwork.service.FileDataService;
import com.internship.socialnetwork.service.PostService;
import com.internship.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.internship.socialnetwork.dto.NewPostDTO.toPost;
import static com.internship.socialnetwork.dto.PostDTO.toPostDTO;
import static com.internship.socialnetwork.dto.PostDTO.toPostDTOWithFile;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final String POST_NOT_FOUND_MESSAGE = "Post with id %s doesn't exist!";

    private final PostRepository postRepository;

    private final UserService userService;

    private final FileDataService fileDataService;

    @Override
    public PostDTO create(Long userId, NewPostDTO newPostDTO) {
        Post post = postRepository.save(toPost(userService.findById(userId), newPostDTO));
        MultipartFile file = newPostDTO.getFile();
        if (file != null) {
            FileData fileData = fileDataService.create(file, post);
            return toPostDTOWithFile(post, fileData);
        }
        return toPostDTO(post);
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
    public PostDTO update(Long id, UpdatePostDTO updatedPost) {
        Post post = findById(id);
        post.setDescription(updatedPost.getDescription());
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

    @Override
    public List<CommentDTO> getAllCommentsForPost(Long id) {
        return findById(id).getComments().stream()
                .map(CommentDTO::toCommentDTO)
                .toList();
    }

}
