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
@RequestMapping("/api/v1/friend-requests")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @PostMapping
    public ResponseEntity<FriendRequestDTO> create(@RequestParam Long fromUserId, @RequestParam Long toUserId) {
        return new ResponseEntity<>(friendRequestService.create(fromUserId, toUserId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<FriendRequestDTO> get(@RequestParam Long fromUserId, @RequestParam Long toUserId) {
        return new ResponseEntity<>(friendRequestService.get(fromUserId, toUserId), HttpStatus.OK);
    }

    @PutMapping(value = "/responses")
    public ResponseEntity<FriendRequestDTO> respondToPendingRequest(@RequestParam Long fromUserId, @RequestParam Long toUserId, @RequestParam FriendRequestStatus status) {
        return new ResponseEntity<>(friendRequestService.respondToPendingRequest(fromUserId, toUserId, status), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long fromUserId, @RequestParam Long toUserId) {
        friendRequestService.delete(fromUserId, toUserId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
