package com.example.driveanalysis.user.service;

import com.example.driveanalysis.base.exception.DataNotFoundException;
import com.example.driveanalysis.base.exception.SignupEmailDuplicatedException;
import com.example.driveanalysis.base.exception.SignupUsernameDuplicatedException;
import com.example.driveanalysis.user.entity.SiteUser;
import com.example.driveanalysis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    // 스프링이 책임지고 PasswordEncoder 타입의 객체를 만들어야 하는 상황
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SiteUser create(String username, String email, String password) throws SignupUsernameDuplicatedException, SignupEmailDuplicatedException {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setProductPaid(false);
        user.setPassword(passwordEncoder.encode(password));

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (userRepository.existsByUsername(username)) {
                throw new SignupUsernameDuplicatedException("이미 사용중인 username 입니다.");
            } else {
                throw new SignupEmailDuplicatedException("이미 사용중인 email 입니다.");
            }
        }


        return user;
    }

    public SiteUser getUser(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("siteuser not found"));
    }

    @Transactional
    public void update(SiteUser users, String password) {
        users.setPassword(passwordEncoder.encode(password));
        userRepository.save(users);
    }

    @Transactional
    public void setProductPayTrue(SiteUser orderer) {
        if (!orderer.isProductPaid()){
            orderer.setProductPaid(true);
            userRepository.save(orderer);
        }
    }
}
