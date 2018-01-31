package com.jefflee.service.information.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.information.PeriodDto;
import com.jefflee.entity.information.Period;
import com.jefflee.mapper.information.PeriodMapper;
import com.jefflee.service.information.PeriodService;
import com.jefflee.util.BeanUtil;

@Service("periodService")
public class PeriodServiceImpl implements PeriodService {

	@Resource(name = "periodMapper")
	private PeriodMapper periodMapper;

	@Override
	public Integer create(PeriodDto periodDto) {
		Period period = new Period();
		BeanUtil.copyProperties(periodDto, period);
		if (periodMapper.insert(period) == 1) {
			return period.getPeriodId();
		} else {
			return null;
		}
	}

	@Override
	public List<PeriodDto> listAll() {
		List<Period> periodList = periodMapper.selectAll();
		List<PeriodDto> periodDtoList = new ArrayList<PeriodDto>();
		for (Period period : periodList) {
			PeriodDto periodDto = new PeriodDto();
			BeanUtil.copyPropertiesSelective(period, periodDto);
			periodDtoList.add(periodDto);
		}
		return periodDtoList;
	}

	@Override
	public PeriodDto findById(Integer periodId) {
		PeriodDto periodDto = new PeriodDto();
		Period period = periodMapper.selectByPrimaryKey(periodId);
		BeanUtil.copyProperties(period, periodDto);
		return periodDto;
	}

	@Override
	public Integer modify(PeriodDto periodDto) {
		Period period = new Period();
		BeanUtil.copyProperties(periodDto, period);
		if (periodMapper.updateByPrimaryKey(period) == 1) {
			return period.getPeriodId();
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
