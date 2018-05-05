package com.jefflee.mapper.schedule;

import java.util.List;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.jefflee.entity.schedule.Schedule;
import com.jefflee.util.MyMapper;

public interface ScheduleMapper extends MyMapper<Schedule> {

	@Select("SELECT * FROM schd_schedule WHERE schedule_id = #{scheduleId}")
	@Results({ @Result(id = true, column = "schedule_id", property = "scheduleId"),
			@Result(column = "grade_id", property = "grade", one = @One(select = "com.jefflee.mapper.schedule.GradeMapper.selectByPrimaryKey") ) })
	public Schedule selectDetailById(Integer scheduleId);

	@Select("SELECT * FROM schd_schedule")
	@Results({ @Result(id = true, column = "schedule_id", property = "scheduleId"),
			@Result(column = "grade_id", property = "gradeId"),
			@Result(column = "grade_id", property = "grade", one = @One(select = "com.jefflee.mapper.schedule.GradeMapper.selectByPrimaryKey") ) })
	public List<Schedule> selectDetailList();

	@Select("SELECT * FROM schd_schedule WHERE grade_id = #{gradeId}")
	@Results({ @Result(id = true, column = "schedule_id", property = "scheduleId"),
			@Result(column = "grade_id", property = "gradeId"),
			@Result(column = "grade_id", property = "grade", one = @One(select = "com.jefflee.mapper.schedule.GradeMapper.selectByPrimaryKey") ) })
	public List<Schedule> selectDetailListByGradeId(Integer gradeId);
}
