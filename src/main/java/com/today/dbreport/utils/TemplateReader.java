package com.today.dbreport.utils;

import java.io.InputStream;

/**
 * 读取模板
 *
 * @author BarryWang create at 2018/6/6 11:46
 * @version 0.0.1
 */
public class TemplateReader {
    /**
     * 获取文件输入流
     * @param filePath
     * @return
     */
    public static InputStream getTemplateInputStream(String filePath){
        return TemplateReader.class.getResourceAsStream("/template/"+filePath);
    }
}
