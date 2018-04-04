package com.cc.admin.entity;

public class ScorePage {
    //1:序号 2:学期 3:课程名字 4：成绩 5:学分 6：总学时 7:考核方式 8:课程属性 9:课程性质
    private String id;
    private String semester;
    private String courseName;
    private String score;
    private String credit;
    private String totalHours;
    private String method;
    private String attribute;
    private String type;


    @Override
    public String toString() {
        return "ScorePage{" +
                "id='" + id + '\'' +
                ", semester='" + semester + '\'' +
                ", courseName='" + courseName + '\'' +
                ", score='" + score + '\'' +
                ", credit='" + credit + '\'' +
                ", totalHours='" + totalHours + '\'' +
                ", method='" + method + '\'' +
                ", attribute='" + attribute + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public ScorePage() {
    }

    public ScorePage(String id, String semester, String courseName, String score, String credit, String totalHours, String method, String attribute, String type) {
        this.id = id;
        this.semester = semester;
        this.courseName = courseName;
        this.score = score;
        this.credit = credit;
        this.totalHours = totalHours;
        this.method = method;
        this.attribute = attribute;
        this.type = type;
    }

    public void setAll(String id, String semester, String courseName, String score, String credit, String totalHours, String method, String attribute, String type){
        this.id = id;
        this.semester = semester;
        this.courseName = courseName;
        this.score = score;
        this.credit = credit;
        this.totalHours = totalHours;
        this.method = method;
        this.attribute = attribute;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(String totalHours) {
        this.totalHours = totalHours;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
