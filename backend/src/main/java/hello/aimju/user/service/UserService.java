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
            StatusResponseDto res = new StatusResponseDto("중복된 사용자가 존재합니다", 401);
            System.out.println("중복!!!");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }

        // 사용자 등록
        User user = new User(userName, password);
        userRepository.save(user);

        StatusResponseDto res = new StatusResponseDto("회원가입이 완료되었습니다.", 200);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public CurrentUserInfoResponseDto getCurrentUserInfo(HttpSession session) {
        User user = getUserFromSession(session);
        return new CurrentUserInfoResponseDto(user.getId(), user.getUserName());
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
        User curruentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: " + user.getId()));

        return new UserDetailResponseDto(curruentUser);
    }

    @Transactional
    public ResponseEntity<?> changeUserName(ChangeUserNameRequestDto requestDto, HttpSession session) {
        User user = getUserFromSession(session);
        if (requestDto.getUserName().equals(user.getUserName()) && requestDto.getPassword().equals(user.getPassword())) {
            if (userRepository.existsByUserName(requestDto.getNewUserName())) {
                return new ResponseEntity<>(new StatusResponseDto("이미 존재하는 사용자 이름입니다.", 400), HttpStatus.OK);
            }
            user.setUserName(requestDto.getNewUserName());
            userRepository.save(user);
            StatusResponseDto res = new StatusResponseDto("이름이 변경되었습니다.", 200);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new StatusResponseDto("회원 정보가 일치하지 않습니다", 401), HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<?> changePassword(ChangePasswordRequestDto requestDto, HttpSession session) {
        User user = getUserFromSession(session);
        if (requestDto.getUserName().equals(user.getUserName()) && requestDto.getPassword().equals(user.getPassword())) {
            user.setPassword(requestDto.getNewPassword());
            userRepository.save(user);
            StatusResponseDto res = new StatusResponseDto("비밀번호가 변경되었습니다.", 200);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new StatusResponseDto("회원 정보가 일치하지 않습니다", 401), HttpStatus.OK);
        }
    }

    public ResponseEntity<?> deleteUser(SignupRequestDto requestDto, HttpSession session) {
        User user = getUserFromSession(session);
        if (requestDto.getUserName().equals(user.getUserName()) && requestDto.getPassword().equals(user.getPassword())) {
            User deleteUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: " + user.getId()));;
            userRepository.delete(deleteUser);
            session.invalidate();
            StatusResponseDto res = new StatusResponseDto("삭제되었습니다.", 200);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new StatusResponseDto("회원 정보가 일치하지 않습니다", 401), HttpStatus.OK);
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
