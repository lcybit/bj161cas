package com.jefflee.service.schedule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.schedule.Adjustment;
import com.jefflee.mapper.schedule.AdjustmentMapper;
import com.jefflee.service.schedule.AdjustmentService;

@Service("adjustmentService")
public class AdjustmentServiceImpl implements AdjustmentService {

	@Resource(name = "adjustmentMapper")
	private AdjustmentMapper adjustmentMapper;

	@Override
	public Integer insert(Adjustment adjustment) {
		adjustmentMapper.insert(adjustment);
		return adjustment.getAdjustmentId();
	}

	@Override
	public Adjustment selectById(Integer adjustmentId) {
		return adjustmentMapper.selectByPrimaryKey(adjustmentId);
	}

	@Override
	public List<Adjustment> selectList() {
		return adjustmentMapper.selectAll();
	}

	@Override
	public int updateById(Adjustment adjustment) {
		return adjustmentMapper.updateByPrimaryKey(adjustment);
	}

	@Override
	public int deleteById(Integer adjustmentId) {
		return adjustmentMapper.deleteByPrimaryKey(adjustmentId);
	}

	@Override
	public List<Adjustment> selectListByScheduleId(Integer scheduleId) {
		Adjustment adjustment = new Adjustment();
		adjustment.setScheduleId(scheduleId);
		return adjustmentMapper.select(adjustment);
	}

	@Override
	public List<Adjustment> selectTempListByScheduleId(Integer scheduleId) {
		Adjustment adjustment = new Adjustment();
		adjustment.setScheduleId(scheduleId);
		adjustment.setDuration(0);
		return adjustmentMapper.select(adjustment);
	}

	@Override
	public Adjustment selectLatest(Integer scheduleId) {
		// 待改进，可只查询最后一个
		List<Adjustment> adjustmentList = selectTempListByScheduleId(scheduleId);
		Adjustment adjustment = adjustmentList.get(adjustmentList.size() - 1);
		return adjustment;
	}

}
