package com.jefflee.service.information.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.information.TclassDto;
import com.jefflee.mapper.information.TclassMapper;
import com.jefflee.po.information.TclassPo;
import com.jefflee.service.information.TclassService;
import com.jefflee.util.BeanUtil;

@Service("tclassService")
public class TclassServiceImpl implements TclassService {

	@Resource(name = "tclassMapper")
	private TclassMapper tclassMapper;

	@Override
	public Integer create(TclassDto tclassDto) {
		TclassPo tclassPo = new TclassPo();
		BeanUtil.copyProperties(tclassDto, tclassPo);
		if (tclassMapper.insert(tclassPo) == 1) {
			return tclassPo.getTclassId();
		} else {
			return null;
		}
	}

	@Override
	public List<TclassDto> listAll() {
		List<TclassPo> tclassList = tclassMapper.selectAll();
		List<TclassDto> tclassDtoList = new ArrayList<TclassDto>();
		for (TclassPo tclassPo : tclassList) {
			TclassDto tclassDto = new TclassDto();
			BeanUtil.copyPropertiesSelective(tclassPo, tclassDto);
			tclassDtoList.add(tclassDto);
		}
		return tclassDtoList;
	}

	@Override
	public TclassDto findById(Integer tclassId) {
		TclassDto tclassDto = new TclassDto();
		TclassPo tclassPo = tclassMapper.selectByPrimaryKey(tclassId);
		BeanUtil.copyProperties(tclassPo, tclassDto);
		return tclassDto;
	}

	@Override
	public Integer modify(TclassDto tclassDto) {
		TclassPo tclassPo = new TclassPo();
		BeanUtil.copyProperties(tclassDto, tclassPo);
		if (tclassMapper.updateByPrimaryKey(tclassPo) == 1) {
			return tclassPo.getTclassId();
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
