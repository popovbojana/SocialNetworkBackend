package com.internship.socialnetwork.service;

import java.util.Optional;


public interface ChatRoomService {

    Optional<String> getChatRoomId(Long senderId, Long recipientId, boolean createNewRoomIfNotExists);

}
