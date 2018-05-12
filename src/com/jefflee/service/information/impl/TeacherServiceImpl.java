package com.jefflee.service.information.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jefflee.entity.information.Teacher;
import com.jefflee.mapper.information.TeacherMapper;
import com.jefflee.service.information.TeacherService;
import com.jefflee.util.ExcelUtil;
import com.jefflee.util.StringUtil;

@Service("teacherService")
public class TeacherServiceImpl implements TeacherService {

	@Resource(name = "teacherMapper")
	private TeacherMapper teacherMapper;

	private String tempPath;

	@Override
	public Integer insert(Teacher teacher) {
		if (teacherMapper.insert(teacher) == 1) {
			return teacher.getTeacherId();
		} else {
			return null;
		}
	}

	@Override
	public List<Teacher> selectList() {
		return teacherMapper.selectList();
	}

	@Override
	public Teacher selectById(Integer teacherId) {
		return teacherMapper.selectByPrimaryKey(teacherId);
	}

	@Override
	public Integer updateById(Teacher teacher) {
		if (teacherMapper.updateByPrimaryKey(teacher) == 1) {
			return teacher.getTeacherId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer teacherId) {
		if (teacherMapper.deleteByPrimaryKey(teacherId) == 1) {
			return teacherId;
		} else {
			return null;
		}
	}

	@Override
	public List<Teacher> selectListByGradeId(Integer gradeId) {
		return teacherMapper.selectListByGradeId(gradeId);
	}

	@Override
	public Map<String, Object> importExcel(MultipartFile file) throws Exception {
		tempPath = "C:\\bj161cas\\";
		File tempDir = new File(tempPath);
		// 创建目录
		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}
		// 新建临时文件
		File tempFile = null;
		// 初始化输入流
		String tempFileName = new Date().getTime() + ".xlsx";
		tempFile = new File(tempPath + tempFileName);
		// 将上传的文件写入临时文件中
		file.transferTo(tempFile);
		// 根据临时文件实例化输入流
		FileInputStream fileInputStream = new FileInputStream(tempFile);
		Workbook workbook = WorkbookFactory.create(fileInputStream);
		try {
			// 根据excel里面的内容读取知识库信息
			return readExcel(workbook);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			fileInputStream.close();
			// 删除临时文件
			if (tempFile.exists()) {
				tempFile.delete();
			}
		}

	}

	private Map<String, Object> readExcel(Workbook workbook) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 得到第一个sheet
		Sheet sheet = workbook.getSheetAt(0);

		Map<String, Teacher> allTeacherMap = new HashMap<String, Teacher>();
		List<Teacher> allTeacherList = selectList();
		for (Teacher teacher : allTeacherList) {
			allTeacherMap.put(teacher.getTeacherNo(), teacher);
		}
		List<Teacher> importedTeacherList = new ArrayList<Teacher>();

		// 循环Excel行
		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				continue;
			}
			Teacher teacher = new Teacher();
			for (Cell cell : row) {
				Object cellValue = ExcelUtil.getValue(cell);
				switch (cell.getColumnIndex()) {
				case 0:
					teacher.setTeacherNo(StringUtil.aviodNumericValue(cellValue));
					break;
				case 1:
					teacher.setName(cellValue.toString());
					break;
				default:
					break;
				}
			}
			teacher.setType(0);
			String teacherNo = teacher.getTeacherNo();
			if (!allTeacherMap.containsKey(teacherNo)) {
				importedTeacherList.add(teacher);
				allTeacherMap.put(teacherNo, teacher);
			}
		}

		// 写入数据库
		int successNum = -1;
		Integer teacherid = 0;
		if (!importedTeacherList.isEmpty()) {
			for (Teacher teacher : importedTeacherList) {
				teacherid = insert(teacher);
				System.out.println(teacherid);
				successNum++;
			}
		} else {
			successNum = 0;
		}
		result.put("successNum", successNum);
		return result;
	}

}
