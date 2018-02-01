package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.po.schedule.RelationPo;

public interface RelationService {

	public Integer insert(RelationPo relationPo);

	public List<RelationPo> selectAll();

	public RelationPo selectById(Integer relationId);

	public Integer updateById(RelationPo relationPo);

	public Integer deleteById(Integer relationId);

	public List<RelationPo> selectByScheduleId(Integer scheduleId);

	public List<RelationPo> selectByCombinedId(RelationPo relationPo);

}
