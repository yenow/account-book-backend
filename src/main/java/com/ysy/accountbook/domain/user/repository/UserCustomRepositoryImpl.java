package com.ysy.accountbook.domain.user.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    private final EntityManager entityManager;

    public User findByIdForTest(Long userId) {
        final JPAQuery<User> query = new JPAQuery<>(entityManager);

        query.from(qUser)
                .where(qUser.userId.eq(userId));

        return query.fetchOne();
    }
}
