package com.jefflee.entity.information;

import com.jefflee.entity.schedule.Grade;

import javax.persistence.*;
import java.util.Calendar;

@Table(name = "info_tclass")
public class Tclass {
    @Id
    // TODO 定好顺序
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tclassId;
    @OrderBy
    private Integer year;
    @OrderBy
    private Integer tclassNo;
    @Transient
    private String name;
    @Transient
    private String shortName;
    private Integer type;
    private Integer level;
    @Transient
    private Integer grade=0;

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getTclassId() {
        return tclassId;
    }

    public void setTclassId(Integer tclassId) {
        this.tclassId = tclassId;
    }

    public Integer getTclassNo() {
        return tclassNo;
    }

    public void setTclassNo(Integer tclassNo) {
        this.tclassNo = tclassNo;
    }

    public String getName() {
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
        name.append(this.tclassNo);
        name.append("班");
        return name.toString();
    }

    public String getShortName() {
        StringBuilder name = new StringBuilder();
        switch (grade) {
            case 0:
                name.append("-");
                break;
            case 1:
                name.append("=");
                break;
            case 2:
                name.append("三");
                break;
            default:
                name.append("?");
                break;
        }
        name.append(this.tclassNo);
        return name.toString();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
