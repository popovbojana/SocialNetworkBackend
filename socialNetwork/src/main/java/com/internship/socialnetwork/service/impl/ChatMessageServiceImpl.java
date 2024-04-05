package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.exception.NotFoundException;
import com.internship.socialnetwork.model.ChatMessage;
import com.internship.socialnetwork.repository.ChatMessageRepository;
import com.internship.socialnetwork.service.ChatMessageService;
import com.internship.socialnetwork.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomService chatRoomService;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        Long senderId = chatMessage.getSenderId();
        Long recipientId = chatMessage.getRecipientId();
        var chatId = chatRoomService
                .getChatRoomId(senderId, recipientId, true)
                .orElseThrow(() -> new NotFoundException(String.format("Chat with sender id %d and recipient id %d not found.", senderId, recipientId)));
        chatMessage.setChatId(chatId);
        return chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> findChatMessages(Long senderId, Long recipientId) {
        return chatRoomService.getChatRoomId(senderId, recipientId, false)
                .map(chatMessageRepository::findByChatId)
                .orElse(List.of());
    }

}
