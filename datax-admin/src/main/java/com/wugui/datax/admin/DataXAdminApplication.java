package com.wugui.datax.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;
import java.net.UnknownHostException;

@EnableSwagger2
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class DataXAdminApplication {

    private static Logger logger = LoggerFactory.getLogger(DataXAdminApplication.class);

    public static void main(String[] args) throws UnknownHostException {
        Environment env = new SpringApplication(DataXAdminApplication.class).run(args).getEnvironment();
        String envPort = env.getProperty("server.port");
        String envContext = env.getProperty("server.contextPath");
        String port = envPort == null ? "8080" : envPort;
        String context = envContext == null ? "" : envContext;
        String path = port + "" + context + "/doc.html";
        String externalAPI = InetAddress.getLocalHost().getHostAddress();
        logger.info(
                "Access URLs:\n----------------------------------------------------------\n\t"
                        + "Local-API: \t\thttp://127.0.0.1:{}\n\t"
                        + "External-API: \thttp://{}:{}\n\t"
                        + "web-URL: \t\thttp://127.0.0.1:{}/index.html\n\t----------------------------------------------------------",
                path, externalAPI, path, port);
    }


}
