package com.ysy.accountbook.domain.demo.repository;

import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.repository.UserCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoRepository extends JpaRepository<User, Long> {

}
