package com.szcares.util;  
  
import java.io.BufferedReader;  
import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.FileWriter;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  
import java.util.ArrayList;  
import java.util.List;  
import java.util.zip.ZipEntry;  
import java.util.zip.ZipOutputStream;  

import org.apache.log4j.Logger;

/** 
 * IO常用工具包 
 */  
public class IOUtils {  
	
	private static Logger logger = Logger.getLogger(IOUtils.class);
  
	private static final int MAX_STR_LEN = 1024;
	
    /** 
     * 将输入流转换成字节流 
     * @param input 
     * @return 
     * @throws Exception 
     */  
    public static byte[] toBytes(InputStream input) throws Exception {  
        byte[] data = null;  
        try {  
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();  
            byte[] b = new byte[1024];  
            int read = 0;  
            while ((read = input.read(b)) > 0) {  
                byteOut.write(b, 0, read);  
            }  
            data = byteOut.toByteArray();  
        } catch (Exception e) {  
        	logger.info("toBytes function exception occurred!");
        } finally {  
            input.close();  
        }  
        return data;  
    }  
  
    /** 
     * 将文件读取为一个字符串 
     * @param input 
     * @return 
     * @throws Exception 
     */  
    public static String toString(File file) throws Exception { 
    	String result = "";
    	InputStream inputStream = null;//【runqian 2017-07-15】sonar bug 资源未关闭修复
    	try {
    		inputStream = new FileInputStream(file);
    		result = toString(inputStream);
		} catch (Exception e) {
			logger.info("toString function exception occurred!");
		} finally {
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (Exception e2) {
					logger.info("inputStream close exception!");
				}
			}
		}
    	return result;
    }  
  
    /** 
     * 将输入流转换为一个串 
     *  
     * @param input 
     * @return 
     * @throws Exception 
     */  
    public static String toString(InputStream input) throws Exception {  
        return toStringWithLineBreak(input, "");  
    }  
  
    /** 
     * 以指定编码格式将输入流按行置入一个List<String> 
     *  
     * @param input 
     * @return 
     * @throws Exception 
     */  
    public static List<String> toLines(InputStream input, String encoding)  
            throws Exception {  
        InputStreamReader insreader = new InputStreamReader(input, encoding);  
        BufferedReader bin = new BufferedReader(insreader);  
        List<String> lines = new ArrayList<String>();  
        String line;  
        while ((line = bin.readLine()) != null) {  
        	if(line.length()>=MAX_STR_LEN){
				try {
					throw new Exception("input too length");
				} catch (Exception e) {
					logger.error("throw input too length Exception");;
				}
			}
            lines.add(line);  
        }  
        bin.close();  
        insreader.close();  
        return lines;  
    }  
  
    /** 
     * 以GBK格式将输入流按行置入一个List<String> 
     *  
     * @param input 
     * @return 
     * @throws Exception 
     */  
    public static List<String> toLines(InputStream input) throws Exception {  
        return toLines(input, "UTF-8");  
    }  
  
    /** 
     * 转换为每行补充指定换行符(例如："/n"，"</br>") 
     *  
     * @param input 
     * @param lineBreak 
     * @return 
     * @throws Exception 
     */  
    public static String toStringWithLineBreak(InputStream input,  
            String lineBreak) throws Exception {  
        List<String> lines = toLines(input);  
        StringBuilder sb = new StringBuilder(20480);  
        for (String line : lines) {  
            sb.append(line);  
            if (lineBreak != null) {  
                sb.append(lineBreak);  
            }  
        }  
        return sb.toString();  
    }  
  
    /** 
     * 将字符串转出到指定文件 
     * @param saveFile 
     * @param content 
     */  
    public static void toFile(File saveFile, String content) {  
        File parent = saveFile.getParentFile();  
        if (!parent.exists()) {  
            parent.mkdirs();  
        }  
        PrintWriter out = null;  
        try {  
            out = new PrintWriter(new FileWriter(saveFile));  
            out.print(content);  
            out.flush();  
        } catch (Exception e) {  
        	logger.info("toFile function exception occurred!");
        } finally {  
            if (out != null) {  
            	try {
            		out.close();  
				} catch (Exception e2) {
					logger.info("out close exception!");
				}
            }  
        }  
    }  
  
    /** 
     * 将一组文件打zip包 
     *  
     * @param srcFiles 
     * @param targetFileName 
     * @throws IOException 
     */  
    public static void filesToZip(List<File> srcFiles, String targetFileName)  
            throws IOException {  
        String fileOutName = targetFileName + ".zip";  
        byte[] buf = new byte[1024];  
        FileInputStream in = null;  
        FileOutputStream fos = null;  
        ZipOutputStream out = null;  
        try {  
            fos = new FileOutputStream(fileOutName);  
            out = new ZipOutputStream(fos);  
            for (File file : srcFiles) {  
                in = new FileInputStream(file);  
                out.putNextEntry(new ZipEntry(file.getName()));  
                int len;  
                while ((len = in.read(buf)) != -1) {  
                    out.write(buf, 0, len);  
                }  
                if (in != null) {  
                    in.close();  
                }  
            }  
        } catch (Exception e) {  
            logger.info("filesToZip function exception occurred!");
        } finally {  
        	if(out != null){//【runqian 2017-07-15】sonar bug 资源未关闭修复
        		try {
        			out.closeEntry();  
                    out.close(); 
				} catch (Exception e2) {
					logger.info("out close exception!");
				}
            }
        	if (fos != null) {   
        		try {
        			fos.close();  
				} catch (Exception e2) {
					logger.info("fos close exception!");
				}
            } 
            if (in != null) {  
            	try {
            		in.close();  
				} catch (Exception e2) {
					logger.info("in close exception!");
				}
            }  
        }  
    }  
  
}  
