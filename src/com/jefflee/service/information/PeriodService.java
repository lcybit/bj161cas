package com.jefflee.service.information;

import java.util.List;

import com.jefflee.po.information.PeriodPo;

public interface PeriodService {

	public Integer insert(PeriodPo periodPo);

	public List<PeriodPo> selectAll();

	public PeriodPo selectById(Integer periodId);

	public Integer updateById(PeriodPo periodPo);

	public Integer deleteById(Integer periodId);

	public List<PeriodPo> select(PeriodPo queryPeriodPo);

	public PeriodPo selectByOrder(Integer dayOfWeek, Integer orderOfDay);

}
