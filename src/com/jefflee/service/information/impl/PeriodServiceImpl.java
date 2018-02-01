package com.jefflee.service.information.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.mapper.information.PeriodMapper;
import com.jefflee.po.information.PeriodPo;
import com.jefflee.service.information.PeriodService;

@Service("periodService")
public class PeriodServiceImpl implements PeriodService {

	@Resource(name = "periodMapper")
	private PeriodMapper periodMapper;

	@Override
	public Integer insert(PeriodPo periodPo) {
		if (periodMapper.insert(periodPo) == 1) {
			return periodPo.getPeriodId();
		} else {
			return null;
		}
	}

	@Override
	public List<PeriodPo> selectAll() {
		return periodMapper.selectAll();
	}

	@Override
	public PeriodPo selectById(Integer periodId) {
		return periodMapper.selectByPrimaryKey(periodId);
	}

	@Override
	public Integer updateById(PeriodPo periodPo) {
		if (periodMapper.updateByPrimaryKey(periodPo) == 1) {
			return periodPo.getPeriodId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer periodId) {
		if (periodMapper.deleteByPrimaryKey(periodId) == 1) {
			return periodId;
		} else {
			return null;
		}
	}

	@Override
	public List<PeriodPo> select(PeriodPo periodPo) {
		return periodMapper.select(periodPo);
	}
}
