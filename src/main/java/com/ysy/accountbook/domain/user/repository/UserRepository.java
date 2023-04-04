package com.ysy.accountbook.domain.user.repository;

import com.ysy.accountbook.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {

}
