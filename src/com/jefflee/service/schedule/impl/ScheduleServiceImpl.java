package com.jefflee.service.schedule.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.schedule.Plan;
import com.jefflee.mapper.schedule.ScheduleMapper;
import com.jefflee.po.information.CoursePo;
import com.jefflee.po.information.PeriodPo;
import com.jefflee.po.relation.GroupCoursePo;
import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.po.schedule.GroupPo;
import com.jefflee.po.schedule.PlanPo;
import com.jefflee.po.schedule.SchedulePo;
import com.jefflee.service.information.CourseService;
import com.jefflee.service.information.PeriodService;
import com.jefflee.service.information.TclassService;
import com.jefflee.service.information.TeacherService;
import com.jefflee.service.relation.GroupCourseService;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.service.schedule.GroupService;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.service.schedule.ScheduleService;
import com.jefflee.view.CoursePlanView;
import com.jefflee.view.PlanView;
import com.jefflee.view.SchdPlanView;
import com.jefflee.view.ScheduleView;
import com.jefflee.view.WeekView;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	@Resource(name = "scheduleMapper")
	private ScheduleMapper scheduleMapper;

	@Resource(name = "planService")
	private PlanService planService;
	@Resource(name = "arrangementService")
	private ArrangementService arrangementService;

	@Resource(name = "periodService")
	private PeriodService periodService;
	@Resource(name = "courseService")
	private CourseService courseService;
	@Resource(name = "tclassService")
	private TclassService tclassService;
	@Resource(name = "teacherService")
	private TeacherService teacherService;
	
	@Resource(name = "groupService")
	GroupService groupService;

	@Resource(name = "groupCourseService")
	GroupCourseService groupCourseService;
	

	@Override
	public Integer insert(SchedulePo schedulePo) {
		if (scheduleMapper.insert(schedulePo) == 1) {
			return schedulePo.getScheduleId();
		} else {
			return null;
		}
	}

	@Override
	public List<SchedulePo> selectAll() {
		return scheduleMapper.selectAll();
	}

	@Override
	public SchedulePo selectById(Integer scheduleId) {
		return scheduleMapper.selectByPrimaryKey(scheduleId);
	}

	@Override
	public Integer updateById(SchedulePo schedulePo) {
		if (scheduleMapper.updateByPrimaryKey(schedulePo) == 1) {
			return schedulePo.getScheduleId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer scheduleId) {
		if (scheduleMapper.deleteByPrimaryKey(scheduleId) == 1) {
			return scheduleId;
		} else {
			return null;
		}
	}

	@Override
	public ScheduleView gnrScheduleView(Integer scheduleId) {
		return arrangementService.gnrScheduleView(scheduleMapper.selectEntityById(scheduleId));
	}
	
	//TODO 待完善 初始化planlist
	@Override
	public SchdPlanView gnrSchdPlanView(Integer groupId,Integer scheduleId){
		
		SchdPlanView schdPlanView = new SchdPlanView();
		
		//获取tclasslist
		//根据groupid获取相应的year ，再根据year从tclass表中获取该年级的多个班级
		GroupPo groupPo = groupService.selectById(groupId);
		Integer year = groupPo.getYear();
		List<Tclass> tclassList = new ArrayList<Tclass>();
		tclassList = tclassService.checkByYear(year);
		schdPlanView.setTclassList(tclassList);
		
		//获取coursePolist
		List<GroupCoursePo> groupCoursePoList = groupCourseService.selectById(groupId);
		List<CoursePo> coursePoList = new ArrayList<CoursePo>();
		//根据groupid得到 groupcoursePolist 遍历groupcoursePolist，根据courseid 得到coursePoList
		for(int i=0;i<groupCoursePoList.size();i++)
		{
			Integer courseId = null;
			courseId = groupCoursePoList.get(i).getCourseId();
			coursePoList.add(courseService.selectById(courseId));
		}
		//schdPlanView.setCoursePoList(coursePoList);
		
		//根据scheduleId获得planList
		List<Plan> planList = new ArrayList<Plan>();
		planList = planService.selectPlanListByScheduleId(scheduleId);
		//测试
		System.out.println("planlist的数量：");
	    System.out.println(planList.size());
		
		//根据coursePolist 、tclasslist 生成具有部分属性的coursePlanViewMap
		Map<String, CoursePlanView> coursePlanViewMap = new LinkedHashMap<String, CoursePlanView>();
		for(CoursePo coursePo:coursePoList)
		{
			CoursePlanView coursePlanView = new CoursePlanView();
			Map<String,Plan> paneMap = new LinkedHashMap<String, Plan>();
			
			coursePlanView.setCourse(coursePo);
			
			for(Tclass tclass:tclassList)
			{
				Plan plan = new Plan();
				plan.setTclass(tclass);
				paneMap.put(tclass.getTclassId().toString(), plan);
				/*
				//schd_plan表里没有一条scheduleId的数据时，插入x*y条数据到该表,这样 planlist 只有两种情况 1. null 2. planlist.size()=x*y
				if(planList==null)
				{
					Integer num;
					PlanPo planPo = new PlanPo();
					planPo.setScheduleId(scheduleId);
					planPo.setCourseId(coursePo.getCourseId());
					planPo.setTclassId(tclass.getTclassId());
					planPo.setPeriodNum(0);//默认值0
					num = planService.insert(planPo);		
				}
				*/
			}
			coursePlanView.setPaneMap(paneMap);
			coursePlanViewMap.put(coursePo.getCourseId().toString(), coursePlanView);
		}
		/*
		//重新获得planlist 
		if(planList==null)
		{
			planList = planService.selectPlanListByScheduleId(scheduleId);
		}
		*/
		//测试
		System.out.println("coursePlanViewMap的数量：");
	    System.out.println(coursePlanViewMap.size());
	    
	    int count=0;//测试  计数用的 最终count==planlist.size()
	    //定位 planList中的plan元素 即将用planList中的每一个plan的plan.getPeriodNum()、plan.getTeacher()填充coursePlanViewMap，使其完善
	    if(planList!=null)
	    {
	    	for(Plan plan:planList)
		    {
		    	count++;
		    	Integer courseId;
		    	Integer tclassId;
		    	CoursePlanView coursePlanView = new CoursePlanView();
		    	Map<String, Plan> paneMap = new LinkedHashMap<String,Plan>();
		    
		    	courseId = plan.getCourse().getCourseId();
		    	tclassId = plan.getTclass().getTclassId();
		    	//if(coursePlanViewMap.contains(courseId.toString())
		    	coursePlanView = coursePlanViewMap.get(courseId.toString());
		    	//判断coursePlanView是否存在，即   coursePlanViewMap是否包含courseId的元素
		    	if(coursePlanView!=null)
		    	{
		    		coursePlanView.setPeriodNum(plan.getPeriodNum());
		    		paneMap = coursePlanView.getPaneMap();
		    		paneMap.put(tclassId.toString(),plan);	    	
		    	}	
		    	
		    }
	    }
	    
	    schdPlanView.setCoursePlanViewMap(coursePlanViewMap);
	      
		return schdPlanView ;		
	}
	

	
	@Override
	public void gnrEmptyArrangementList(Integer scheduleId) {
		// TODO 根据课表上午、下午、晚上课时数生成课时列表
		List<PeriodPo> periodPoList = periodService.selectAll();
		List<PlanPo> planPoList = planService.selectAll();
		for (PlanPo planPo : planPoList) {
			for (PeriodPo periodPo : periodPoList) {
				ArrangementPo arrangementPo = new ArrangementPo();
				arrangementPo.setScheduleId(scheduleId);
				arrangementPo.setPeriodId(periodPo.getPeriodId());
				arrangementPo.setCourseId(planPo.getCourseId());
				arrangementPo.setRoomId(planPo.getRoomId());
				arrangementPo.setTclassId(planPo.getTclassId());
				arrangementPo.setTeacherId(planPo.getTeacherId());
				arrangementPo.setArranged(0);
				arrangementPo.setPriority(2);
				arrangementService.insert(arrangementPo);
			}
		}
	}

	@Override
	public void gnrSchedule(Integer scheduleId) {
		arrangementService.gnrArrangementList(scheduleId);
	}

	@Override
	public void gnrScheduleViewExcel(Integer scheduleId) throws FileNotFoundException, IOException {
		ScheduleView scheduleView = gnrScheduleView(scheduleId);
		List<WeekView> tclassWeekViewList = (List<WeekView>) scheduleView.getTclassWeekViewMap().values();
		List<WeekView> teacherWeekViewList = (List<WeekView>) scheduleView.getTeacherWeekViewMap().values();

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet tclassSheet = workbook.createSheet("班级课表");
		HSSFSheet teacherSheet = workbook.createSheet("教师课表");
		HSSFRow tclassRow = null;
		HSSFRow teacherRow = null;
		HSSFCell tclassCell = null;
		HSSFCell teacherCell = null;

		int tclassNum = tclassWeekViewList.size();
		int teacherNum = teacherWeekViewList.size();
		int tclassRowNum = tclassNum % 3 == 0 ? tclassNum / 3 : tclassNum / 3 + 1;
		int teacherRowNum = teacherNum % 3 == 0 ? teacherNum / 3 : teacherNum / 3 + 1;
		WeekView tclassWeekView = new WeekView();
		WeekView teacherWeekView = new WeekView();

		int blockRowIdx = -1;
		int blockColIdx = -1;
		int rowIdx = -1;
		int colIdx = -1;
		int weekViewIdx = -1;
		for (int i = 0; i < tclassRowNum * 9 - 1; i++) {
			blockRowIdx = i / 9;
			rowIdx = i % 9;
			tclassRow = tclassSheet.createRow(i);
			for (int j = 0; j < 29; j++) {
				blockColIdx = j / 10;
				colIdx = j % 10;
				weekViewIdx = blockRowIdx * 3 + blockColIdx;
				if (weekViewIdx >= tclassNum) {
					break;
				}
				tclassWeekView = tclassWeekViewList.get(weekViewIdx);
				tclassCell = tclassRow.createCell(j);
				if (rowIdx == 0) {
					if (colIdx == 0) {
						tclassCell.setCellValue(tclassWeekView.getTitleView().getTclassName());
						continue;
					}
					if (colIdx == 3) {
						tclassCell.setCellValue(tclassWeekView.getTitleView().getTeacherName());
						continue;
					}
					if (colIdx == 6) {
						tclassCell.setCellValue(tclassWeekView.getTitleView().getStartDate());
						continue;
					}
				}
				if (rowIdx == 1) {
					if (colIdx == 1) {
						tclassCell.setCellValue("上午");
						continue;
					}
					if (colIdx == 6) {
						tclassCell.setCellValue("下午");
						continue;
					}
				}
				if (rowIdx == 2) {
					if (colIdx == 1) {
						tclassCell.setCellValue("早");
						continue;
					} else if (colIdx >= 2 && colIdx <= 8) {
						tclassCell.setCellValue(String.valueOf(colIdx - 1));
						continue;
					}
				}
				if (rowIdx >= 3 && rowIdx <= 7) {
					if (colIdx == 0) {
						tclassCell.setCellValue(String.valueOf(rowIdx - 2));
						continue;
					} else if (colIdx >= 1 && colIdx <= 8) {
						tclassCell.setCellValue(
								tclassWeekView.getDayViewList().get(rowIdx - 3).getArrangedPeriodViewList()
										.get(colIdx - 1).getArrangementList().get(0).getCourse().getName());
						continue;
					}
				}
			}
		}
		for (int i = 0; i < teacherRowNum * 9 - 1; i++) {
			blockRowIdx = i / 9;
			rowIdx = i % 9;
			teacherRow = teacherSheet.createRow(i);
			for (int j = 0; j < 29; j++) {
				blockColIdx = j / 10;
				colIdx = j % 10;
				weekViewIdx = blockRowIdx * 3 + blockColIdx;
				if (weekViewIdx >= teacherNum) {
					break;
				}
				teacherWeekView = teacherWeekViewList.get(weekViewIdx);
				teacherCell = teacherRow.createCell(j);
				if (rowIdx == 0) {
					if (colIdx == 0) {
						teacherCell.setCellValue(teacherWeekView.getTitleView().getCourseName());
						continue;
					}
					if (colIdx == 1) {
						teacherCell.setCellValue(teacherWeekView.getTitleView().getGradeName());
					}
					if (colIdx == 3) {
						teacherCell.setCellValue(teacherWeekView.getTitleView().getTeacherName());
						continue;
					}
					if (colIdx == 6) {
						teacherCell.setCellValue(teacherWeekView.getTitleView().getStartDate());
						continue;
					}
				}
				if (rowIdx == 1) {
					if (colIdx == 1) {
						teacherCell.setCellValue("上午");
						continue;
					}
					if (colIdx == 6) {
						teacherCell.setCellValue("下午");
						continue;
					}
				}
				if (rowIdx == 2) {
					if (colIdx == 1) {
						teacherCell.setCellValue("早");
						continue;
					} else if (colIdx >= 2 && colIdx <= 8) {
						teacherCell.setCellValue(String.valueOf(colIdx - 1));
						continue;
					}
				}
				if (rowIdx >= 3 && rowIdx <= 7) {
					if (colIdx == 0) {
						teacherCell.setCellValue(String.valueOf(rowIdx - 2));
						continue;
					} else if (colIdx >= 1 && colIdx <= 8) {
						teacherCell.setCellValue(
								teacherWeekView.getDayViewList().get(rowIdx - 3).getArrangedPeriodViewList()
										.get(colIdx - 1).getArrangementList().get(0).getTclass().getName());
						continue;
					}
				}
			}
		}

		for (int j = 0; j < 29; j++) {
			tclassSheet.setColumnWidth(j, 600);
			teacherSheet.setColumnWidth(j, 600);
		}
		FileOutputStream fileOutputStream = new FileOutputStream("C:/360Downloads/1.xls");
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		workbook.close();
	}

	@Override
	public void initial(Integer scheduleId) {
		arrangementService.initial(scheduleId);
		return;
	}

}
