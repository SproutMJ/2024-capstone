package hello.aimju.User.service;

import hello.aimju.User.domain.User;
import hello.aimju.User.dto.SignupRequestDto;
import hello.aimju.User.dto.StatusResponseDto;
import hello.aimju.User.dto.UserInfoResponseDto;
import hello.aimju.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<StatusResponseDto> signup(SignupRequestDto requestDto) {
        String userName = requestDto.getUserName();
        String password = requestDto.getPassword();


        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUserName(userName);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 등록
        User user = new User(userName, password);
        userRepository.save(user);

        StatusResponseDto res = new StatusResponseDto("회원가입이 완료되었습니다.", 200);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public UserInfoResponseDto getUserInfo(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("해당 사용자가 존재하지 않습니다.");
        }

        User userInfo = user.get();

        return new UserInfoResponseDto(userInfo.getUserName(), userInfo.getPassword());
    }

}
