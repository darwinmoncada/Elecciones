package com.votaciones.Services;

import com.votaciones.Models.User;

import com.votaciones.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User findByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean checkPassword(User user, String password) {
        return user.getPassword().equals(password);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
