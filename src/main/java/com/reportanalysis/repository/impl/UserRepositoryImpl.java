package com.reportanalysis.repository.impl;

import com.reportanalysis.config.DatabaseConfig;
import com.reportanalysis.model.User;
import com.reportanalysis.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public List<User> findAllUsers() {

        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setCountryCode(rs.getString("country_code"));
                user.setMobileNumber(rs.getString("mobile_number"));
                user.setCreatedAt(rs.getLong("created_at"));

                users.add(user);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch users", e);
        }

        return users;
    }

    @Override
    public User findById(Long id) {

        String sql = "SELECT * FROM users WHERE id = ?";
        User user = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setCountryCode(rs.getString("country_code"));
                user.setMobileNumber(rs.getString("mobile_number"));
                user.setCreatedAt(rs.getLong("created_at"));
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user by ID", e);
        }

        return user;
    }
}
