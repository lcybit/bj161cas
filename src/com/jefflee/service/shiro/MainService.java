package com.jefflee.service.shiro;


import com.jefflee.entity.shiro.TbAdmin;

import java.util.List;

public interface MainService {

	public Integer selAdminList();

	public Integer selStudentTotal();

	public Integer selTeacherTotal();

	public int selStudentCountByGender(int i);
}
