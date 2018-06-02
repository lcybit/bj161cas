package com.jefflee.entity.score;

import lombok.Data;

import javax.persistence.*;
import java.util.Map;

/**
 * Created by TGL on 2018/5/15.
 */
@Table(name = "sr_score")
@Data
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OrderBy
    private Integer student_id;
    private Integer exam_id;
    private Integer sr_course_id;

    private Float score;

    @Transient
    private String studentName;

    @Transient
    private Integer studentNo;

    @Transient
    private String className;

    @Transient//单科总分
    private Float totalScore;

    @Transient//所有科目总分
    private Float allScore;

    @Transient//单科排名
    private Integer rank;

    @Transient//整体排名
    private Integer allRank;
}
