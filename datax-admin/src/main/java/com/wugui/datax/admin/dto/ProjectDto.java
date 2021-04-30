package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectDto implements Serializable {

    private String id;

    private String type = "group";

    private String name;

    private String ico = "el-icon-video-play";

    private boolean open = false;

    private List<JobInfoDto> children = new ArrayList<>();
}
