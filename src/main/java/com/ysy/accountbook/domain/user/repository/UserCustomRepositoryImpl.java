package com.ysy.accountbook.domain.user.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.ysy.accountbook.domain.user.entity.QUser;
import com.ysy.accountbook.domain.user.entity.User;

import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QUser qUser = QUser.user;


    public User findByIdForTest(Long userId) {

        return (User) jpaQueryFactory.from(qUser)
                                     .where(qUser.userId.eq(userId))
                                     .fetchOne();
    }

    @Override
    public void updateRefreshToken(Long userId, String refreshToken) {

        jpaQueryFactory.update(QUser.user)
                       .set(qUser.refreshToken, refreshToken)
                       .where(qUser.userId.eq(userId))
                       .execute();
    }

    @Override
    public String getRefreshTokenById(Long userId) {
        User user = (User) jpaQueryFactory.from(QUser.user)
                                          .where(QUser.user.userId.eq(userId))
                                          .fetchOne();

        assert user != null;
        return user.getRefreshToken();
    }
}
