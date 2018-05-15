package com.jefflee.service.information.impl;

import com.jefflee.entity.information.Student;
import com.jefflee.mapper.information.StudentMapper;
import com.jefflee.service.information.StudentService;
import com.jefflee.util.ExcelUtil;
import com.jefflee.util.StringUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Created by TGL on 2018/5/15.
 */
@Service("studentService")
public class StudentServiceImpl implements StudentService{

    @Resource(name = "studentMapper")
    StudentMapper studentMapper;

    @Override
    public Integer insert(Student student) {
        if (studentMapper.insert(student) == 1){
            return student.getId();
        } else {
            return null;
        }
    }

    @Override
    public List<Student> selectList() {
        return studentMapper.selectAll();
    }

    @Override
    public Student selectById(Integer id) {
        return studentMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateById(Student student) {
        if (studentMapper.updateByPrimaryKey(student) == 1) {
            return student.getId();
        } else {
            return null;
        }
    }

    @Override
    public Integer deleteById(Integer id) {
        if (studentMapper.deleteByPrimaryKey(id) == 1) {
            return id;
        } else {
            return null;
        }
    }

    @Override
    public List<Student> selectListByGradeId(Integer gradeId) {
        return null;
    }

    @Override
    public Map<String, Object> importExcel(MultipartFile file) throws Exception {
//        tempPath = "C:\\bj161cas\\";
//        File tempDir = File.createTempFile()
//        // 创建目录
//        if (!tempDir.exists()) {
//            tempDir.mkdirs();
//        }
//        // 新建临时文件
//        File tempFile = null;
//        // 初始化输入流
//        String tempFileName = new Date().getTime() + ".xlsx";
        String name = file.getOriginalFilename();
        String suffix = name.substring(name.lastIndexOf("."));
        File tempFile = File.createTempFile(String.valueOf(new Date().getTime()), suffix);
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

        Map<Integer, Student> allStudentMap = new HashMap<>();
        List<Student> allStudentList = selectList();
        for (Student student : allStudentList) {
            allStudentMap.put(student.getStudent_no(), student);
        }
        List<Student> importedStudentList = new ArrayList<>();

        // 循环Excel行
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            Student student = new Student();
            for (Cell cell : row) {
                Object cellValue = ExcelUtil.getValue(cell);
                switch (cell.getColumnIndex()) {
                    case 0:
                        student.setStudent_no(Integer.valueOf(StringUtil.aviodNumericValue(cellValue)));
                        break;
                    case 1:
                        student.setName(cellValue.toString());
                        break;
                    case 2:
                        if (cellValue.toString().equals("男"))
                            student.setSex(0);
                        else if (cellValue.toString().equals("女"))
                            student.setSex(1);
                        else student.setSex(2);
                        break;
                    case 3:
                        student.setBegin_year(Integer.valueOf(StringUtil.aviodNumericValue(cellValue)));
                        break;
                    case 4:
                        student.setOffset(Integer.valueOf(StringUtil.aviodNumericValue(cellValue)));
                        break;
                    default:
                        break;
                }
            }
            Integer student_no = student.getStudent_no();
            if (!allStudentMap.containsKey(student_no)) {
                importedStudentList.add(student);
                allStudentMap.put(student_no, student);
            }
        }

        // 写入数据库
        int successNum = -1;
        Integer id = 0;
        if (!importedStudentList.isEmpty()) {
            for (Student student : importedStudentList) {
                id = insert(student);
                System.out.println(id);
                successNum++;
            }
        } else {
            successNum = 0;
        }
        result.put("successNum", successNum);
        return result;
    }

}
