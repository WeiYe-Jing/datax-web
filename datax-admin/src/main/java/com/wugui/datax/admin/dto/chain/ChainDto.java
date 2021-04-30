package com.wugui.datax.admin.dto.chain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ChainDto implements Serializable {

    private String id;

    private String name;

    private List<NodeDto> nodeList;

    private List<LineDto> lineList;
}
