package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.schedule.RelationDto;
import com.jefflee.entity.schedule.Relation;
import com.jefflee.mapper.schedule.RelationMapper;
import com.jefflee.po.schedule.RelationPo;
import com.jefflee.service.schedule.RelationService;
import com.jefflee.util.BeanUtil;

@Service("relationService")
public class RelationServiceImpl implements RelationService {

	@Resource(name = "relationMapper")
	private RelationMapper relationMapper;

	@Override
	public Integer create(RelationDto relationDto) {
		RelationPo relationPo = new RelationPo();
		BeanUtil.copyProperties(relationDto, relationPo);
		if (relationMapper.insert(relationPo) == 1) {
			return relationPo.getRelationId();
		} else {
			return null;
		}
	}

	@Override
	public List<RelationDto> listAll() {
		List<RelationPo> relationList = relationMapper.selectAll();
		List<RelationDto> relationDtoList = new ArrayList<RelationDto>();
		for (RelationPo relationPo : relationList) {
			RelationDto relationDto = new RelationDto();
			BeanUtil.copyPropertiesSelective(relationPo, relationDto);
			relationDtoList.add(relationDto);
		}
		return relationDtoList;
	}

	@Override
	public RelationDto findById(Integer relationId) {
		RelationDto relationDto = new RelationDto();
		RelationPo relationPo = relationMapper.selectByPrimaryKey(relationId);
		BeanUtil.copyProperties(relationPo, relationDto);
		return relationDto;
	}

	@Override
	public Integer modify(RelationDto relationDto) {
		RelationPo relationPo = new RelationPo();
		BeanUtil.copyProperties(relationDto, relationPo);
		if (relationMapper.updateByPrimaryKey(relationPo) == 1) {
			return relationPo.getRelationId();
		} else {
			return null;
		}
	}

	@Override
	public Integer delete(Integer relationId) {
		if (relationMapper.deleteByPrimaryKey(relationId) == 1) {
			return relationId;
		} else {
			return null;
		}
	}

	@Override
	public List<Relation> findByScheduleId(Integer scheduleId) {
		List<Relation> relationList = new ArrayList<Relation>();
		Relation relation = new Relation();
		RelationPo queryRelationPo = new RelationPo();
		queryRelationPo.setScheduleId(scheduleId);
		List<RelationPo> relationPoList = relationMapper.select(queryRelationPo);
		for (RelationPo relationPo : relationPoList) {
			BeanUtil.copyProperties(relationPo, relation);
			relationPoList.add(relationPo);
		}
		return relationList;
	}
}
