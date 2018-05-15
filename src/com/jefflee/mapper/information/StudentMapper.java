package com.jefflee.mapper.information;

import com.jefflee.entity.information.Student;
import com.jefflee.util.MyMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by TGL on 2018/5/15.
 */
@Repository("studentMapper")
public interface StudentMapper extends MyMapper<Student> {

    @Select("select * from info_student order by student_no")
    @Results({
            @Result(id = true, column = "student_no", property = "student_no"),
            @Result(id = true, column = "begin_year", property = "begin_year")})
    public List<Student> selectAllWithOrder();
}
