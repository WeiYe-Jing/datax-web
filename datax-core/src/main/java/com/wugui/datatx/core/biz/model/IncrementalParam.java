package com.wugui.datatx.core.biz.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by jingwk on 2019/12/18.
 */
public class IncrementalParam implements Serializable {

    private static final long serialVersionUID = 42L;

    private Map<String,String> commandParams;
    private long idParam;
    private Date dateParam;

    public Map<String, String> getCommandParams() {
        return commandParams;
    }

    public void setCommandParams(Map<String, String> commandParams) {
        this.commandParams = commandParams;
    }

    public long getIdParam() {
        return idParam;
    }

    public void setIdParam(long idParam) {
        this.idParam = idParam;
    }

    public Date getDateParam() {
        return dateParam;
    }

    public void setDateParam(Date dateParam) {
        this.dateParam = dateParam;
    }
}
