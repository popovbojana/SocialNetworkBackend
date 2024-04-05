package com.internship.socialnetwork.service;

import com.internship.socialnetwork.model.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    ChatMessage save(ChatMessage chatMessage);

    List<ChatMessage> findChatMessages(Long senderId, Long recipientId);

}
