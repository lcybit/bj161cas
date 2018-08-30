package com.jefflee.util;

import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.schedule.Arrangement;
import com.jefflee.entity.schedule.Grade;
import com.jefflee.entity.schedule.Plan;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.ArrangementMapper;
import com.jefflee.mapper.schedule.GradeMapper;
import com.jefflee.mapper.schedule.PlanMapper;
import com.jefflee.service.schedule.ScheduleService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("cache")
public class Cache {

    private List<Arrangement> scheduleArrangementList;
    private List<Arrangement> semesterArrangementList;
    private List<Plan> schedulePlanList;
    private Map<String, Map<String, Integer>> backgroundMap;

    @Resource(name = "arrangementMapper")
    private ArrangementMapper arrangementMapper;
    @Resource(name = "planMapper")
    private PlanMapper planMapper;
    @Resource(name = "gradeMapper")
    private GradeMapper gradeMapper;

    @Resource(name = "scheduleService")
    private ScheduleService scheduleService;

    private Cache() {
    }

    public List<Arrangement> getScheduleArrangementList() {
        return scheduleArrangementList;
    }

    public List<Arrangement> getSemesterArrangementList() {
        return semesterArrangementList;
    }

    public List<Plan> getSchedulePlanList() {
        return schedulePlanList;
    }

    public Map<String, Map<String, Integer>> getBackgroundMap() {
        return backgroundMap;
    }

    public void initial(Integer scheduleId) {
        scheduleArrangementList = selectArrangementDetailListByScheduleId(scheduleId);

        semesterArrangementList = scheduleArrangementList;
        List<Integer> semesterScheduleIdList = getSemesterScheduleIdList(scheduleId);
        for (Integer semesterScheduleId : semesterScheduleIdList) {
            semesterArrangementList.addAll(selectArrangementDetailListByScheduleId(semesterScheduleId));
        }

        schedulePlanList = selectPlanDetailListByScheduleId(scheduleId);

        backgroundMap = new HashMap<String, Map<String, Integer>>();
        backgroundMap.put("conflicting", new HashMap<String, Integer>());
        backgroundMap.put("adjusted", new HashMap<String, Integer>());
        backgroundMap.put("selected", new HashMap<String, Integer>());
    }

    private List<Arrangement> selectArrangementDetailListByScheduleId(Integer scheduleId) {
        Integer grade = scheduleService.selectById(scheduleId).getGrade().getGrade();
        List<Arrangement> arrangementList = arrangementMapper.selectDetailListByScheduleId(scheduleId);
        for (Arrangement arrangement : arrangementList) {
            Tclass tclass = arrangement.getTclass();
            if (tclass != null) {
                arrangement.getTclass().setGrade(grade);
            }
        }
        return arrangementList;
    }

    private List<Plan> selectPlanDetailListByScheduleId(Integer scheduleId) {
        Integer grade = scheduleService.selectById(scheduleId).getGrade().getGrade();
        List<Plan> planList = planMapper.selectDetailListByScheduleId(scheduleId);
        for (Plan plan : planList) {
            Tclass tclass = plan.getTclass();
            if (tclass != null) {
                tclass.setGrade(grade);
            }
        }
        return planList;
    }

    // TODO 待补充
    private List<Integer> getSemesterScheduleIdList(Integer scheduleId) {
        List<Integer> scheduleIdList = new ArrayList<Integer>();

        Schedule schedule = scheduleService.selectById(scheduleId);
        Grade grade = schedule.getGrade();
        Integer startWeek = schedule.getStartWeek();

        Grade queryGrade = new Grade();
        queryGrade.setYear(grade.getYear());
        queryGrade.setSemester(grade.getSemester());
        queryGrade.setLevel(grade.getLevel());
        List<Grade> semesterGradeList = gradeMapper.select(queryGrade);

        for (Grade semesterGrade : semesterGradeList) {
            if(semesterGrade.getGradeId().equals(grade.getGradeId())){
                continue;
            }
            List<Schedule> gradeScheduleList = scheduleService.selectListByGradeId(semesterGrade.getGradeId());
            Integer semesterScheduleId = null;
            for (Schedule gradeSchedule : gradeScheduleList) {
                if (gradeSchedule.getStartWeek() <= startWeek) {
                    semesterScheduleId = gradeSchedule.getScheduleId();
                } else {
                    break;
                }
            }
            if(semesterScheduleId != null){
                scheduleIdList.add(semesterScheduleId);
            }
        }
        return scheduleIdList;
    }

}
