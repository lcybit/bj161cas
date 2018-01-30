package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.schedule.RelationDto;
import com.jefflee.entity.schedule.Relation;
import com.jefflee.mapper.schedule.RelationMapper;
import com.jefflee.service.schedule.RelationService;
import com.jefflee.util.BeanUtil;

import tk.mybatis.mapper.entity.Example;

@Service("relationService")
public class RelationServiceImpl implements RelationService {

	@Resource(name = "relationMapper")
	private RelationMapper relationMapper;

	@Override
	public Integer create(RelationDto relationDto) {
		Relation relation = new Relation();
		BeanUtil.copyProperties(relationDto, relation);
		if (relationMapper.insert(relation) == 1) {
			return relation.getRelationId();
		} else {
			return null;
		}
	}

	@Override
	public List<RelationDto> listAll() {
		Example example = new Example(Relation.class);
		example.setOrderByClause("relation_no ASC");
		List<Relation> relationList = relationMapper.selectByExample(example);
		List<RelationDto> relationDtoList = new ArrayList<RelationDto>();
		for (Relation relation : relationList) {
			RelationDto relationDto = new RelationDto();
			BeanUtil.copyPropertiesSelective(relation, relationDto);
			relationDtoList.add(relationDto);
		}
		return relationDtoList;
	}

	@Override
	public RelationDto findById(Integer relationId) {
		RelationDto relationDto = new RelationDto();
		Relation relation = relationMapper.selectByPrimaryKey(relationId);
		BeanUtil.copyProperties(relation, relationDto);
		return relationDto;
	}

	@Override
	public Integer modify(RelationDto relationDto) {
		Relation relation = new Relation();
		BeanUtil.copyProperties(relationDto, relation);
		if (relationMapper.updateByPrimaryKey(relation) == 1) {
			return relation.getRelationId();
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
}
