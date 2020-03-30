package com.wugui.datax.registry;

import com.wugui.datax.registry.model.XxlRegistryDataParamVO;
import com.wugui.datax.registry.model.XxlRegistryParamVO;
import com.wugui.datax.registry.util.BasicHttpUtil;
import com.wugui.datax.registry.util.json.BasicJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * base util for registry
 *
 * @author xuxueli 2018-12-01 21:40:04
 */
public class XxlRegistryBaseClient {
    private static Logger logger = LoggerFactory.getLogger(XxlRegistryBaseClient.class);


    private String adminAddress;
    private String accessToken;
    private String biz;
    private String env;

    private List<String> adminAddressArr;


    public XxlRegistryBaseClient(String adminAddress, String accessToken, String biz, String env) {
        this.adminAddress = adminAddress;
        this.accessToken = accessToken;
        this.biz = biz;
        this.env = env;

        // valid
        if (adminAddress==null || adminAddress.trim().length()==0) {
            throw new RuntimeException("xxl-registry adminAddress empty");
        }
        if (biz==null || biz.trim().length()<4 || biz.trim().length()>255) {
            throw new RuntimeException("xxl-registry biz empty Invalid[4~255]");
        }
        if (env==null || env.trim().length()<2 || env.trim().length()>255) {
            throw new RuntimeException("xxl-registry biz env Invalid[2~255]");
        }

        // parse
        adminAddressArr = new ArrayList<>();
        if (adminAddress.contains(",")) {
            adminAddressArr.addAll(Arrays.asList(adminAddress.split(",")));
        } else {
            adminAddressArr.add(adminAddress);
        }

    }

    /**
     * registry
     *
     * @param registryDataList
     * @return
     */
    public boolean registry(List<XxlRegistryDataParamVO> registryDataList){

        // valid
        if (registryDataList==null || registryDataList.size()==0) {
            throw new RuntimeException("xxl-registry registryDataList empty");
        }
        for (XxlRegistryDataParamVO registryParam: registryDataList) {
            if (registryParam.getKey()==null || registryParam.getKey().trim().length()<4 || registryParam.getKey().trim().length()>255) {
                throw new RuntimeException("xxl-registry registryDataList#key Invalid[4~255]");
            }
            if (registryParam.getValue()==null || registryParam.getValue().trim().length()<4 || registryParam.getValue().trim().length()>255) {
                throw new RuntimeException("xxl-registry registryDataList#value Invalid[4~255]");
            }
        }

        // pathUrl
        String pathUrl = "/api/registry";

        // param
        XxlRegistryParamVO registryParamVO = new XxlRegistryParamVO();
        registryParamVO.setAccessToken(this.accessToken);
        registryParamVO.setBiz(this.biz);
        registryParamVO.setEnv(this.env);
        registryParamVO.setRegistryDataList(registryDataList);

        String paramsJson = BasicJson.toJson(registryParamVO);

        // result
        Map<String, Object> respObj = requestAndValid(pathUrl, paramsJson, 5);
        return respObj!=null?true:false;
    }

    private Map<String, Object> requestAndValid(String pathUrl, String requestBody, int timeout){

        for (String adminAddressUrl: adminAddressArr) {
            String finalUrl = adminAddressUrl + pathUrl;

            // request
            String responseData = BasicHttpUtil.postBody(finalUrl, requestBody, timeout);
            if (responseData == null) {
                return null;
            }

            // parse resopnse
            Map<String, Object> resopnseMap = null;
            try {
                resopnseMap = BasicJson.parseMap(responseData);
            } catch (Exception e) { }


            // valid resopnse
            if (resopnseMap==null
                    || !resopnseMap.containsKey("code")
                    || !"200".equals(String.valueOf(resopnseMap.get("code")))
                    ) {
                logger.warn("XxlRegistryBaseClient response fail, responseData={}", responseData);
                return null;
            }

            return resopnseMap;
        }


        return null;
    }

    /**
     * remove
     *
     * @param registryDataList
     * @return
     */
    public boolean remove(List<XxlRegistryDataParamVO> registryDataList) {
        // valid
        if (registryDataList==null || registryDataList.size()==0) {
            throw new RuntimeException("xxl-registry registryDataList empty");
        }
        for (XxlRegistryDataParamVO registryParam: registryDataList) {
            if (registryParam.getKey()==null || registryParam.getKey().trim().length()<4 || registryParam.getKey().trim().length()>255) {
                throw new RuntimeException("xxl-registry registryDataList#key Invalid[4~255]");
            }
            if (registryParam.getValue()==null || registryParam.getValue().trim().length()<4 || registryParam.getValue().trim().length()>255) {
                throw new RuntimeException("xxl-registry registryDataList#value Invalid[4~255]");
            }
        }

        // pathUrl
        String pathUrl = "/api/remove";

        // param
        XxlRegistryParamVO registryParamVO = new XxlRegistryParamVO();
        registryParamVO.setAccessToken(this.accessToken);
        registryParamVO.setBiz(this.biz);
        registryParamVO.setEnv(this.env);
        registryParamVO.setRegistryDataList(registryDataList);

        String paramsJson = BasicJson.toJson(registryParamVO);

        // result
        Map<String, Object> respObj = requestAndValid(pathUrl, paramsJson, 5);
        return respObj!=null?true:false;
    }

    /**
     * discovery
     *
     * @param keys
     * @return
     */
    public Map<String, TreeSet<String>> discovery(Set<String> keys) {
        // valid
        if (keys==null || keys.size()==0) {
            throw new RuntimeException("xxl-registry keys empty");
        }

        // pathUrl
        String pathUrl = "/api/discovery";

        // param
        XxlRegistryParamVO registryParamVO = new XxlRegistryParamVO();
        registryParamVO.setAccessToken(this.accessToken);
        registryParamVO.setBiz(this.biz);
        registryParamVO.setEnv(this.env);
        registryParamVO.setKeys(new ArrayList<String>(keys));

        String paramsJson = BasicJson.toJson(registryParamVO);

        // result
        Map<String, Object> respObj = requestAndValid(pathUrl, paramsJson, 5);

        // parse
        if (respObj!=null && respObj.containsKey("data")) {
            Map<String, TreeSet<String>> data = (Map<String, TreeSet<String>>) respObj.get("data");
            return data;
        }

        return null;
    }

    /**
     * discovery
     *
     * @param keys
     * @return
     */
    public boolean monitor(Set<String> keys) {
        // valid
        if (keys==null || keys.size()==0) {
            throw new RuntimeException("xxl-registry keys empty");
        }

        // pathUrl
        String pathUrl = "/api/monitor";

        // param
        XxlRegistryParamVO registryParamVO = new XxlRegistryParamVO();
        registryParamVO.setAccessToken(this.accessToken);
        registryParamVO.setBiz(this.biz);
        registryParamVO.setEnv(this.env);
        registryParamVO.setKeys(new ArrayList<String>(keys));

        String paramsJson = BasicJson.toJson(registryParamVO);

        // result
        Map<String, Object> respObj = requestAndValid(pathUrl, paramsJson, 60);
        return respObj!=null?true:false;
    }

}
