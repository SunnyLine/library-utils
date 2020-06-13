package com.library.util.common;

/**
 * ProjectName: X-Util
 * ClassName: com.pullein.common.util.RegularConstants
 * Author: XG
 * CreateDate: 2019/12/15 11:34
 * Description:常用正则字符串常量
 */
public interface RegularConstants {
    /**
     * 数字正则
     */
    String RULE_NUMBER = "^[0-9]*$";
    /**
     * 字母正则
     */
    String RULE_LETTER = "^[a-zA-Z]*$";

    /**
     * 数字+字母正则
     */
    String RULE_NUMBER_LETTER = "^[0-9a-zA-Z]*$";


}
