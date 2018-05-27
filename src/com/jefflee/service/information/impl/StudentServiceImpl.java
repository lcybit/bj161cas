package com.jefflee.service.information.impl;

import com.jefflee.entity.information.Student;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.relation.StudentTClass;
import com.jefflee.mapper.information.StudentMapper;
import com.jefflee.mapper.information.TclassMapper;
import com.jefflee.mapper.relation.StudentTClassMapper;
import com.jefflee.service.information.StudentService;
import com.jefflee.util.ExcelUtil;
import com.jefflee.util.StringUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

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

    @Resource(name = "studentTClassMapper")
    StudentTClassMapper studentTClassMapper;

    @Resource(name = "tclassMapper")
    TclassMapper tclassMapper;

    @Override
    public Integer insert(Student student) {
        //cardNo表示的是机读卡号
        String cardNo = student.getCardNo();
        Integer tclassNo = Integer.valueOf(cardNo.substring(4,6));
        Integer year = Integer.valueOf(cardNo.substring(0, 4));
        student.setBegin_year(year);
        student.setStudent_no(Integer.valueOf(cardNo.substring(6)));
        Example example = new Example(Tclass.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("year", year);
        criteria.andEqualTo("tclassNo", tclassNo);
        Tclass tclass = tclassMapper.selectOneByExample(example);
        StudentTClass studentTClass = new StudentTClass();
        studentTClass.setTclassId(tclass.getTclassId());

        if (studentMapper.insert(student) == 1){
            studentTClass.setStudentId(student.getId());
            studentTClassMapper.insert(studentTClass);
            return student.getId();
        } else {
            return null;
        }
    }

    @Override
    public List<Student> selectList() {
        List<Student> studentList = studentMapper.selectAll();
        for (Student student : studentList){
            Example example = new Example(StudentTClass.class);
            example.createCriteria().andEqualTo("studentId", student.getId());
            StudentTClass studentTClass = studentTClassMapper.selectOneByExample(example);
            Tclass tclass = tclassMapper.selectByPrimaryKey(studentTClass.getTclassId());
            student.setTClassName(tclass.getName());
        }
        return studentList;
    }

    @Override
    public Student selectById(Integer id) throws Exception{
        Student student =  studentMapper.selectByPrimaryKey(id);
        if(student == null)
            throw new Exception("没有该学生");
        StudentTClass studentTClass = new StudentTClass();
        studentTClass.setStudentId(student.getId());
        studentTClass = studentTClassMapper.selectOne(studentTClass);
        Tclass tclass = tclassMapper.selectByPrimaryKey(studentTClass.getTclassId());
        student.setTClassNo(tclass.getTclassNo());
        return student;
    }

    @Override
    public Integer updateById(Student student) {
        //cardNo表示的是机读卡号
        String cardNo = student.getCardNo();
        Integer tclassNo = Integer.valueOf(cardNo.substring(4,6));
        Integer year = Integer.valueOf(cardNo.substring(0, 4));
        student.setBegin_year(year);
        student.setStudent_no(Integer.valueOf(cardNo.substring(6)));
        Example example = new Example(Tclass.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("year", year);
        criteria.andEqualTo("tclassNo", tclassNo);
        Tclass tclass = tclassMapper.selectOneByExample(example);
        StudentTClass studentTClass = new StudentTClass();
        studentTClass.setStudentId(student.getId());
        studentTClass = studentTClassMapper.selectOne(studentTClass);
        if (studentTClass.getTclassId() != tclass.getTclassId()){
            studentTClass.setTclassId(tclass.getTclassId());
        }
        studentTClassMapper.updateByPrimaryKey(studentTClass);
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
    public List<Student> selectListByTClassId(Integer tClassId) {
        Example example = new Example(StudentTClass.class);
        example.createCriteria().andEqualTo("tclassId", tClassId);
        List<StudentTClass> studentTClassList = studentTClassMapper.selectByExample(tClassId);
        List<Student> studentList = new ArrayList<>();
        for (StudentTClass studentTClass : studentTClassList){
            Student s = studentMapper.selectByPrimaryKey(studentTClass.getStudentId());
            studentList.add(s);
        }

        return studentList;
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
        List<Student> allStudentList = studentMapper.selectAll();
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
                        String cardNo = StringUtil.aviodNumericValue(cellValue);
                        int year = Integer.valueOf(cardNo.substring(0, 4));
                        int tclassNo = Integer.valueOf(cardNo.substring(4, 6));
                        student.setBegin_year(year);
                        student.setStudent_no(Integer.valueOf(cardNo.substring(6)));
                        Example example = new Example(Tclass.class);
                        Example.Criteria criteria = example.createCriteria();
                        criteria.andEqualTo("year", year);
                        criteria.andEqualTo("tclassNo", tclassNo);
                        Tclass tclass = tclassMapper.selectOneByExample(example);
                        student.setTClassId(tclass.getTclassId());
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
        List<StudentTClass> studentTClassList = new ArrayList<>();
        if (!importedStudentList.isEmpty()) {
            for (Student student : importedStudentList) {
                Integer id = insert(student);
                StudentTClass studentTClass = new StudentTClass();
                studentTClass.setStudentId(id);
                studentTClass.setTclassId(student.getTClassId());
                studentTClassList.add(studentTClass);
                studentTClassMapper.insert(studentTClass);
                successNum++;
            }

        } else {
            successNum = 0;
        }
        result.put("successNum", successNum);
        return result;
    }

}
