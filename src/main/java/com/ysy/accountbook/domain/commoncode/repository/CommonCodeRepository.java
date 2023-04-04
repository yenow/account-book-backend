package com.ysy.accountbook.domain.commoncode.repository;

import com.ysy.accountbook.domain.commoncode.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonCodeRepository extends JpaRepository<CommonCode, String>, CommonCodeCustomRepository {
}
