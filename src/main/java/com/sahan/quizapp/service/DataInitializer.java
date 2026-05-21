package com.sahan.quizapp.service;

import com.sahan.quizapp.dao.UserDao;
import com.sahan.quizapp.model.Role;
import com.sahan.quizapp.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserDao userDao;

    public DataInitializer(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void run(String... args) throws Exception {
        for (User u : userDao.findAll()) {
            if (u.getRole() == null) {
                u.setRole(Role.STUDENT);
                userDao.save(u);
            }
        }
    }
}
