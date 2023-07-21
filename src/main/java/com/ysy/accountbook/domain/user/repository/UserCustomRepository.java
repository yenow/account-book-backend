package com.ysy.accountbook.domain.user.repository;

import com.ysy.accountbook.domain.user.entity.User;

public interface UserCustomRepository {

    User findByIdForTest(Long userId);

    /**
     * 리프레쉬 토큰 저장
     * @param id
     * @param refreshToken
     */
    void updateRefreshToken(Long id, String refreshToken);

    String getRefreshTokenById(Long id);
}
