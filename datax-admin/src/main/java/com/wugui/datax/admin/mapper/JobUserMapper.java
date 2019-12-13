package com.wugui.datax.admin.mapper;

import com.wugui.datax.admin.entity.JobUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xuxueli 2019-05-04 16:44:59
 */
@Mapper
@Repository
public interface JobUserMapper {

	public List<JobUser> pageList(@Param("offset") int offset,
								  @Param("pagesize") int pagesize,
								  @Param("username") String username);
	public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("username") String username);

	public JobUser loadByUserName(@Param("username") String username);

	public int save(JobUser xxlJobUser);

	public int update(JobUser xxlJobUser);
	
	public int delete(@Param("id") int id);

}
