
package com.jefflee.service.relation;

import java.util.List;

import com.jefflee.entity.relation.GradeCourse;

public interface GradeCourseService {

	public Integer insert(GradeCourse gradeCourse);

	public List<GradeCourse> selectByGradeId(Integer gradeId);

	public void deleteByGradeId(Integer gradeId);

}
