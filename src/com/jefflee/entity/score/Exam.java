package com.jefflee.entity.score;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by TGL on 2018/5/16.
 */
@Table(name = "sr_exam")
@Data
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //考试场次
    private Integer exam_no;
    //考试名称
    @Transient
    private String exam_name;
    //考试时间
    private String exam_time;
    //考试科目id
    @OrderBy
    private Integer course_id;
    //考试科目名称
    @Transient
    private String course_name;
}
