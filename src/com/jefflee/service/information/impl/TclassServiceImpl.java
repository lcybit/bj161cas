package com.jefflee.service.information.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.information.TclassDto;
import com.jefflee.entity.information.Tclass;
import com.jefflee.mapper.information.TclassMapper;
import com.jefflee.service.information.TclassService;
import com.jefflee.util.BeanUtil;

import tk.mybatis.mapper.entity.Example;

@Service("tclassService")
public class TclassServiceImpl implements TclassService {

	@Resource(name = "tclassMapper")
	private TclassMapper tclassMapper;

	@Override
	public Integer create(TclassDto tclassDto) {
		Tclass tclass = new Tclass();
		BeanUtil.copyProperties(tclassDto, tclass);
		if (tclassMapper.insert(tclass) == 1) {
			return tclass.getTclassId();
		} else {
			return null;
		}
	}

	@Override
	public List<TclassDto> listAll() {
		Example example = new Example(Tclass.class);
		example.setOrderByClause("tclass_no ASC");
		List<Tclass> tclassList = tclassMapper.selectByExample(example);
		List<TclassDto> tclassDtoList = new ArrayList<TclassDto>();
		for (Tclass tclass : tclassList) {
			TclassDto tclassDto = new TclassDto();
			BeanUtil.copyPropertiesSelective(tclass, tclassDto);
			tclassDtoList.add(tclassDto);
		}
		return tclassDtoList;
	}

	@Override
	public TclassDto findById(Integer tclassId) {
		TclassDto tclassDto = new TclassDto();
		Tclass tclass = tclassMapper.selectByPrimaryKey(tclassId);
		BeanUtil.copyProperties(tclass, tclassDto);
		return tclassDto;
	}

	@Override
	public Integer modify(TclassDto tclassDto) {
		Tclass tclass = new Tclass();
		BeanUtil.copyProperties(tclassDto, tclass);
		if (tclassMapper.updateByPrimaryKey(tclass) == 1) {
			return tclass.getTclassId();
		} else {
			return null;
		}
	}

	@Override
	public Integer delete(Integer tclassId) {
		if (tclassMapper.deleteByPrimaryKey(tclassId) == 1) {
			return tclassId;
		} else {
			return null;
		}
	}
}
