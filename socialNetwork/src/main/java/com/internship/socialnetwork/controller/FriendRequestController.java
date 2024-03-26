package com.internship.socialnetwork.controller;

import com.internship.socialnetwork.dto.FriendRequestDTO;
import com.internship.socialnetwork.model.enumeration.FriendRequestStatus;
import com.internship.socialnetwork.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @PostMapping(value = "users/{userId}/friend-requests/{otherUserId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FriendRequestDTO> create(@PathVariable Long userId, @PathVariable Long otherUserId) {
        return new ResponseEntity<>(friendRequestService.create(userId, otherUserId), HttpStatus.CREATED);
    }

    @GetMapping(value = "users/{userId}/{otherUserId}/friend-requests", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FriendRequestDTO> get(@PathVariable Long userId, @PathVariable Long otherUserId) {
        return new ResponseEntity<>(friendRequestService.get(userId, otherUserId), HttpStatus.OK);
    }

    @GetMapping(value = "users/{id}/friend-requests", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FriendRequestDTO>> getAllForUser(@PathVariable Long id) {
        return new ResponseEntity<>(friendRequestService.getAllForUser(id), HttpStatus.OK);
    }

    @GetMapping(value = "users/{id}/friend-requests/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FriendRequestDTO>> getAllByStatusForUser(@PathVariable Long id, @RequestParam FriendRequestStatus status) {
        return new ResponseEntity<>(friendRequestService.getAllByStatusForUser(id, status), HttpStatus.OK);
    }

    @PutMapping(value = "friend-requests/{fromUserId}/{toUserId}/response", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FriendRequestDTO> respondToPendingRequest(@PathVariable Long fromUserId, @PathVariable Long toUserId, @RequestParam FriendRequestStatus status) {
        return new ResponseEntity<>(friendRequestService.respondToPendingRequest(fromUserId, toUserId, status), HttpStatus.OK);
    }

    @DeleteMapping(value = "friend-requests/{fromUserId}/{toUserId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Long fromUserId, @PathVariable Long toUserId) {
        friendRequestService.delete(fromUserId, toUserId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
