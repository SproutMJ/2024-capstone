package hello.aimju.image.repository;

import hello.aimju.chat.chat_room.domain.ChatRoom;
import hello.aimju.image.entity.IngredientsImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientsImageRepository extends JpaRepository<IngredientsImage, Long> {
    IngredientsImage findByChatRoom(ChatRoom chatRoom);
}
