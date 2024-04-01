package com.internship.socialnetwork.controller;

import com.internship.socialnetwork.dto.FriendRequestDTO;
import com.internship.socialnetwork.dto.UserDTO;
import com.internship.socialnetwork.dto.NewUserDTO;
import com.internship.socialnetwork.model.enumeration.FriendRequestStatus;
import com.internship.socialnetwork.service.FriendRequestService;
import com.internship.socialnetwork.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> register(@Valid @RequestBody NewUserDTO newUser) {
        return new ResponseEntity<>(userService.create(newUser), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}/friends", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllFriends(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getAllFriendsById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/friend-requests", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FriendRequestDTO>> getAllByStatusForUser(@PathVariable Long id, @RequestParam(required = false) FriendRequestStatus status) {
        return new ResponseEntity<>(friendRequestService.getAllByStatusForUser(id, status), HttpStatus.OK);
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> get(@PathVariable Long id) {
        return new ResponseEntity<>(userService.get(id), HttpStatus.OK);
    }

    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody NewUserDTO updatedUser) {
        return new ResponseEntity<>(userService.update(id, updatedUser), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> search(@RequestParam(required = false) String username,
                                          @RequestParam(required = false) String firstName,
                                          @RequestParam(required = false) String lastName) {
        return new ResponseEntity<>(userService.search(username, firstName, lastName), HttpStatus.OK);
    }

}
