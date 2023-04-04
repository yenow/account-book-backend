package com.ysy.accountbook.domain.commoncode.repository;

import com.ysy.accountbook.domain.commoncode.entity.CommonCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommonCodeRepositoryTest {

    @Autowired
    private CommonCodeRepository commonCodeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    void saveAndFind() {
        CommonCode parentCommonCode = CommonCode.builder()
                .codeId("ROOT")
                .codeName("루트")
                .build();
        commonCodeRepository.save(parentCommonCode);

        String commonCodeId = "TEST_001";
        CommonCode commonCode = CommonCode.builder()
                .codeId(commonCodeId)
                .codeName("테스트001")
                .parentCommonCode(parentCommonCode)
                .build();
        commonCodeRepository.save(commonCode);

        entityManager.flush();

        Optional<CommonCode> optionalCommonCode = commonCodeRepository.findById(commonCodeId);
        CommonCode findCommonCode = optionalCommonCode.orElse(null);
        CommonCode findParentCommonCode = Objects.requireNonNull(findCommonCode).getParentCommonCode();
        log.info("findCommonCode : {}", findCommonCode);
        log.info("findParentCommonCode : {}, {}", findParentCommonCode.getCodeId(), findParentCommonCode.getCodeName());
    }

    @Test
    void commonCodeToString() {
        CommonCode parentCommonCode = CommonCode.builder()
                .codeId("ROOT")
                .codeName("루트")
                .build();

        String commonCodeId = "TEST_001";
        CommonCode commonCode = CommonCode.builder()
                .codeId(commonCodeId)
                .codeName("테스트001")
                .parentCommonCode(parentCommonCode)
                .createdDate(LocalDateTime.now())
                .build();

        log.info("{}", parentCommonCode);
        log.info("{}", commonCode);
    }
}