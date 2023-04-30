package com.ysy.accountbook.domain.user.repository;

import com.ysy.accountbook.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
    Optional<User> findUserByEmail(String email);
}
