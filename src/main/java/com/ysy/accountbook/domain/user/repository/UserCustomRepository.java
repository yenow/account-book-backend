package com.ysy.accountbook.domain.user.repository;

import com.ysy.accountbook.domain.user.entity.User;

public interface UserCustomRepository {

    User findByIdForTest(Long userId);
}
