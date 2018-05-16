package com.jefflee.entity.score;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by TGL on 2018/5/16.
 */
@Table(name = "sr_course")
@Data
public class SrCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //父课程id
    private Integer course_id;

    //子课程编号
    @OrderBy
    private Integer sr_course_no;

    //子课程名称
    private String sr_course_name;
}
