package com.jefflee.service.schedule.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.mapper.schedule.RelationMapper;
import com.jefflee.po.schedule.RelationPo;
import com.jefflee.service.schedule.RelationService;

@Service("relationService")
public class RelationServiceImpl implements RelationService {

	@Resource(name = "relationMapper")
	private RelationMapper relationMapper;

	@Override
	public Integer insert(RelationPo relationPo) {
		if (relationMapper.insert(relationPo) == 1) {
			return relationPo.getRelationId();
		} else {
			return null;
		}
	}

	@Override
	public List<RelationPo> selectAll() {
		return relationMapper.selectAll();
	}

	@Override
	public RelationPo selectById(Integer relationId) {
		return relationMapper.selectByPrimaryKey(relationId);
	}

	@Override
	public Integer updateById(RelationPo relationPo) {
		if (relationMapper.updateByPrimaryKey(relationPo) == 1) {
			return relationPo.getRelationId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer relationId) {
		if (relationMapper.deleteByPrimaryKey(relationId) == 1) {
			return relationId;
		} else {
			return null;
		}
	}

	@Override
	public List<RelationPo> selectByScheduleId(Integer scheduleId) {
		RelationPo queryRelationPo = new RelationPo();
		queryRelationPo.setScheduleId(scheduleId);
		return relationMapper.select(queryRelationPo);
	}

	@Override
	public List<RelationPo> select(RelationPo relationPo) {
		return relationMapper.select(relationPo);
	}

}
