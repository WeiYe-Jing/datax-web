package com.wugui.datax.transformer;

import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.element.StringColumn;
import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.core.transport.transformer.TransformerErrorCode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

/**
 * no comments.
 * Created by liqiang on 16/3/4.
 */
public class Md5Transformer extends ComplexTransformer {


    public Md5Transformer() {
        super.setTransformerName("md5");
    }

    @Override
    public Record evaluate(Record record, Map<String, Object> tContext, Object... paras) {

        int columnIndex;


        try {
            if (paras.length != 2) {
                throw new RuntimeException("md5 paras must be 2");
            }

            columnIndex = (Integer) paras[0];

        } catch (Exception e) {
            throw DataXException.asDataXException(TransformerErrorCode.TRANSFORMER_ILLEGAL_PARAMETER, "paras:" + Arrays.asList(paras).toString() + " => " + e.getMessage());
        }

        Column column = record.getColumn(columnIndex);

        String md5str = column.asString();
        if (null != md5str) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] bytes = md5.digest(md5str.getBytes());
                StringBuffer stringBuffer = new StringBuffer();
                for (byte b : bytes) {
                    int bt = b & 0xff;
                    if (bt < 16) {
                        stringBuffer.append(0);
                    }
                    stringBuffer.append(Integer.toHexString(bt));
                }
                md5str = stringBuffer.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            record.setColumn(columnIndex, new StringColumn(md5str));
        }
        ;
        return record;
    }
}
