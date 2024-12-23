package com.libaixieshi.parse.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegUtil {

    /**
     * @param text 待匹配的数据
     * @param pattern pattern
     * @param groupIndex 组的索引 默认值为1
     */
    public static String applyReg(String text, Pattern pattern, Integer groupIndex) {
        groupIndex = groupIndex == null ? 1 : groupIndex;
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(groupIndex);
        }
        return null;
    }
}

