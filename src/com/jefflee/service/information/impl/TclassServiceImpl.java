package com.jefflee.service.information.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Tclass;
import com.jefflee.mapper.information.TclassMapper;
import com.jefflee.service.information.TclassService;

@Service("tclassService")
public class TclassServiceImpl implements TclassService {

	@Resource(name = "tclassMapper")
	private TclassMapper tclassMapper;

	@Override
	public Integer insert(Tclass tclass) {
		if (tclassMapper.insert(tclass) == 1) {
			return tclass.getTclassId();
		} else {
			return null;
		}
	}

	@Override
	public List<Tclass> selectList() {
		return tclassMapper.selectAll();
	}

	@Override
	public List<Tclass> selectListByYearAndLevel(Integer year, Integer level) {
		Tclass tclass = new Tclass();
		tclass.setYear(year);
		tclass.setLevel(level);
		List<Tclass> tclassList = tclassMapper.select(tclass);
		return tclassList;
	}

	@Override
	public Tclass selectById(Integer tclassId) {
		return tclassMapper.selectByPrimaryKey(tclassId);
	}

	@Override
	public Integer updateById(Tclass tclass) {
		if (tclassMapper.updateByPrimaryKey(tclass) == 1) {
			return tclass.getTclassId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer tclassId) {
		if (tclassMapper.deleteByPrimaryKey(tclassId) == 1) {
			return tclassId;
		} else {
			return null;
		}
	}

}
