package com.jefflee.mapper.information;

import com.jefflee.entity.information.Student;
import com.jefflee.util.MyMapper;
import org.springframework.stereotype.Repository;

/**
 * Created by TGL on 2018/5/15.
 */
@Repository("studentMapper")
public interface StudentMapper extends MyMapper<Student> {

}
