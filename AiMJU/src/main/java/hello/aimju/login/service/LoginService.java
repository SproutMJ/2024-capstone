package hello.aimju.login.service;

import hello.aimju.User.domain.User;
import hello.aimju.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;

    /**
     * @return null 로그인 실패
     */
    public User login(String userName, String password) {
        return userRepository.findByUserName(userName)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
