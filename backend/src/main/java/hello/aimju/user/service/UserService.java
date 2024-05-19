package hello.aimju.user.service;

import hello.aimju.login.session.SessionConst;
import hello.aimju.user.domain.User;
import hello.aimju.user.dto.*;
import hello.aimju.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
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

        System.out.println("userName = " + userName);
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

    public UserDetailResponseDto getUserDetail(HttpSession session) {
        User user = getUserFromSession(session);
        return new UserDetailResponseDto(user);
    }

    @Transactional
    public ResponseEntity<?> changeUserName(ChangeUserNameRequestDto requestDto, HttpSession session) {
        User user = getUserFromSession(session);
        if (requestDto.getUserName().equals(user.getUserName()) && requestDto.getPassWord().equals(user.getPassword())) {
            if (userRepository.existsByUserName(requestDto.getNewUserName())) {
                return new ResponseEntity<>(new StatusResponseDto("이미 존재하는 사용자 이름입니다.", 400), HttpStatus.BAD_REQUEST);
            }
            user.setUserName(requestDto.getNewUserName());
            userRepository.save(user);
            StatusResponseDto res = new StatusResponseDto("이름이 변경되었습니다.", 200);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        else {
            throw new IllegalArgumentException("회원정보가 일치하지 않습니다");
        }
    }

    private User getUserFromSession(HttpSession session) {
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginUser == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        return loginUser;
    }

}
