package hello.aimju.user.dto;

import hello.aimju.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailResponseDto {
    private String userName;
    private int recipesCount;
    private int chatRoomsCount;
    private int boardsCount;
    private int commentsCount;

    public UserDetailResponseDto(User user) {
        this.userName = user.getUserName();
        this.recipesCount = user.getRecipeCount();
        this.chatRoomsCount = user.getChatRoomCount();
        this.boardsCount = user.getBoardCount();
        this.commentsCount = user.getCommentCount();
    }
}
