package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.model.ChatRoom;
import com.internship.socialnetwork.repository.ChatRoomRepository;
import com.internship.socialnetwork.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Optional<String> getChatRoomId(Long senderId, Long recipientId, boolean createNewRoomIfNotExists) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(createNewRoomIfNotExists) {
                        return Optional.of(createChatId(senderId, recipientId));
                    }
                    return Optional.empty();
                });
    }

    private String createChatId(Long senderId, Long recipientId) {
        String chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = buildChatRoom(chatId, senderId, recipientId);
        ChatRoom recipientSender = buildChatRoom(chatId, recipientId, senderId);

        chatRoomRepository.saveAll(List.of(senderRecipient, recipientSender));

        return chatId;
    }

    private ChatRoom buildChatRoom(String chatId, Long senderId, Long recipientId) {
        return ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();
    }

}
