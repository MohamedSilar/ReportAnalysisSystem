package com.reportanalysis.repository;

import java.util.List;

import com.reportanalysis.model.User;

public interface UserRepository {

    User findById(Long userId);
    List<User> findAllUsers();


}
