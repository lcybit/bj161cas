package com.jefflee.entity.information;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private Integer student_no;
    private Integer begin_year;
    private Integer offset;
}
