package com.ms.user.services;

import com.ms.user.models.UserModel;
import com.ms.user.producers.UserProducer;
import com.ms.user.repositories.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final IUserRepository userRepository;
    private final UserProducer userProducer;

    public UserService(IUserRepository userRepository, UserProducer userProducer) {
        this.userRepository = userRepository;
        this.userProducer = userProducer;
    }

    @Transactional
    public UserModel saveUser(UserModel userModel) {
        var user = userRepository.save(userModel);
        userProducer.publishMessageEmail(user);
        return user;
    }
}
