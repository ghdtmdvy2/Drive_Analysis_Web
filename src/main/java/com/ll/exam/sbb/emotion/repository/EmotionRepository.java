package com.ll.exam.sbb.emotion.repository;

import com.ll.exam.sbb.base.util.RepositoryUtil;
import com.ll.exam.sbb.emotion.entity.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public interface EmotionRepository extends JpaRepository<Emotion, Long>, RepositoryUtil {

    ArrayList<Emotion> findByAuthor_id(Long id);


    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE emotion AUTO_INCREMENT = 1", nativeQuery = true)
    void truncate(); // 이거 지우면 안됨, truncateTable 하면 자동으로 이게 실행됨
}