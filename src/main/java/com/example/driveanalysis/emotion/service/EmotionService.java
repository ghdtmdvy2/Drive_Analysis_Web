package com.example.driveanalysis.emotion.service;

import com.example.driveanalysis.analysis.entity.Analysis;
import com.example.driveanalysis.base.util.Ut;
import com.example.driveanalysis.emotion.entity.Emotion;
import com.example.driveanalysis.emotion.repository.EmotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmotionService {
    private final EmotionRepository emotionRepository;
    public List<Emotion> currentUserFindEmotions(Long authorId, String yearMonth) {
        if (yearMonth == null || yearMonth.trim().length() == 0){
            return emotionRepository.findAllByAuthorId(authorId);
        }
        int monthEndDay = Ut.Date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Ut.Date.parse(fromDateStr);
        LocalDateTime toDate = Ut.Date.parse(toDateStr);

        return emotionRepository.findAllByCreatedDateBetweenAndAuthorId(fromDate,toDate,authorId);
    }


    public List<Emotion> allUsersFindEmotions(String yearMonth) {
        if (yearMonth == null || yearMonth.trim().length() == 0){
            return emotionRepository.findAll();
        }
        int monthEndDay = Ut.Date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Ut.Date.parse(fromDateStr);
        LocalDateTime toDate = Ut.Date.parse(toDateStr);

        return emotionRepository.findAllByCreatedDateBetween(fromDate,toDate);
    }
    @Transactional
    public void emotionCreate(Analysis analysis, double angry, double happy, double neutral) {
        Emotion emotion = new Emotion();
        emotion.setAnalysis(analysis);
        emotion.setAuthor(analysis.getAuthor());
        emotion.setAngry(angry);
        emotion.setHappy(happy);
        emotion.setNeutral(neutral);
        emotion.setCreatedDate(LocalDateTime.now());
        emotionRepository.save(emotion);
    }
    @Transactional
    public void testDataEmotionCreate(Analysis analysis, double angry, double happy, double neutral, String createDate) {
        Emotion emotion = new Emotion();
        emotion.setAnalysis(analysis);
        emotion.setAuthor(analysis.getAuthor());
        emotion.setAngry(angry);
        emotion.setHappy(happy);
        emotion.setNeutral(neutral);
        LocalDateTime createDateEmotion = Ut.Date.parse(createDate);
        emotion.setCreatedDate(createDateEmotion);
        emotionRepository.save(emotion);
    }
}
