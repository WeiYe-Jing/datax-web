package com.wugui.datax.admin.tool.pojo;

import lombok.Data;

import java.util.List;

@Data
public class DataXTransformer {

    private String name;
    private Parameter parameter;

    @Data
    public static class Parameter {
        private List<String> paras;
        private int columnIndex;
        private List<String> extraPackage;
    }
}
