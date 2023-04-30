package com.ysy.accountbook.domain.common_code.repository;

import com.ysy.accountbook.domain.common_code.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonCodeRepository extends JpaRepository<CommonCode, String>, CommonCodeCustomRepository {
}
