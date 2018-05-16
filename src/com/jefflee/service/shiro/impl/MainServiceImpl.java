package com.jefflee.service.shiro.impl;

import com.jefflee.entity.information.Student;
import com.jefflee.entity.shiro.TbAdminExample;
import com.jefflee.mapper.information.StudentMapper;
import com.jefflee.mapper.shiro.MainMapper;
import com.jefflee.mapper.shiro.TbAdminMapper;
import com.jefflee.service.shiro.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MainServiceImpl implements MainService {
	
	@Autowired
	private TbAdminMapper tbAdminMapper;
	
	@Autowired
	private MainMapper mainMapper;

	@Resource(name = "studentMapper")
	StudentMapper studentMapper;

	@Override
	public Integer selAdminList() {
		TbAdminExample example=new TbAdminExample();
		return tbAdminMapper.selectByExample(example).size();
	}
	
	@Override
	public Integer selStudentTotal() {
		return mainMapper.selStudentTotal();
	}

	@Override
	public Integer selTeacherTotal() {
		return mainMapper.selTeacherTotal();
	}

	@Override
	public int selStudentCountByGender(int i) {
		Example example = new Example(Student.class);
		example.createCriteria().andEqualTo("sex", i);
		List<Student> list = studentMapper.selectByExample(example);
		return list.size();
	}

}
