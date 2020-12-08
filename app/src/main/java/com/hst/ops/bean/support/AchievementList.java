package com.hst.ops.bean.support;

public class AchievementList {

    private String achievement_id;
    private String achievement_image;
    private String achievement_title_en;
    private String achievement_title_ta;
    private String achievement_text_en;
    private String achievement_text_ta;
    private String achievement_date;

    public AchievementList(String achievement_id, String achievement_image, String achievement_title_en,
                           String achievement_text_en, String achievement_date) {
        this.achievement_id = achievement_id;
        this.achievement_image = achievement_image;
        this.achievement_title_en = achievement_title_en;
        this.achievement_text_en = achievement_text_en;
        this.achievement_date = achievement_date;
    }

    public String getAchievement_id() {
        return achievement_id;
    }

    public void setAchievement_id(String achievement_id) {
        this.achievement_id = achievement_id;
    }

    public String getAchievement_image() {
        return achievement_image;
    }

    public void setAchievement_image(String achievement_image) {
        this.achievement_image = achievement_image;
    }

    public String getAchievement_title_en() {
        return achievement_title_en;
    }

    public void setAchievement_title_en(String achievement_title_en) {
        this.achievement_title_en = achievement_title_en;
    }

    public String getAchievement_title_ta() {
        return achievement_title_ta;
    }

    public void setAchievement_title_ta(String achievement_title_ta) {
        this.achievement_title_ta = achievement_title_ta;
    }

    public String getAchievement_text_en() {
        return achievement_text_en;
    }

    public void setAchievement_text_en(String achievement_text_en) {
        this.achievement_text_en = achievement_text_en;
    }

    public String getAchievement_text_ta() {
        return achievement_text_ta;
    }

    public void setAchievement_text_ta(String achievement_text_ta) {
        this.achievement_text_ta = achievement_text_ta;
    }

    public String getAchievement_date() {
        return achievement_date;
    }

    public void setAchievement_date(String achievement_date) {
        this.achievement_date = achievement_date;
    }
}
