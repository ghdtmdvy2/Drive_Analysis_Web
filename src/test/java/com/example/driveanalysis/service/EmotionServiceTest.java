package com.example.driveanalysis.service;

import com.example.driveanalysis.analysis.entity.Analysis;
import com.example.driveanalysis.analysis.service.AnalysisService;
import com.example.driveanalysis.base.util.Ut;
import com.example.driveanalysis.emotion.entity.Emotion;
import com.example.driveanalysis.emotion.service.EmotionService;
import com.example.driveanalysis.user.entity.SiteUser;
import com.example.driveanalysis.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EmotionServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    EmotionService emotionService;

    @Autowired
    AnalysisService analysisService;
    @Test
    void current_user_emotion_find(){
        SiteUser user1 = userService.getUser("user1");
        SiteUser user2 = userService.getUser("user2");

        List<Emotion> emotionsUser1 = emotionService.currentUserFindEmotions(user1.getId(), "");
        assertThat(emotionsUser1).isNotEmpty();

        List<Emotion> emotionsUser2 = emotionService.currentUserFindEmotions(user2.getId(), "");
        assertThat(emotionsUser2).isNotEmpty();
    }
    @Test
    void entire_user_emotion_find(){
        List<Emotion> emotions = emotionService.allUsersFindEmotions("");
        assertThat(emotions).isNotEmpty();
    }
    @Test
    void difference_emotion_between_current_user_entire_user_not_same(){
        SiteUser user1 = userService.getUser("user1");
        List<Emotion> emotionsUser1 = emotionService.currentUserFindEmotions(user1.getId(),"");
        assertThat(emotionsUser1).isNotEmpty();
        List<Emotion> emotionsEntireEmotion = emotionService.allUsersFindEmotions("");
        assertThat(emotionsEntireEmotion).isNotEmpty();

        double[] emotionsAvgUser1 = Ut.emotionsAvgCreate(emotionsUser1);
        double[] emotionsAvgEntireUser = Ut.emotionsAvgCreate(emotionsEntireEmotion);

        assertThat(emotionsAvgEntireUser[0]).isNotSameAs(emotionsAvgUser1[0]);
        assertThat(emotionsAvgEntireUser[1]).isNotSameAs(emotionsAvgUser1[1]);
        assertThat(emotionsAvgEntireUser[2]).isNotSameAs(emotionsAvgUser1[2]);
    }

    @Test
    void emotion_create(){
        SiteUser user = userService.getUser("user3");
        Analysis analysis = analysisService.create(user);
        emotionService.emotionCreate(analysis,30,50,20);
        emotionService.emotionCreate(analysis,30,50,20);
        emotionService.emotionCreate(analysis,30,50,20);
        List<Emotion> emotions = emotionService.currentUserFindEmotions(user.getId(), "");
        double[] emotionsAvgUser = Ut.emotionsAvgCreate(emotions);
        assertThat(emotionsAvgUser[0]).isEqualTo(30);
        assertThat(emotionsAvgUser[1]).isEqualTo(50);
        assertThat(emotionsAvgUser[2]).isEqualTo(20);
    }
}