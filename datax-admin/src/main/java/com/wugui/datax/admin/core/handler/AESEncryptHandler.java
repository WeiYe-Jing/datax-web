package com.wugui.datax.admin.core.handler;

import com.wugui.datax.admin.util.AESUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author water
 * @date 20-03-17  下午5:38
 */
@MappedTypes({String.class})
public class AESEncryptHandler extends BaseTypeHandler<String> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, AESUtil.encrypt(parameter));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        return AESUtil.decrypt(columnValue);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return AESUtil.decrypt(columnValue);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String columnValue = cs.getString(columnIndex);
        return AESUtil.decrypt(columnValue);
    }
}