package com.internship.socialnetwork.service;

import com.internship.socialnetwork.model.ChatRoom;
import com.internship.socialnetwork.repository.ChatRoomRepository;
import com.internship.socialnetwork.service.impl.ChatRoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceImplTest {

    private static final Long SENDER_ID = 1L;

    private static final Long RECIPIENT_ID = 2L;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    @Test
    void shouldReturnChatRoomId_whenGetChatRoomId_ifChatRoomExists() {
        // given
        String chatId = "1_2";
        ChatRoom chatRoom = ChatRoom.builder()
                .chatId(chatId)
                .senderId(SENDER_ID)
                .recipientId(RECIPIENT_ID)
                .build();

        when(chatRoomRepository.findBySenderIdAndRecipientId(any(), any())).thenReturn(Optional.of(chatRoom));

        // when
        Optional<String> result = chatRoomService.getChatRoomId(SENDER_ID, RECIPIENT_ID, false);

        // then
        assertTrue(result.isPresent());
        assertEquals(chatId, result.get());

        // and
        verify(chatRoomRepository).findBySenderIdAndRecipientId(any(), any());
    }

    @Test
    void shouldReturnChatRoomId_whenGetChatRoomId_ifCreateRoomIsTrue() {
        // given
        when(chatRoomRepository.findBySenderIdAndRecipientId(any(), any())).thenReturn(Optional.empty());

        // when
        Optional<String> result = chatRoomService.getChatRoomId(SENDER_ID, RECIPIENT_ID, true);

        // then
        assertTrue(result.isPresent());
        assertTrue(result.get().contains(SENDER_ID.toString()));
        assertTrue(result.get().contains(RECIPIENT_ID.toString()));

        // and
        verify(chatRoomRepository).findBySenderIdAndRecipientId(any(), any());
    }

    @Test
    void shouldReturnChatRoomId_whenGetChatRoomId_ifCreateRoomIsFalse() {
        // given
        when(chatRoomRepository.findBySenderIdAndRecipientId(any(), any())).thenReturn(Optional.empty());

        // when
        Optional<String> result = chatRoomService.getChatRoomId(SENDER_ID, RECIPIENT_ID, false);

        // then
        assertFalse(result.isPresent());

        // and
        verify(chatRoomRepository).findBySenderIdAndRecipientId(any(), any());
    }


}
