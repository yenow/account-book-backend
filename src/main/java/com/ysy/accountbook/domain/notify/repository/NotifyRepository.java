package com.ysy.accountbook.domain.notify.repository;

import com.ysy.accountbook.domain.notify.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifyRepository extends JpaRepository<Notify, Long> {
}
