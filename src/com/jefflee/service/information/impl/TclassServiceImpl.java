package com.jefflee.service.information.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.mapper.information.TclassMapper;
import com.jefflee.po.information.TclassPo;
import com.jefflee.service.information.TclassService;

@Service("tclassService")
public class TclassServiceImpl implements TclassService {

	@Resource(name = "tclassMapper")
	private TclassMapper tclassMapper;

	@Override
	public Integer insert(TclassPo tclassPo) {
		if (tclassMapper.insert(tclassPo) == 1) {
			return tclassPo.getTclassId();
		} else {
			return null;
		}
	}

	@Override
	public List<TclassPo> selectAll() {
		return tclassMapper.selectAll();
	}

	@Override
	public TclassPo selectById(Integer tclassId) {
		return tclassMapper.selectByPrimaryKey(tclassId);
	}

	@Override
	public Integer updateById(TclassPo tclassPo) {
		if (tclassMapper.updateByPrimaryKey(tclassPo) == 1) {
			return tclassPo.getTclassId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer tclassId) {
		if (tclassMapper.deleteByPrimaryKey(tclassId) == 1) {
			return tclassId;
		} else {
			return null;
		}
	}
}
