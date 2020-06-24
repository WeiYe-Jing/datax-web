package com.wugui.datax.admin.util;/**
 * ClassName: ApiMsg
 * Description:
 * date: 2020/6/23 9:08
 *
 * @author 吴迪
 * @Version 1.0
 */

import lombok.Data;

/**
 * ClassName: ApiMsg 
 * Description: 
 * date: 2020/6/23 9:08
 * @author 吴迪
 * @Version 1.0
 */
@Data
public class ApiMsg {
    private String code ;
    private String msg ;
    private Boolean flag;
    private Object obj;

}
