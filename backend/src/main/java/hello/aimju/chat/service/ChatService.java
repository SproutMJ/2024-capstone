package hello.aimju.chat.service;

import hello.aimju.chat.chat_message.domain.ChatMessage;
import hello.aimju.chat.chat_message.dto.ChatMessageRequestDto;
import hello.aimju.chat.chat_message.dto.GetAllChatMessageResponseDto;
import hello.aimju.chat.chat_message.repository.ChatMessageRepository;
import hello.aimju.chat.chat_room.domain.ChatRoom;
import hello.aimju.chat.chat_room.dto.ChatRoomRequestDto;
import hello.aimju.chat.chat_room.dto.GetAllChatRoomResponseDto;
import hello.aimju.chat.chat_room.repository.ChatRoomRepository;
import hello.aimju.login.session.SessionConst;
import hello.aimju.user.domain.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public void saveChatData(ChatRoomRequestDto chatRoomRequestDto, HttpSession session) {
        // ChatRoom 데이터 저장
        ChatRoom chatRoom = new ChatRoom(getUserFromSession(session), chatRoomRequestDto.getMenu());
        chatRoomRepository.save(chatRoom);

        // ChatMessage 데이터 저장
        for (ChatMessageRequestDto chatMessageRequestDto : chatRoomRequestDto.getMessages()) {
            ChatMessage chatMessage = new ChatMessage(chatMessageRequestDto.getContent(),
                    chatMessageRequestDto.getIsUser(),
                    chatMessageRequestDto.getChatType(),
                    chatRoom);
            chatMessageRepository.save(chatMessage);
        }
    }

    public List<GetAllChatRoomResponseDto> getAllChatRooms(HttpSession session) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByUserId(getUserFromSession(session).getId());
        return chatRoomList.stream()
                .map(this::mapToChatRoomDto)
                .collect(Collectors.toList());
    }

    public List<GetAllChatMessageResponseDto> getAllChatMessages(Long chatId) {
        // 채팅방 ID로 해당 채팅방의 모든 메시지 조회
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomId(chatId);

        // ChatMessage를 GetAllChatMessageResponseDto로 변환
        return chatMessages.stream()
                .map(this::mapToMessageDto)
                .collect(Collectors.toList());
    }

    private User getUserFromSession(HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 만약 세션에 사용자 정보가 없다면 로그인하지 않은 상태이므로 적절히 처리
        if (loginUser == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        return loginUser;
    }

    private GetAllChatRoomResponseDto mapToChatRoomDto(ChatRoom chatRoom) {
        GetAllChatRoomResponseDto dto = new GetAllChatRoomResponseDto();
        dto.setChatId(chatRoom.getId());
        dto.setMenu(chatRoom.getMenu());
        dto.setCreatedAt(chatRoom.getCreatedAt()); // ChatRoom에 생성일자 필드가 있어야 함
        return dto;
    }

    private GetAllChatMessageResponseDto mapToMessageDto(ChatMessage chatMessage) {
        GetAllChatMessageResponseDto dto = new GetAllChatMessageResponseDto();
        dto.setChatType(chatMessage.getChatType());
        dto.setIsUser(chatMessage.getIsUser());
        dto.setContent(chatMessage.getContent());
        return dto;
    }
}
