package com.wugui.datax.client.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Http连接处理工具类
 * 
 * @author lxj
 *
 */
public class HttpUtil {
	/**
	 * get方式
	 * 
	 * @param targetURL
	 * @param paramMap
	 */
	public String get(String targetURL, Map<String, String> paramMap) throws Exception {
		HttpURLConnection con = null;
		try {
			if (targetURL.indexOf("?") == -1) {
				targetURL += "?";
			}
			targetURL += uriMapToString(paramMap);
			// 获取链接
			con = getHttpConnection(targetURL);
			// 设置提交方式，用户名及密码
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

			// 请求返回值不为200，直接退出
			if (con.getResponseCode() != 200) {
				throw new RuntimeException("HTTP GET Request Failed with Error code : " + con.getResponseCode());
			}

			// 获取体信息
			StringBuffer bufBody = getBodyFieds(con);
			return bufBody.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			// 关闭链接
			disconnect(con);
		}
	}

	/**
	 * post方式-form
	 * 
	 * @param targetURL
	 * @param paramMap
	 */
	public String postForm(String targetURL, Map<String, String> paramMap) throws Exception {
		HttpURLConnection con = null;
		try {
			// 获取链接
			con = getHttpConnection(targetURL);
			// 设置提交方式，用户名及密码
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Accept-Charset", "UTF-8");

			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.write(uriMapToString(paramMap).getBytes());
			out.flush();
			out.close();

			// 请求返回值不为200，直接退出
			if (con.getResponseCode() != 200) {
				throw new RuntimeException("HTTP GET Request Failed with Error code : " + con.getResponseCode());
			}
			// 获取体信息
			StringBuffer bufBody = getBodyFieds(con);
			return bufBody.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			// 关闭链接
			disconnect(con);
		}
	}

	/**
	 * post方式-json
	 *
	 * @param targetURL
	 * @param paramMap
	 */
	public String postJson(String targetURL, Map<String, String> paramMap) throws Exception {
		HttpURLConnection con = null;
		try {
			// 获取链接
			con = getHttpConnection(targetURL);
			con.setDoOutput(true);
			con.setDoInput(true);
			// 设置提交方式，用户名及密码
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Accept-Charset", "UTF-8");

			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
			String data = JSONUtil.toJsonStr(new JSONObject(paramMap, false));
			out.write(data);
			out.flush();
			out.close();

			// 请求返回值不为200，直接退出
			if (con.getResponseCode() != 200) {
				throw new RuntimeException("HTTP GET Request Failed with Error code : " + con.getResponseCode());
			}
			// 获取体信息
			StringBuffer bufBody = getBodyFieds(con);
			return bufBody.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			// 关闭链接
			disconnect(con);
		}
	}

	/**
	 * put方式
	 * 
	 * @param targetURL
	 * @param param
	 */
	public String put(String targetURL, Map<String, String> paramMap) throws Exception {
		HttpURLConnection con = null;
		try {
			// 获取链接
			con = getHttpConnection(targetURL);
			// 设置提交方式，用户名及密码
			con.setRequestMethod("PUT");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.addRequestProperty("Svc_UserName", "admin");
			con.addRequestProperty("Svc_Password", "1");

			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.write(uriMapToString(paramMap).getBytes("UTF-8"));
			out.flush();
			out.close();

			// 请求返回值不为200，直接退出
			if (con.getResponseCode() != 200) {
				throw new RuntimeException("HTTP GET Request Failed with Error code : " + con.getResponseCode());
			}
			// 获取体信息
			StringBuffer bufBody = getBodyFieds(con);
			return bufBody.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			// 关闭链接
			disconnect(con);
		}
	}

	/**
	 * delete方式
	 * 
	 * @param targetURL
	 * @param param
	 */
	public String delete(String targetURL, Map<String, String> paramMap) throws Exception {
		HttpURLConnection con = null;
		try {
			if (targetURL.indexOf("?") == -1) {
				targetURL += "?";
			}
			targetURL += uriMapToString(paramMap);
			// 获取链接
			con = getHttpConnection(targetURL);
			// 设置提交方式，用户名及密码
			con.setRequestMethod("DELETE");
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.addRequestProperty("Svc_UserName", "admin");
			con.addRequestProperty("Svc_Password", "1");

			// 请求返回值不为200，直接退出
			if (con.getResponseCode() != 200) {
				throw new RuntimeException("HTTP GET Request Failed with Error code : " + con.getResponseCode());
			}

			// 获取体信息
			StringBuffer bufBody = getBodyFieds(con);
			return bufBody.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			// 关闭链接
			disconnect(con);
		}
	}

	/**
	 * 获取链接
	 * 
	 * @param targetURL
	 * @return
	 */
	public HttpURLConnection getHttpConnection(String targetURL) throws Exception {
		HttpURLConnection httpConnection = null;
		try {
			URL targetUrl = new URL(targetURL);

			httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setInstanceFollowRedirects(true);
			httpConnection.setConnectTimeout(5 * 1000);
		} catch (Exception e) {
			throw e;
		}
		return httpConnection;
	}

	/**
	 * 获取体信息
	 * 
	 * @param con
	 * @return
	 */
	public StringBuffer getBodyFieds(HttpURLConnection con) throws Exception {
		StringBuffer output = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
			String outStr = "";
			while ((outStr = br.readLine()) != null) {
				output.append(outStr).append("\n");
			}
		} catch (Exception e) {
			throw e;
		}
		return output;
	}

	/**
	 * 关闭链接
	 * 
	 * @param con
	 */
	public void disconnect(HttpURLConnection con) throws Exception {
		try {
			if (con != null) {
				con.disconnect();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private String uriMapToString(Map<String, String> paramMap) {
		Set<String> set = paramMap.keySet();
		StringBuffer postedData = new StringBuffer();
		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			String key = it.next();
			String value = paramMap.get(key) == null ? "" : paramMap.get(key);
			postedData.append(key);
			postedData.append("=");
			try {
				postedData.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			postedData.append("&");
		}
		if (postedData.length() > 0) {
			postedData.deleteCharAt(postedData.length() - 1);// 删除末尾分隔符
		}
		return postedData.toString();
	}
}
