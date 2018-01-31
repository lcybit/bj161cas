package com.jefflee.service.information.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.information.PeriodDto;
import com.jefflee.mapper.information.PeriodMapper;
import com.jefflee.po.information.PeriodPo;
import com.jefflee.service.information.PeriodService;
import com.jefflee.util.BeanUtil;

@Service("periodService")
public class PeriodServiceImpl implements PeriodService {

	@Resource(name = "periodMapper")
	private PeriodMapper periodMapper;

	@Override
	public Integer create(PeriodDto periodDto) {
		PeriodPo periodPo = new PeriodPo();
		BeanUtil.copyProperties(periodDto, periodPo);
		if (periodMapper.insert(periodPo) == 1) {
			return periodPo.getPeriodId();
		} else {
			return null;
		}
	}

	@Override
	public List<PeriodDto> listAll() {
		List<PeriodPo> periodList = periodMapper.selectAll();
		List<PeriodDto> periodDtoList = new ArrayList<PeriodDto>();
		for (PeriodPo periodPo : periodList) {
			PeriodDto periodDto = new PeriodDto();
			BeanUtil.copyPropertiesSelective(periodPo, periodDto);
			periodDtoList.add(periodDto);
		}
		return periodDtoList;
	}

	@Override
	public PeriodDto findById(Integer periodId) {
		PeriodDto periodDto = new PeriodDto();
		PeriodPo periodPo = periodMapper.selectByPrimaryKey(periodId);
		BeanUtil.copyProperties(periodPo, periodDto);
		return periodDto;
	}

	@Override
	public Integer modify(PeriodDto periodDto) {
		PeriodPo periodPo = new PeriodPo();
		BeanUtil.copyProperties(periodDto, periodPo);
		if (periodMapper.updateByPrimaryKey(periodPo) == 1) {
			return periodPo.getPeriodId();
		} else {
			return null;
		}
	}

	@Override
	public Integer delete(Integer periodId) {
		if (periodMapper.deleteByPrimaryKey(periodId) == 1) {
			return periodId;
		} else {
			return null;
		}
	}
}
