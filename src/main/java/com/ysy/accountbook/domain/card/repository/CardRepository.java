package com.ysy.accountbook.domain.card.repository;

import com.ysy.accountbook.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<User, Long> {

}
