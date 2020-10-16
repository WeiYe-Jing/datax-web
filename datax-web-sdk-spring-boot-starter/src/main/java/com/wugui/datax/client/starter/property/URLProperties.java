package com.wugui.datax.client.starter.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * datax-web定时调度SDK starter配置类
 *
 * @author Locki
 * @date 2020/9/23
 */
@ConfigurationProperties(prefix = "datax.web.admin")
public class URLProperties {
    private boolean enabled = false;
    private String accesskey;
    private String secretkey;
    private String url;
    private Api api;

    public boolean isEnabled() {
        return enabled;
    }

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public static class Api {
        private String add = null;
        private String loadJob = null;
        private String update = null;
        private String delete = null;
        private String start = null;
        private String stop = null;
        private String log = null;

        public String getAdd() {
            return add;
        }

        public void setAdd(String add) {
            this.add = add;
        }

        public String getLoadJob() {
            return loadJob;
        }

        public void setLoadJob(String loadJob) {
            this.loadJob = loadJob;
        }

        public String getUpdate() {
            return update;
        }

        public void setUpdate(String update) {
            this.update = update;
        }

        public String getDelete() {
            return delete;
        }

        public void setDelete(String delete) {
            this.delete = delete;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getStop() {
            return stop;
        }

        public void setStop(String stop) {
            this.stop = stop;
        }

        public String getLog() {
            return log;
        }

        public void setLog(String log) {
            this.log = log;
        }
    }
}
