package com.jefflee.mapper.information;

import org.springframework.stereotype.Repository;

import com.jefflee.entity.information.Tclass;
import com.jefflee.util.MyMapper;

@Repository("tclassMapper")
public interface TclassMapper extends MyMapper<Tclass> {

}
