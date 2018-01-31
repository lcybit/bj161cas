package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.schedule.ArrangementDto;
import com.jefflee.mapper.schedule.ArrangementMapper;
import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.util.BeanUtil;
import com.jefflee.util.DatabaseUtil;

@Service("arrangementService")
public class ArrangementServiceImpl implements ArrangementService {

	@Resource(name = "arrangementMapper")
	private ArrangementMapper arrangementMapper;

	@Override
	public String create(ArrangementDto arrangementDto) {
		ArrangementPo arrangementPo = new ArrangementPo();
		arrangementDto.setArrangementId(DatabaseUtil.gnr32Uuid());
		BeanUtil.copyProperties(arrangementDto, arrangementPo);
		if (arrangementMapper.insert(arrangementPo) == 1) {
			return arrangementPo.getArrangementId();
		} else {
			return null;
		}
	}

	@Override
	public List<ArrangementDto> listAll() {
		List<ArrangementPo> arrangementList = arrangementMapper.selectAll();
		List<ArrangementDto> arrangementDtoList = new ArrayList<ArrangementDto>();
		for (ArrangementPo arrangementPo : arrangementList) {
			ArrangementDto arrangementDto = new ArrangementDto();
			BeanUtil.copyPropertiesSelective(arrangementPo, arrangementDto);
			arrangementDtoList.add(arrangementDto);
		}
		return arrangementDtoList;
	}

	@Override
	public ArrangementDto findById(Integer arrangementId) {
		ArrangementDto arrangementDto = new ArrangementDto();
		ArrangementPo arrangementPo = arrangementMapper.selectByPrimaryKey(arrangementId);
		BeanUtil.copyProperties(arrangementPo, arrangementDto);
		return arrangementDto;
	}

	@Override
	public String modify(ArrangementDto arrangementDto) {
		ArrangementPo arrangementPo = new ArrangementPo();
		BeanUtil.copyProperties(arrangementDto, arrangementPo);
		if (arrangementMapper.updateByPrimaryKey(arrangementPo) == 1) {
			return arrangementPo.getArrangementId();
		} else {
			return null;
		}
	}

	@Override
	public String delete(String arrangementId) {
		if (arrangementMapper.deleteByPrimaryKey(arrangementId) == 1) {
			return arrangementId;
		} else {
			return null;
		}
	}
}
