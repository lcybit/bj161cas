package com.jefflee.service.schedule.impl;

import com.jefflee.entity.schedule.Grade;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.ScheduleMapper;
import com.jefflee.service.information.CourseService;
import com.jefflee.service.information.PeriodService;
import com.jefflee.service.information.TclassService;
import com.jefflee.service.information.TeacherService;
import com.jefflee.service.schedule.GradeService;
import com.jefflee.service.schedule.ScheduleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

    @Resource(name = "scheduleMapper")
    private ScheduleMapper scheduleMapper;

    @Resource(name = "periodService")
    private PeriodService periodService;
    @Resource(name = "courseService")
    private CourseService courseService;
    @Resource(name = "tclassService")
    private TclassService tclassService;
    @Resource(name = "teacherService")
    private TeacherService teacherService;

    @Resource(name = "gradeService")
    private GradeService gradeService;

    @Override
    public Integer insert(Schedule schedule) {
        if (scheduleMapper.insert(schedule) == 1) {
            return schedule.getScheduleId();
        } else {
            return null;
        }
    }

    @Override
    public List<Schedule> selectList() {
        List<Schedule> scheduleList = scheduleMapper.selectDetailList();
        sortScheduleList(scheduleList);
        return scheduleList;
    }

    @Override
    public List<Schedule> selectListByGradeId(Integer gradeId) {
        return scheduleMapper.selectDetailListByGradeId(gradeId);
    }

    private void sortScheduleList(List<Schedule> scheduleList) {
        Collections.sort(scheduleList, new Comparator<Schedule>() {
            @Override
            public int compare(Schedule o1, Schedule o2) {
                Grade grade1 = o1.getGrade();
                Grade grade2 = o2.getGrade();
                Integer year1 = grade1.getYear();
                Integer year2 = grade2.getYear();
                Integer semester1 = grade1.getSemester();
                Integer semester2 = grade2.getSemester();
                Integer level1 = grade1.getLevel();
                Integer level2 = grade2.getLevel();
                return year1.equals(year2) ?
                        semester1.equals(semester2) ?
                                level1.equals(level2) ?
                                        grade1.getGrade().compareTo(grade2.getGrade()) :
                                        level1.compareTo(level2) :
                                -(semester1.compareTo(semester2)) :
                        -(year1.compareTo(year2));
            }
        });
    }

    @Override
    public Schedule selectById(Integer scheduleId) {
        return scheduleMapper.selectDetailById(scheduleId);
    }

    @Override
    public Integer updateById(Schedule schedule) {
        if (scheduleMapper.updateByPrimaryKey(schedule) == 1) {
            return schedule.getScheduleId();
        } else {
            return null;
        }
    }

    @Override
    public Integer deleteById(Integer scheduleId) {
        if (scheduleMapper.deleteByPrimaryKey(scheduleId) == 1) {
            return scheduleId;
        } else {
            return null;
        }
    }

}