package com.jefflee.entity.schedule;

import javax.persistence.*;

@Table(name = "schd_grade")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gradeId;
    @OrderBy(value = "DESC")
    private Integer year;
    @OrderBy(value = "DESC")
    private Integer semester;
    @OrderBy
    private Integer level;
    @OrderBy
    private Integer grade;
    private String startDate;

    @Transient
    private String name;
    @Transient
    private String shortName;

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        StringBuilder name = new StringBuilder();
        name.append(this.year);
        name.append("年");
        name.append(getShortName());
        switch (this.semester) {
            case 0:
                name.append("上学期");
                break;
            case 1:
                name.append("下学期");
                break;
            default:
                break;
        }
        return name.toString();
    }

    public String getShortName() {
        StringBuilder name = new StringBuilder();
        switch (this.level) {
            case 0:
                name.append("初");
                break;
            case 1:
                name.append("高");
                break;
            default:
                break;
        }
        switch (this.grade) {
            case 0:
                name.append("一");
                break;
            case 1:
                name.append("二");
                break;
            case 2:
                name.append("三");
                break;
            default:
                name.append("中");
                break;
        }
        return name.toString();
    }

}
