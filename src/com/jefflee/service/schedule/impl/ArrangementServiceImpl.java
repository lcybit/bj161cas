package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.schedule.ArrangementDto;
import com.jefflee.entity.schedule.Arrangement;
import com.jefflee.mapper.schedule.ArrangementMapper;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.util.BeanUtil;
import com.jefflee.util.DatabaseUtil;

@Service("arrangementService")
public class ArrangementServiceImpl implements ArrangementService {

	@Resource(name = "arrangementMapper")
	private ArrangementMapper arrangementMapper;

	@Override
	public String create(ArrangementDto arrangementDto) {
		Arrangement arrangement = new Arrangement();
		arrangementDto.setArrangementId(DatabaseUtil.gnr32Uuid());
		BeanUtil.copyProperties(arrangementDto, arrangement);
		if (arrangementMapper.insert(arrangement) == 1) {
			return arrangement.getArrangementId();
		} else {
			return null;
		}
	}

	@Override
	public List<ArrangementDto> listAll() {
		List<Arrangement> arrangementList = arrangementMapper.selectAll();
		List<ArrangementDto> arrangementDtoList = new ArrayList<ArrangementDto>();
		for (Arrangement arrangement : arrangementList) {
			ArrangementDto arrangementDto = new ArrangementDto();
			BeanUtil.copyPropertiesSelective(arrangement, arrangementDto);
			arrangementDtoList.add(arrangementDto);
		}
		return arrangementDtoList;
	}

	@Override
	public ArrangementDto findById(Integer arrangementId) {
		ArrangementDto arrangementDto = new ArrangementDto();
		Arrangement arrangement = arrangementMapper.selectByPrimaryKey(arrangementId);
		BeanUtil.copyProperties(arrangement, arrangementDto);
		return arrangementDto;
	}

	@Override
	public String modify(ArrangementDto arrangementDto) {
		Arrangement arrangement = new Arrangement();
		BeanUtil.copyProperties(arrangementDto, arrangement);
		if (arrangementMapper.updateByPrimaryKey(arrangement) == 1) {
			return arrangement.getArrangementId();
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
