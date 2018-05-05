package com.jefflee.mapper.schedule;

import java.util.List;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.jefflee.entity.schedule.Arrangement;
import com.jefflee.util.MyMapper;

public interface ArrangementMapper extends MyMapper<Arrangement> {

	@Select("select * from schd_arrangement where arrangement_id = #{arrangementId}")
	@Results({ @Result(id = true, column = "arrangement_id", property = "arrangementId"),
			@Result(column = "schedule_id", property = "scheduleId"),
			@Result(column = "period_id", property = "periodId"), @Result(column = "course_id", property = "courseId"),
			@Result(column = "room_id", property = "roomId"), @Result(column = "tclass_id", property = "tclassId"),
			@Result(column = "teacher_id", property = "teacherId"),
			@Result(column = "period_id", property = "period", one = @One(select = "com.jefflee.mapper.information.PeriodMapper.selectByPrimaryKey") ),
			@Result(column = "course_id", property = "course", one = @One(select = "com.jefflee.mapper.information.CourseMapper.selectByPrimaryKey") ),
			@Result(column = "room_id", property = "room", one = @One(select = "com.jefflee.mapper.information.RoomMapper.selectByPrimaryKey") ),
			@Result(column = "tclass_id", property = "tclass", one = @One(select = "com.jefflee.mapper.information.TclassMapper.selectByPrimaryKey") ),
			@Result(column = "teacher_id", property = "teacher", one = @One(select = "com.jefflee.mapper.information.TeacherMapper.selectByPrimaryKey") ),
			@Result(column = "arranged", property = "arranged"), @Result(column = "priority", property = "priority") })
	public Arrangement selectDetailById(Integer arrangementId);

	@Select("select * from schd_arrangement where schedule_id = #{scheduleId}")
	@Results({ @Result(id = true, column = "arrangement_id", property = "arrangementId"),
			@Result(column = "schedule_id", property = "scheduleId"),
			@Result(column = "period_id", property = "periodId"), @Result(column = "course_id", property = "courseId"),
			@Result(column = "room_id", property = "roomId"), @Result(column = "tclass_id", property = "tclassId"),
			@Result(column = "teacher_id", property = "teacherId"),
			@Result(column = "period_id", property = "period", one = @One(select = "com.jefflee.mapper.information.PeriodMapper.selectByPrimaryKey") ),
			@Result(column = "course_id", property = "course", one = @One(select = "com.jefflee.mapper.information.CourseMapper.selectByPrimaryKey") ),
			@Result(column = "room_id", property = "room", one = @One(select = "com.jefflee.mapper.information.RoomMapper.selectByPrimaryKey") ),
			@Result(column = "tclass_id", property = "tclass", one = @One(select = "com.jefflee.mapper.information.TclassMapper.selectByPrimaryKey") ),
			@Result(column = "teacher_id", property = "teacher", one = @One(select = "com.jefflee.mapper.information.TeacherMapper.selectByPrimaryKey") ),
			@Result(column = "arranged", property = "arranged"), @Result(column = "priority", property = "priority") })
	public List<Arrangement> selectDetailListByScheduleId(Integer scheduleId);

}
