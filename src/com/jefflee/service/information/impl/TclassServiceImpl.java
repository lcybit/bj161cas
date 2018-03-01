package com.jefflee.service.information.impl;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Tclass;
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

	@Override
	public String gnrName(Tclass tclass) {
		StringBuilder name = new StringBuilder();
		switch (tclass.level) {
		case 0:
			name.append("初");
			break;
		case 1:
			name.append("高");
			break;
		default:
			break;
		}
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		if (month < 9) {
			year -= 1;
		}
		switch (year - tclass.year) {
		case 0:
			name.append("一");
			break;
		case 1:
			name.append("二");
			break;
		case 2:
			name.append("三");
			break;
		default:
			name.insert(0, tclass.year + "级");
			name.append("中");
			break;
		}
		name.append(tclass.tclassNo);
		name.append("班");
		return name.toString();
	}

	@Override
	public String gnrShortName(Tclass tclass) {
		StringBuilder name = new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		if (month < 9) {
			year -= 1;
		}
		switch (year - tclass.year) {
		case 0:
			name.append("-");
			break;
		case 1:
			name.append("=");
			break;
		case 2:
			name.append("三");
			break;
		default:
			name.append("?");
			break;
		}
		name.append(tclass.tclassNo);
		return name.toString();
	}
}
