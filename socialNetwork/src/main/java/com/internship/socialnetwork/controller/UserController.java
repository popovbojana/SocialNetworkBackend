package com.internship.socialnetwork.controller;

import com.internship.socialnetwork.dto.FriendRequestDTO;
import com.internship.socialnetwork.dto.NewPostDTO;
import com.internship.socialnetwork.dto.PostDTO;
import com.internship.socialnetwork.dto.UpdateUserDTO;
import com.internship.socialnetwork.dto.UserDTO;
import com.internship.socialnetwork.model.ChatMessage;
import com.internship.socialnetwork.model.enumeration.FriendRequestStatus;
import com.internship.socialnetwork.service.ChatMessageService;
import com.internship.socialnetwork.service.FriendRequestService;
import com.internship.socialnetwork.service.PostService;
import com.internship.socialnetwork.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    private final FriendRequestService friendRequestService;

    private final PostService postService;

    private final ChatMessageService chatMessageService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> get(@PathVariable Long id) {
        return new ResponseEntity<>(userService.get(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> search(@RequestParam(required = false) String username,
                                                @RequestParam(required = false) String firstName,
                                                @RequestParam(required = false) String lastName) {
        return new ResponseEntity<>(userService.search(username, firstName, lastName), HttpStatus.OK);

    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("@authServiceImpl.hasAccess(#id)")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO updatedUser) {
        return new ResponseEntity<>(userService.update(id, updatedUser), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@authServiceImpl.hasAccess(#id)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}/friends")
    public ResponseEntity<List<UserDTO>> getAllFriends(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getAllFriendsById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/friend-requests")
    @PreAuthorize("@authServiceImpl.hasAccess(#id)")
    public ResponseEntity<List<FriendRequestDTO>> getAllByStatusForUser(@PathVariable Long id, @RequestParam(required = false) FriendRequestStatus status) {
        return new ResponseEntity<>(friendRequestService.getAllByStatusForUser(id, status), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/posts")
    @PreAuthorize("@authServiceImpl.hasAccess(#id)")
    public ResponseEntity<PostDTO> create(@PathVariable Long id, @Valid @ModelAttribute NewPostDTO newPostDTO) {
        return new ResponseEntity<>(postService.create(id, newPostDTO), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/posts")
    public ResponseEntity<List<PostDTO>> getAllForUser(@PathVariable Long id) {
        return new ResponseEntity<>(postService.getAllForUser(id), HttpStatus.OK);
    }

    @GetMapping("/connected")
    public ResponseEntity<List<UserDTO>> findConnectedUsers() {
        return new ResponseEntity<>(userService.findConnectedUsers(), HttpStatus.OK);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable Long senderId,
                                                              @PathVariable Long recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

}
