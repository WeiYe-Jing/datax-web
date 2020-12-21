package com.wugui.datax.transformer;

import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.element.StringColumn;
import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.core.transport.record.DefaultRecord;
import com.alibaba.datax.core.transport.transformer.TransformerErrorCode;

import java.util.Arrays;
import java.util.Map;

/**
 * no comments.
 * Created by liqiang on 16/3/4.
 */
public class ReplaceNewLineSymbolTransformer extends ComplexTransformer {
    public ReplaceNewLineSymbolTransformer() {
        super.setTransformerName("replaceNewLineSymbol");
    }

    @Override
    public Record evaluate(Record record, Map<String, Object> tContext, Object... paras) {

        int columnIndex;

        try {
            if (paras.length != 2) {
                throw new RuntimeException("replaceNewLineSymbol paras must be 3");
            }

            columnIndex = (Integer) paras[0];

        } catch (Exception e) {
            throw DataXException.asDataXException(TransformerErrorCode.TRANSFORMER_ILLEGAL_PARAMETER, "paras:" + Arrays.asList(paras).toString() + " => " + e.getMessage());
        }

        Column column = record.getColumn(columnIndex);

        String columnStr = column.asString();
        if (null != columnStr) {

            columnStr=columnStr.replaceAll("\\\\N","").replaceAll("\\\\r","").replaceAll("\\\\n","");
            record.setColumn(columnIndex, new StringColumn(columnStr));
        };
        return record;
    }

    public static void main(String[] args) {
        DefaultRecord record = new DefaultRecord();
        Column column0 = new StringColumn("sfsf\\Nsdfsg");
        record.setColumn(0,column0);
        record= (DefaultRecord)new ReplaceNewLineSymbolTransformer().evaluate(record, null, new Integer[]{0,0});
        System.out.println(record.toString());
    }
}
