package com.jefflee.service.information;

import java.util.List;

import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.schedule.Grade;

public interface TclassService {

	Integer insert(Tclass tclass);

	List<Tclass> selectList();

	List<Tclass> selectListByGrade(Grade grade);

	Tclass selectById(Integer tclassId);

	Integer updateById(Tclass tclass);

	Integer deleteById(Integer tclassId);

}
