package com.jefflee.entity.information;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by TGL on 2018/5/15.
 */
@Table(name = "info_student")
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OrderBy
    private Integer student_no;
    private Integer begin_year;
    private Integer offset;
    private Integer sex;

    @Transient
    private String sexName;

    public String getSexName() {
        if (sex == 0)
            return "男";
        else if (sex == 1) return "女";
        else return "未知性别";
    }
}