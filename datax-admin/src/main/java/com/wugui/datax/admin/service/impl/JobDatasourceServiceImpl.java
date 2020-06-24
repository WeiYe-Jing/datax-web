package com.wugui.datax.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wugui.datax.admin.entity.JobDatasourceEntity;
import com.wugui.datax.admin.mapper.JobDatasourceMapper;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.service.JobDatasourceService;
import com.wugui.datax.admin.tool.query.BaseQueryTool;
import com.wugui.datax.admin.tool.query.HBaseQueryTool;
import com.wugui.datax.admin.tool.query.MongoDBQueryTool;
import com.wugui.datax.admin.tool.query.QueryToolFactory;
import com.wugui.datax.admin.util.AESUtil;
import com.wugui.datax.admin.util.ApiMsg;
import com.wugui.datax.admin.util.JdbcConstants;
import com.wugui.datax.admin.util.Kerberos;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by jingwk on 2020/01/30
 */
@Service
@Transactional(readOnly = true)
public class JobDatasourceServiceImpl extends ServiceImpl<JobDatasourceMapper, JobDatasource> implements JobDatasourceService {

    @Resource
    private JobDatasourceMapper datasourceMapper;

//    @Resource
//    private  Kerberos kerberos;

    @Override
    public Boolean dataSourceTest(JobDatasourceEntity jobDatasource) throws IOException, SQLException {
        if (JdbcConstants.HBASE.equals(jobDatasource.getDatasource())) {
            return new HBaseQueryTool(jobDatasource).dataSourceTest();
        }
        // 如果是hive同时采用了kerberos认证
        if (JdbcConstants.HIVE.equals(jobDatasource.getDatasource()) && jobDatasource.getIsKerberos()) {
            boolean flag =false;
            try {
                //todo windows系统测试使用,上传代码注掉
                jobDatasource.setIniPath("C:\\ProgramData\\MIT\\Kerberos5\\krb5.ini");
//                    //设置 krb5.init 文件路径
//                    iniPath="C:\\ProgramData\\MIT\\Kerberos5\\krb5.ini";
//                    //设置 keytab 用户名
//                    user="jc_wud";
//                    //设置 keytab 文件路径
//                    keytabPath="C:\\ProgramData\\MIT\\Kerberos5\\jc_wud.keytab";
                Kerberos kerberos =new  Kerberos();
                ApiMsg apiMsg = null;
                apiMsg = kerberos.get_conn(jobDatasource.getJdbcUrl(), jobDatasource.getIniPath(), jobDatasource.getNameStr(), jobDatasource.getKeytabPath());
                flag=apiMsg.getFlag();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                flag=false;
            }
            return flag;
        }
        String userName = AESUtil.decrypt(jobDatasource.getJdbcUsername());
        //  判断账密是否为密文
        if (userName == null) {
            jobDatasource.setJdbcUsername(AESUtil.encrypt(jobDatasource.getJdbcUsername()));
        }
        String pwd = AESUtil.decrypt(jobDatasource.getJdbcPassword());
        if (pwd == null) {
            jobDatasource.setJdbcPassword(AESUtil.encrypt(jobDatasource.getJdbcPassword()));
        }
        if (JdbcConstants.MONGODB.equals(jobDatasource.getDatasource())) {
            return new MongoDBQueryTool(jobDatasource).dataSourceTest(jobDatasource.getDatabaseName());
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jobDatasource);
        return queryTool.dataSourceTest();
    }
    @Override
    public int update(JobDatasource datasource) {
        return datasourceMapper.update(datasource);
    }

}