package com.wugui.datax.admin.util;

import com.wugui.datatx.core.enums.ResUploadType;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datatx.core.util.PropertyUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * common utils
 */
public class CommonUtils {
  private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

  private CommonUtils() {
    throw new IllegalStateException("CommonUtils class");
  }





  /**
   * if upload resource is HDFS and kerberos startup is true , else false
   * @return true if upload resource is HDFS and kerberos startup
   */
  public static boolean getKerberosStartupState(){
    String resUploadStartupType = PropertyUtils.getString(Constants.RESOURCE_STORAGE_TYPE);
    ResUploadType resUploadType = ResUploadType.valueOf(resUploadStartupType);
    Boolean kerberosStartupState = PropertyUtils.getBoolean(Constants.HADOOP_SECURITY_AUTHENTICATION_STARTUP_STATE,false);
    return resUploadType == ResUploadType.HDFS && kerberosStartupState;
  }

  /**
   * load kerberos configuration
   * @throws Exception errors
   */
  public static void loadKerberosConf()throws Exception{
    if (CommonUtils.getKerberosStartupState())  {
      System.setProperty(Constants.JAVA_SECURITY_KRB5_CONF, PropertyUtils.getString(Constants.JAVA_SECURITY_KRB5_CONF_PATH));
      Configuration configuration = new Configuration();
      configuration.set(Constants.HADOOP_SECURITY_AUTHENTICATION, Constants.KERBEROS);
      UserGroupInformation.setConfiguration(configuration);
      UserGroupInformation.loginUserFromKeytab(PropertyUtils.getString(Constants.LOGIN_USER_KEY_TAB_USERNAME),
              PropertyUtils.getString(Constants.LOGIN_USER_KEY_TAB_PATH));
    }
  }
}
