package com.jefflee.entity.score;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by TGL on 2018/5/15.
 */
@Table(name = "sr_score")
@Data
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer student_id;
    private Integer exam_id;
    private Integer sr_course_id;

    private Float score;

    //总分
    @Transient
    private Float totalScore;
}
