package com.jefflee.mapper.schedule;

import java.util.List;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.jefflee.entity.schedule.Plan;
import com.jefflee.util.MyMapper;

public interface PlanMapper extends MyMapper<Plan> {

	@Select("select * from schd_plan where plan_id = #{planId}")
	@Results({ @Result(id = true, column = "plan_id", property = "planId"),
			@Result(column = "schedule_id", property = "scheduleId"),
			@Result(column = "course_id", property = "courseId"), @Result(column = "room_id", property = "roomId"),
			@Result(column = "tclass_id", property = "tclassId"),
			@Result(column = "teacher_id", property = "teacherId"),
			@Result(column = "course_id", property = "course", one = @One(select = "com.jefflee.mapper.information.CourseMapper.selectByPrimaryKey") ),
			@Result(column = "room_id", property = "room", one = @One(select = "com.jefflee.mapper.information.RoomMapper.selectByPrimaryKey") ),
			@Result(column = "tclass_id", property = "tclass", one = @One(select = "com.jefflee.mapper.information.TclassMapper.selectByPrimaryKey") ),
			@Result(column = "teacher_id", property = "teacher", one = @One(select = "com.jefflee.mapper.information.TeacherMapper.selectByPrimaryKey") ),
			@Result(column = "period_num", property = "periodNum") })
	public Plan selectDetailById(Integer planId);

	@Select("select * from schd_plan where schedule_id = #{scheduleId} order by teacher_id, course_id")
	@Results({ @Result(id = true, column = "plan_id", property = "planId"),
			@Result(column = "schedule_id", property = "scheduleId"),
			@Result(column = "course_id", property = "courseId"), @Result(column = "room_id", property = "roomId"),
			@Result(column = "tclass_id", property = "tclassId"),
			@Result(column = "teacher_id", property = "teacherId"),
			@Result(column = "course_id", property = "course", one = @One(select = "com.jefflee.mapper.information.CourseMapper.selectByPrimaryKey") ),
			@Result(column = "room_id", property = "room", one = @One(select = "com.jefflee.mapper.information.RoomMapper.selectByPrimaryKey") ),
			@Result(column = "tclass_id", property = "tclass", one = @One(select = "com.jefflee.mapper.information.TclassMapper.selectByPrimaryKey") ),
			@Result(column = "teacher_id", property = "teacher", one = @One(select = "com.jefflee.mapper.information.TeacherMapper.selectByPrimaryKey") ),
			@Result(column = "period_num", property = "periodNum") })
	public List<Plan> selectDetailListByScheduleId(Integer scheduleId);

	@Select("SELECT * FROM schd_plan WHERE course_id = #{courseId} AND schedule_id = #{scheduleId} GROUP BY teacher_id")
	@Results({ @Result(id = true, column = "plan_id", property = "planId") })
	public List<Plan> selectPlanListByScheduleIdAndCourseIdGradeByTeacherId(@Param("courseId") Integer courseId,
			@Param("scheduleId") Integer scheduleId);

}
