package com.wugui.dataxweb.controller;

import com.alibaba.datax.common.log.LogResult;
import com.baomidou.mybatisplus.extension.api.R;
import com.wugui.dataxweb.dto.RunJobDto;
import com.wugui.dataxweb.service.IDataxJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: datax-all
 * @description:
 * @author: huzekang
 * @create: 2019-05-05 14:21
 **/
@RestController
@RequestMapping("api")
@Api(tags = "datax作业接口")
public class JobController {

    @Autowired
    IDataxJobService iDataxJobService;

    @GetMapping("/testStartJob")
    public void testStartJob() {
        // 指定获取作业配置json的接口，此处用下面mock出来的接口提供
        String jobPath = "http://localhost:8080/mock_stream2stream";
        iDataxJobService.startJobByJsonStr(jobPath);
    }

    @GetMapping("/mock_oracle2mongodb")
    public String mock() {
        return "{\n" +
                "  \"job\": {\n" +
                "    \"setting\": {\n" +
                "      \"speed\": {\n" +
                "        \"channel\": 5\n" +
                "      }\n" +
                "    },\n" +
                "    \"content\": [\n" +
                "      {\n" +
                "        \"reader\": {\n" +
                "          \"name\": \"oraclereader\",\n" +
                "          \"parameter\": {\n" +
                "            \"username\": \"GZFUYI20190301\",\n" +
                "            \"password\": \"yibo123\",\n" +
                "            \"column\": [\n" +
                "              \"REPORT_NUMBER\",\"REPORT_DATE_SERIAL\",\"EXAM_ITEM_NAME\",\"EXAM_RESULT\"\n" +
                "            ],\n" +
                "            \"connection\": [\n" +
                "              {\n" +
                "                \"table\": [\n" +
                "                  \"TB_LIS_INDICATORS\"\n" +
                "                ],\n" +
                "                \"jdbcUrl\": [\n" +
                "                  \"jdbc:oracle:thin:@192.168.1.130:1521:gzfy\"\n" +
                "                ]\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        },\n" +
                "        \"writer\": {\n" +
                "          \"name\": \"mongodbwriter\",\n" +
                "          \"parameter\": {\n" +
                "            \"address\": [\n" +
                "              \"192.168.1.226:27017\"\n" +
                "            ],\n" +
                "            \"userName\": \"\",\n" +
                "            \"userPassword\": \"\",\n" +
                "            \"dbName\": \"datax_gzfy\",\n" +
                "            \"collectionName\": \"indicator22\",\n" +
                "            \"column\":   [\n" +
                "              { \"name\" : \"reportNumber\"         , \"type\" : \"string\"},\n" +
                "              { \"name\" : \"reportDateSerial\"    , \"type\" : \"string\"},\n" +
                "              { \"name\" : \"examItemName\"        , \"type\" : \"string\"},\n" +
                "              { \"name\" : \"examResult\"           , \"type\" : \"string\"}]\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
    }

    @GetMapping("/mock_stream2stream")
    @ApiOperation("提供stream2stream的配置")
    public String mock2() {
        return "{\n" +
                "  \"job\": {\n" +
                "    \"content\": [\n" +
                "      {\n" +
                "        \"reader\": {\n" +
                "          \"name\": \"streamreader\",\n" +
                "          \"parameter\": {\n" +
                "            \"sliceRecordCount\": 10,\n" +
                "            \"column\": [\n" +
                "              {\n" +
                "                \"type\": \"long\",\n" +
                "                \"value\": \"10\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"type\": \"string\",\n" +
                "                \"value\": \"hello，你好，世界-DataX\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        },\n" +
                "        \"writer\": {\n" +
                "          \"name\": \"streamwriter\",\n" +
                "          \"parameter\": {\n" +
                "            \"encoding\": \"UTF-8\",\n" +
                "            \"print\": true\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"setting\": {\n" +
                "      \"speed\": {\n" +
                "        \"channel\": 5\n" +
                "       }\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

    /**
     * 通过接口传入json配置启动一个datax作业
     *
     * @param jobJson
     * @return
     */
    @ApiOperation("通过传入json配置启动一个datax作业")
    @PostMapping("/runJob")
    public R<String> runJob(@RequestBody String jobJson) {
        String result = iDataxJobService.startJobByJsonStr(jobJson);
        return R.ok(result);
    }

    /**
     * 通过接口传入 runJobDto 实体启动一个datax作业，并记录日志
     *
     * @param runJobDto
     * @return
     */
    @ApiOperation("通过传入 runJobDto 实体启动一个datax作业，并记录日志")
    @PostMapping("/runJobLog")
    public R<String> runJobLog(@RequestBody RunJobDto runJobDto) {
        String result = iDataxJobService.startJobLog(runJobDto);
        return R.ok(result);
    }

    /**
     * 根据任务id查询日志
     *
     * @param id
     * @return
     */
    @ApiOperation("查看任务抽取日志,id为任务id，fromLineNum为读取的行数")
    @GetMapping("/viewJobLog")
    public R<LogResult> viewJogLog(Long id, int fromLineNum) {
        return R.ok(iDataxJobService.viewJogLog(id, fromLineNum));
    }
}
