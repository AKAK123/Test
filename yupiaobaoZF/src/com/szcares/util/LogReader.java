package com.szcares.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Logger;

public class LogReader
{
  private static final Logger logger = Logger.getLogger(LogReader.class);
  private static String logFilePath;
  private static final int MAX_STR_LEN = 1024;

  public static String formateDate(Calendar calendar)
  {
    Date date = calendar.getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String formatedDate = dateFormat.format(date);
    return formatedDate;
  }

  public static String readPreLogFile(Calendar calendar)
  {
    if (StringUtils.isEmpty(logFilePath)) {
      DailyRollingFileAppender appender = (DailyRollingFileAppender)Logger.getRootLogger().getAppender("TRANSLOG");

      logFilePath = appender.getFile();
    }

    String formatedDate = formateDate(calendar);
    String preLogFilePath = logFilePath + "." + formatedDate;
    logger.info(String.format("handled the log file name[%s]", preLogFilePath));//【runqian 2017-07-15】sonar 日志 bug 修复
    String log = null;
    try {
      log = FileUtils.readFileToString(new File(preLogFilePath));
    } catch (IOException e) {
      logger.error("LogReader.readPreLogFile 发生异常");
    }
    return log;
  }

  public static String readInputToString(InputStream inputStream)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf8"));
    StringBuffer buffer = new StringBuffer();
    int intc;
    while ((intc = br.read()) != -1) {
		char c = (char)intc;
		if(c=='\n'){
			break;
		}
		if(buffer.length()>=MAX_STR_LEN){
			try {
				throw new Exception("input too length");
			} catch (Exception e) {
				logger.error("throw input too length Exception");;
			}
		}
		buffer.append(c);
    }
    br.close();
    return buffer.toString();
  }

}
