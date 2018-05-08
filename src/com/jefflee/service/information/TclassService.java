package com.jefflee.service.information;

import java.util.List;

import com.jefflee.entity.information.Tclass;

public interface TclassService {

	Integer insert(Tclass tclass);

	List<Tclass> selectList();

	List<Tclass> selectListByYearAndLevel(Integer year, Integer level);

	Tclass selectById(Integer tclassId);

	Integer updateById(Tclass tclass);

	Integer deleteById(Integer tclassId);

}
