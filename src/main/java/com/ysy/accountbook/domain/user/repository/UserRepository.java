package com.ysy.accountbook.domain.user.repository;

import com.ysy.accountbook.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.DoubleStream;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByEmailAndIsDelete(String email, boolean isDelete);

    Optional<User> findByEmail(String email);
}
