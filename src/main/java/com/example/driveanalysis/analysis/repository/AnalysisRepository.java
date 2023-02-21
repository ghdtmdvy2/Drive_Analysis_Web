package com.example.driveanalysis.analysis.repository;

import com.example.driveanalysis.analysis.entity.Analysis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE analysis AUTO_INCREMENT = 1", nativeQuery = true)
    void truncate(); // 이거 지우면 안됨, truncateTable 하면 자동으로 이게 실행됨

    Page<Analysis> findAllByAuthor_Id(Pageable pageable, long id);

    Page<Analysis> findDistinctByAuthor_usernameContainsOrAuthor_Id(String kw, String kw1, String kw2, long id, Pageable pageable);

    @Query(value = "select a from Analysis a join fetch a.author where a.id = :analysisId")
    Optional<Analysis> findById(@Param(value = "analysisId") long analysisId);
}
