package com.wugui.datax.admin.service;

import com.wugui.datax.admin.dto.DataXJsonBuildDTO;

public interface FlinkxJsonService {

    String buildJobJson(DataXJsonBuildDTO dto);
}
