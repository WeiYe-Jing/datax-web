package com.wugui.datax.client.starter.config;

import com.wugui.datax.client.starter.property.URLProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.wugui.datax.client.JobClient;

@Configuration
@EnableConfigurationProperties(URLProperties.class)
@ConditionalOnProperty(prefix = "datax.web.admin", name = "enabled", havingValue = "true")
public class SdkConfig {
	@Autowired
	private URLProperties urlProperties;

	@Bean
	public JobClient xxlJobClient() {
		JobClient client = new JobClient();
		if (urlProperties.getUrl() == null || urlProperties.getUrl().trim().length() == 0) {
			throw new RuntimeException("datax.web.admin.url is required!");
		}
		if (urlProperties.getAccesskey() == null || urlProperties.getAccesskey().trim().length() == 0) {
			throw new RuntimeException("datax.web.admin.accessKey is required!");
		}
		if (urlProperties.getSecretkey() == null || urlProperties.getSecretkey().trim().length() == 0) {
			throw new RuntimeException("datax.web.admin.secretKey is required!");
		}
		client.setWebUrl(urlProperties.getUrl());
		URLProperties.Api api = urlProperties.getApi();
		if (api == null) {
			return client;
		}
		if (api.getAdd() != null && api.getAdd().trim().length() > 0) {
			client.setAdd(api.getAdd());
		}
		if (api.getDelete() != null && api.getDelete().trim().length() > 0) {
			client.setDelete(api.getDelete());
		}
		if (api.getUpdate() != null && api.getUpdate().trim().length() > 0) {
			client.setUpdate(api.getUpdate());
		}
		if (api.getLoadJob() != null && api.getLoadJob().trim().length() > 0) {
			client.setLoad(api.getLoadJob());
		}
		if (api.getStart() != null && api.getStart().trim().length() > 0) {
			client.setStart(api.getStart());
		}
		if (api.getStop() != null && api.getStop().trim().length() > 0) {
			client.setStop(api.getStop());
		}
		if (api.getLog() != null && api.getLog().trim().length() > 0) {
			client.setLog(api.getLog());
		}
		return client;
	}
}
