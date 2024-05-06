package hello.aimju.chat.service;

import hello.aimju.user.domain.User;
import hello.aimju.user.repository.UserRepository;
import hello.aimju.chat.chat_message.domain.ChatMessage;
import hello.aimju.chat.chat_message.dto.ChatMessageRequestDto;
import hello.aimju.chat.chat_message.repository.ChatMessageRepository;
import hello.aimju.chat.chat_room.domain.ChatRoom;
import hello.aimju.chat.chat_room.dto.ChatRoomRequestDto;
import hello.aimju.chat.chat_room.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public void saveChatData(ChatRoomRequestDto chatRoomRequestDto) {
        // ChatRoom 데이터 저장
        ChatRoom chatRoom = new ChatRoom(getUserById(chatRoomRequestDto.getUserId()), chatRoomRequestDto.getMenu());
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

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));
    }
}
