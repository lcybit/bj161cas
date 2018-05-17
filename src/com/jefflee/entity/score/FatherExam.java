package com.jefflee.entity.score;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by TGL on 2018/5/17.
 */
@Table(name = "sr_father_exam")
@Data
public class FatherExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer no;

    private String name;

    @OrderBy("desc")
    private String begin_time;

    private String end_time;
}
