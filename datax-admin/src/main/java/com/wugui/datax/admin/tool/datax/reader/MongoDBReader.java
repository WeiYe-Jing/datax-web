package com.wugui.datax.admin.tool.datax.reader;

import com.google.common.collect.Maps;
import com.wugui.datatx.core.datasource.MongoDBDataSource;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datatx.core.util.JSONUtils;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.pojo.DataxMongoDBPojo;

import java.util.Map;

/**
 * @author jingwk
 */
public class MongoDBReader extends BaseReaderPlugin implements DataxReaderInterface {
    @Override
    public String getName() {
        return "mongodbreader";
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }

    @Override
    public Map<String, Object> buildMongoDB(DataxMongoDBPojo plugin) {


        //构建
        JobDatasource dataSource = plugin.getJdbcDatasource();

        MongoDBDataSource mongoDBDataSource = JSONUtils.parseObject(dataSource.getConnectionParams(), MongoDBDataSource.class);

        Map<String, Object> readerObj = Maps.newLinkedHashMap();
        readerObj.put("name", getName());
        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
        String[] addressList = null;
        String str = mongoDBDataSource.getMongoClientURI().replace(Constants.MONGO_URL_PREFIX, Constants.STRING_BLANK);
        if (str.contains(Constants.SPLIT_AT) && str.contains(Constants.SPLIT_DIVIDE)) {
            addressList = str.substring(str.indexOf(Constants.SPLIT_AT) + 1, str.indexOf(Constants.SPLIT_DIVIDE)).split(Constants.SPLIT_COMMA);
        } else if (str.contains(Constants.SPLIT_DIVIDE)) {
            addressList = str.substring(0, str.indexOf(Constants.SPLIT_DIVIDE)).split(Constants.SPLIT_COMMA);
        }
        parameterObj.put("address", addressList);
        parameterObj.put("userName", mongoDBDataSource.getUser() == null ? Constants.STRING_BLANK : mongoDBDataSource.getUser());
        parameterObj.put("userPassword", mongoDBDataSource.getPassword() == null ? Constants.STRING_BLANK : mongoDBDataSource.getPassword());
        parameterObj.put("dbName", mongoDBDataSource.getDatabase());
        parameterObj.put("collectionName", plugin.getReaderTable());
        parameterObj.put("column", plugin.getColumns());
        readerObj.put("parameter", parameterObj);
        return readerObj;
    }
}
