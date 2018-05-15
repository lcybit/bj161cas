package com.jefflee.mapper.shiro;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface MainMapper {
	@Select("select count(*) from info_student")
	Integer selStudentTotal();

	@Select("SELECT count(*) FROM info_teacher")
	Integer selTeacherTotal();
}
