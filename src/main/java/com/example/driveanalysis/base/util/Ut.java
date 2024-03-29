package com.example.driveanalysis.base.util;

import com.example.driveanalysis.emotion.entity.Emotion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Ut {
    public static double[] emotionsAvgCreate(List<Emotion> usersEmotions) {
        double[] emotions = new double[3];
        emotions[0] = emotionAvgCreate(usersEmotions,"angry");
        emotions[1] = emotionAvgCreate(usersEmotions,"happy");
        emotions[2] = emotionAvgCreate(usersEmotions,"neutral");
        return emotions;
    }
    public static double emotionAvgCreate(List<Emotion> usersEmotions, String findEmotion){
        double extractEmotion = 0;
        if (findEmotion.equals("angry")){
            for (Emotion emotion : usersEmotions){
                extractEmotion += emotion.getAngry();
            }
        } else if (findEmotion.equals("happy")){
            for (Emotion emotion : usersEmotions){
                extractEmotion += emotion.getHappy();
            }
        } else {
            for (Emotion emotion : usersEmotions){
                extractEmotion += emotion.getNeutral();
            }
        }
        extractEmotion = extractEmotion/(usersEmotions.size());
        return extractEmotion;
    }

    public static double[] diffAvgEmotions(double[] currentUserEmotionAvg, double[] otherUserEmotionAvg) {
        double[] diffEmotions = new double[3];
        diffEmotions[0] = diffAvgEmotion(otherUserEmotionAvg[0],currentUserEmotionAvg[0]);
        diffEmotions[1] = diffAvgEmotion(otherUserEmotionAvg[1],currentUserEmotionAvg[1]);
        diffEmotions[2] = diffAvgEmotion(otherUserEmotionAvg[2],currentUserEmotionAvg[2]);
        return diffEmotions;
    }
    public static double diffAvgEmotion(double currentUserEmotion, double otherUserEmotion){
        return Math.abs(Math.round((currentUserEmotion - otherUserEmotion) * 100) / 100.0);
    }

    public static class Date {
        public static int getEndDayOf(int year, int month) {
            String yearMonth = year + "-" + "%02d".formatted(month);

            return getEndDayOf(yearMonth);
        }

        public static int getEndDayOf(String yearMonth) {
            LocalDate convertedDate = LocalDate.parse(yearMonth + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            convertedDate = convertedDate.withDayOfMonth(
                    convertedDate.getMonth().length(convertedDate.isLeapYear()));

            return convertedDate.getDayOfMonth();
        }

        public static LocalDateTime parse(String pattern, String dateText) {
            return LocalDateTime.parse(dateText, DateTimeFormatter.ofPattern(pattern));
        }

        public static LocalDateTime parse(String dateText) {
            return parse("yyyy-MM-dd HH:mm:ss.SSSSSS", dateText);
        }
    }

}
