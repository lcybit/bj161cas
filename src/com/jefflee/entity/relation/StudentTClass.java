package com.jefflee.entity.relation;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by TGL on 2018/5/26.
 */
@Table(name = "rlat_student_tclass")
@Data
public class StudentTClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer studentId;

    private Integer tclassId;
}
