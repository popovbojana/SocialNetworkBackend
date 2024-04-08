package com.internship.socialnetwork.service;

import com.internship.socialnetwork.model.ChatMessage;
import com.internship.socialnetwork.repository.ChatMessageRepository;
import com.internship.socialnetwork.service.impl.ChatMessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceImplTest {

    private static final Long SENDER_ID = 1L;

    private static final Long RECIPIENT_ID = 2L;

    private static final String CHAT_ID = "1_2";

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRoomService chatRoomService;

    @InjectMocks
    private ChatMessageServiceImpl chatMessageService;

    @Test
    void shouldReturnChatMessage_whenSave_ifChatRoomExists() {
        // given
        ChatMessage chatMessage = ChatMessage.builder()
                .id(1L)
                .senderId(SENDER_ID)
                .recipientId(RECIPIENT_ID)
                .build();

        when(chatRoomService.getChatRoomId(any(), any(), anyBoolean())).thenReturn(Optional.of("1_2"));
        when(chatMessageRepository.save(chatMessage)).thenReturn(chatMessage);

        // when
        ChatMessage savedMessage = chatMessageService.save(chatMessage);

        // then
        assertNotNull(savedMessage);
        assertEquals(CHAT_ID, savedMessage.getChatId());

        // and
        verify(chatRoomService).getChatRoomId(any(), any(), anyBoolean());
        verify(chatMessageRepository).save(any());
    }

    @Test
    void shouldReturnChatMessages_whenFindChatMessages_ifChatMessagesExist() {
        // given
        List<ChatMessage> chatMessages = List.of(new ChatMessage());

        when(chatRoomService.getChatRoomId(any(), any(), anyBoolean())).thenReturn(Optional.of(CHAT_ID));
        when(chatMessageRepository.findByChatId(any())).thenReturn(chatMessages);

        // when
        List<ChatMessage> foundMessages = chatMessageService.findChatMessages(SENDER_ID, RECIPIENT_ID);

        // then
        assertNotNull(foundMessages);
        assertEquals(1, foundMessages.size());

        // and
        verify(chatRoomService).getChatRoomId(any(), any(), anyBoolean());
        verify(chatMessageRepository).findByChatId(any());
    }

}
