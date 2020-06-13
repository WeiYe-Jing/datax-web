package com.wugui.datax.admin.mapper;

import com.wugui.datax.admin.entity.JobTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * job info
 * @author xuxueli 2016-1-12 18:03:45
 */
@Mapper
public interface JobTemplateMapper {

	public List<JobTemplate> pageList(@Param("offset") int offset,
									  @Param("pagesize") int pagesize,
									  @Param("jobGroup") int jobGroup,
									  @Param("jobDesc") String jobDesc,
									  @Param("executorHandler") String executorHandler,
									  @Param("userId") int userId,
									  @Param("projectIds") Integer[] projectIds);

	public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("jobGroup") int jobGroup,
                             @Param("jobDesc") String jobDesc,
                             @Param("executorHandler") String executorHandler,
                             @Param("userId") int userId,
							 @Param("projectIds") Integer[] projectIds);

	public int save(JobTemplate info);

	public JobTemplate loadById(@Param("id") int id);

	public int update(JobTemplate jobTemplate);

	public int delete(@Param("id") long id);

}
