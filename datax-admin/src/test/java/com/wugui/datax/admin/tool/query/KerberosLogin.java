//package com.wugui.datax.admin.tool.query;
//
//public class KerberosLogin {
//
//    private Log logger = LogFactory.getLog(KerberosLogin.class);
//
//    public void login() {
//        String hiveUserName = "xxxxx@BCHKDC";//kerberos认证的用户principal名称
//        String hiveKeytab = "F:/NL_MYECLIPSE2014_WORK/usrkrb5/conf/xxxxx.keytab";//用户的keytab认证文件
//        String krbconf = "F:/NL_MYECLIPSE2014_WORK/usrkrb5/conf/krb5.conf";//kerberos5的配置文件
//
//        System.setProperty("java.security.krb5.conf", krbconf);
//        Configuration conf = new Configuration();
//        conf.set("hadoop.security.authentication", "Kerberos");
//        UserGroupInformation.setConfiguration(conf);
//        try {
//            UserGroupInformation.loginUserFromKeytab(hiveUserName, hiveKeytab);
//        } catch (IOException e) {
//            logger.error("Kerberos login fail.", e);
//        }
//    }
//}
