package com.jefflee.service.score.impl;

import com.jefflee.entity.information.Student;
import com.jefflee.entity.information.Teacher;
import com.jefflee.entity.relation.StudentTClass;
import com.jefflee.entity.schedule.Plan;
import com.jefflee.entity.score.Exam;
import com.jefflee.entity.score.Score;
import com.jefflee.entity.score.SrCourse;
import com.jefflee.entity.shiro.TbAdmin;
import com.jefflee.mapper.information.StudentMapper;
import com.jefflee.mapper.information.TeacherMapper;
import com.jefflee.mapper.relation.StudentTClassMapper;
import com.jefflee.mapper.schedule.PlanMapper;
import com.jefflee.mapper.score.ScoreMapper;
import com.jefflee.mapper.score.SrCourseMapper;
import com.jefflee.service.score.ScoreService;
import com.jefflee.service.score.SrCourseService;
import com.jefflee.util.ExcelUtil;
import com.jefflee.util.StringUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
@Service("scoreService")
public class ScoreServiceImpl implements ScoreService{

    @Resource(name = "scoreMapper")
    ScoreMapper scoreMapper;

    @Resource(name = "studentMapper")
    StudentMapper studentMapper;

    @Resource(name = "srCourseService")
    SrCourseService srCourseService;

    @Resource(name = "planMapper")
    PlanMapper planMapper;

    @Resource(name = "teacherMapper")
    TeacherMapper teacherMapper;

    @Resource(name = "studentTClassMapper")
    StudentTClassMapper studentTClassMapper;


    @Override
    public Integer insert(Score score) {
        if (scoreMapper.insert(score) == 1)
            return score.getId();
        else return null;
    }

    @Override
    public List<Score> selectList(Integer examId) {
        Example example = new Example(Score.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("exam_id", examId);
        Subject subject= SecurityUtils.getSubject();
        TbAdmin tbAdmin = (TbAdmin) subject.getPrincipal();
        if (tbAdmin.getRoleId() == 5){
            Example e = new Example(Student.class);
            e.createCriteria().andEqualTo("student_no",  tbAdmin.getUsername());
            Student student = studentMapper.selectOneByExample(e);
            criteria.andEqualTo("student_id", student.getId());
        } else if (tbAdmin.getRoleId() == 4){
            Example e = new Example(Teacher.class);
            e.createCriteria().andEqualTo("teacherNo",  tbAdmin.getUsername());
            Teacher teacher = teacherMapper.selectOneByExample(e);
            Example e1 = new Example(Plan.class);
            e1.createCriteria().andEqualTo("teacherId", teacher.getTeacherId());
            List<Plan> plans = planMapper.selectByExample(e1);
            List<Integer> students = new ArrayList<>();
            for (Plan plan : plans){
                Example e2 = new Example(StudentTClass.class);
                e2.createCriteria().andEqualTo("tclassId", plan.getTclassId());
                List<StudentTClass> studentTClassList = studentTClassMapper.selectByExample(e2);
                for (StudentTClass studentTClass : studentTClassList){
                    students.add(studentTClass.getStudentId());
                }
            }
            criteria.andIn("student_id", students);
        }
        List<Score> scoreList = scoreMapper.selectByExample(example);
        return scoreList;
    }

    @Override
    public Score selectById(Integer id) {
        return scoreMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateById(Score score) {
        Example example = new Example(Score.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("exam_id", score.getExam_id());
        criteria.andEqualTo("sr_course_id", score.getSr_course_id());
        criteria.andEqualTo("student_id", score.getStudent_id());

        Score oldScore = scoreMapper.selectOneByExample(example);
        oldScore.setScore(score.getScore());

        if (scoreMapper.updateByPrimaryKey(oldScore) == 1)
            return oldScore.getId();
        else return null;
    }

    @Override
    public Integer deleteById(Integer id) {
        if (scoreMapper.deleteByPrimaryKey(id) == 1)
            return id;
        else return null;
    }

    @Override
    public Integer importExcel(MultipartFile file, String info) throws Exception{
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
            return readExcel(workbook, info);
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

    private Integer readExcel(Workbook workbook, String info) {
        String arg[] = info.split(",");
        Integer examId = Integer.valueOf(arg[0]);
        Integer courseId = Integer.valueOf(arg[1]);
        List<SrCourse> srCourses = srCourseService.selectListByCourseId(courseId);
        Map<String, Integer> srCourseMap = new HashMap<>();
        for (SrCourse srCourse : srCourses){
            srCourseMap.put(srCourse.getSr_course_name(), srCourse.getId());
        }

        // 得到第一个sheet
        Sheet sheet = workbook.getSheetAt(0);
        List<Score> importedScoreList = new ArrayList<>();

        // 循环Excel行
        //记录了录入成绩的excel按顺序的子课程id
        List<Integer> srCourseExcel = new ArrayList<>();
        for (Row row : sheet) {
            Integer student_id = -1111;
            for (Cell cell : row) {
                Score score = new Score();
                Object cellValue = ExcelUtil.getValue(cell);
                if (row.getRowNum() == 0){
                  if (srCourseMap.get(cellValue.toString()) != null){
                      srCourseExcel.add(srCourseMap.get(cellValue.toString()));
                  }
                  continue;
                }

                if (cell.getColumnIndex() == 0){
                    Integer student_no = Integer.valueOf(StringUtil.aviodNumericValue(cellValue));
                    Example example = new Example(Student.class);
                    example.createCriteria().andEqualTo("student_no", student_no);
                    Student student = studentMapper.selectOneByExample(example);
                    if (student != null)
                        student_id = student.getId();
                } else if (cell.getColumnIndex() <= srCourseExcel.size()){
                        score.setSr_course_id(srCourseExcel.get(cell.getColumnIndex()-1));
                        score.setScore(Float.valueOf(cellValue.toString()));
                        if (student_id >= 0)
                            score.setStudent_id(student_id);
                        score.setExam_id(examId);
                        importedScoreList.add(score);
                }
            }
        }

        // 写入数据库
        int successNum = -1;
        if (!importedScoreList.isEmpty()) {
            for (Score score: importedScoreList) {
                insert(score);
                successNum++;
            }
        } else {
            successNum = 0;
        }
        return successNum;
    }
}
