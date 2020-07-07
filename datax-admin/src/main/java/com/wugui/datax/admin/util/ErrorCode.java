package com.wugui.datax.admin.util;

/**
 * 尤其注意：最好提供toString()实现。例如：
 *
 * @author <pre>
 *
 * &#064;Override
 * public String toString() {
 * 	return String.format(&quot;Code:[%s], Description:[%s]. &quot;, this.code, this.describe);
 * }
 * </pre>
 */
public interface ErrorCode {

    /**
     * 错误码编号
     *
     * @return String
     */
    String getCode();


    /**
     * 错误码描述
     *
     * @return String
     */
    String getDescription();


    /**
     * @return String
     */
    @Override
    String toString();
}
