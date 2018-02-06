package com.jefflee.mapper.information;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.jefflee.entity.information.Tclass;
import com.jefflee.po.information.TclassPo;

import tk.mybatis.mapper.common.Mapper;

@Repository("tclassMapper")
public interface TclassMapper extends Mapper<TclassPo> {

	@Select("select * from info_tclass where tclass_id = #{tclassId}")
	@Results({ @Result(id = true, column = "tclass_id", property = "tclassId"),
			@Result(column = "tclass_no", property = "tclassNo"), @Result(column = "name", property = "name"),
			@Result(column = "type", property = "type"), @Result(column = "year", property = "year"),
			@Result(column = "level", property = "level") })
	public Tclass selectEntityById(Integer tclassId);

}
