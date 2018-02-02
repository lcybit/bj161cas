package com.jefflee.mapper.schedule;

import java.util.List;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.jefflee.entity.schedule.Relation;
import com.jefflee.po.schedule.RelationPo;

import tk.mybatis.mapper.common.Mapper;

public interface RelationMapper extends Mapper<RelationPo> {

	@Select("select * from schd_relation where relation_id = #{relationId}")
	@Results({ @Result(id = true, column = "relation_id", property = "relationId"),
			@Result(column = "schedule_id", property = "schedule", one = @One(select = "com.jefflee.mapper.schedule.ScheduleMapper.selectEntityById") ),
			@Result(column = "course_id", property = "course", one = @One(select = "com.jefflee.mapper.information.CourseMapper.selectEntityById") ),
			@Result(column = "room_id", property = "room", one = @One(select = "com.jefflee.mapper.information.RoomMapper.selectEntityById") ),
			@Result(column = "tclass_id", property = "tclass", one = @One(select = "com.jefflee.mapper.information.TclassMapper.selectEntityById") ),
			@Result(column = "teacher_id", property = "teacher", one = @One(select = "com.jefflee.mapper.information.TeacherMapper.selectEntityById") ) })
	public Relation selectEntityById(Integer relationId);

	@Select("select * from schd_relation where schedule_id = #{scheduleId}")
	@Results({ @Result(id = true, column = "relation_id", property = "relationId"),
			@Result(column = "schedule_id", property = "schedule", one = @One(select = "com.jefflee.mapper.schedule.ScheduleMapper.selectEntityById") ),
			@Result(column = "course_id", property = "course", one = @One(select = "com.jefflee.mapper.information.CourseMapper.selectEntityById") ),
			@Result(column = "room_id", property = "room", one = @One(select = "com.jefflee.mapper.information.RoomMapper.selectEntityById") ),
			@Result(column = "tclass_id", property = "tclass", one = @One(select = "com.jefflee.mapper.information.TclassMapper.selectEntityById") ),
			@Result(column = "teacher_id", property = "teacher", one = @One(select = "com.jefflee.mapper.information.TeacherMapper.selectEntityById") ) })
	public List<Relation> selectEntityListByScheduleId(Integer scheduleId);
}
