package com.szcares.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.szcares.bean.User;
import com.szcares.util.ConfigureReader;
import com.szcares.util.LogReader;
import com.szcares.util.SecurityScanner;
import com.szcares.util.UserContainer;

public class CalculateFlow
{
  private static final Logger logger = Logger.getLogger(CalculateFlow.class);
  private static String PREFIX = "daily_flow_";
  private static String SUFFIX = ".txt";
  
  public static void calculateFlow()
  {
    Calendar calendar = Calendar.getInstance();

    calendar.add(5, -1);
    String log = LogReader.readPreLogFile(calendar);
    String dailyFlow;
    if (StringUtils.isEmpty(log)) {
      dailyFlow = "the log file isn't exist or its content is empty";
    } else {
      List userList = UserContainer.getAllUser();
      StringBuilder dailyFlowBuild = new StringBuilder();
      for (User user :(List<User>) userList) {
        String userInfo = user.toString();

        int count = getMatchCount(log, "user validation success: ", userInfo);

        if (count != 0) {
          dailyFlowBuild.append(userInfo).append(" - total visit times: ").append(count);

          count = getMatchCount(log, "http invoke success: ", userInfo);
          dailyFlowBuild.append(",  eSPEED http invoke success(times): ").append(count);

          count = getMatchCount(log, "http invoke failed: ", userInfo);
          dailyFlowBuild.append(",  eSPEED http invoke failed(times): ").append(count);
          dailyFlowBuild.append("\n");
        }
      }
      dailyFlow = dailyFlowBuild.toString();
      if (StringUtils.isEmpty(dailyFlow)) {
        dailyFlow = "there isn't user which visit the service today";
      }
    }

    try
    {
      writeFile(dailyFlow);
    } catch (IOException e) {
      logger.error("CalculateFlow.calculateFlow 发生 IOException 异常");
    }
  }

  public static int getMatchCount(String content, String prefix, String userInfo)
  {
    String key = prefix + userInfo;
    Pattern pattern = Pattern.compile(key.replace("[", "\\[").replace("]", "\\]"));
    Matcher matcher = pattern.matcher(content);
    int count = 0;
    while (matcher.find()) {
      count++;
    }
    return count;
  }

  private static void writeFile(String content)
    throws IOException
  {
    Calendar calendar = Calendar.getInstance();
    calendar.add(5, -1);
    String formatedDate = LogReader.formateDate(calendar);
    String directory = ConfigureReader.getProperty("daily.flow.file.directory");
    File dirFile = new File(directory.replace("..", ""));//2017-07-20 by runqian，路径字符串中特殊字符的过滤
    if (!dirFile.exists()) {
      dirFile.mkdirs();
    }
    String dailyFlowPath = directory + File.separator + PREFIX + formatedDate + SUFFIX;

    OutputStream output = null;//【runqian 2017-07-15】sonar bug 资源未关闭修复
    try {
    	output = new FileOutputStream(new File(dailyFlowPath.replace("..", "")));//2017-07-20 by runqian，路径字符串中特殊字符的过滤
    	String result = SecurityScanner.htmlEncode(content);
    	output.write(result.getBytes());
        output.flush();
	} catch (Exception e) {
		logger.info("output exception occurred!");
	} finally {
		/**modify by chencl 2017-7-17**/
		if(null!=output){
			try {
				output.close();

			} catch (Exception e2) {
				logger.info("close output exception occurred!");
			}
		}
	}
  }
}

/* Location:           C:\Users\kaiya_swen\Desktop\shopping\DomShopping\WEB-INF\classes\
 * Qualified Name:     com.travelsky.internalshop.CalculateFlow
 * JD-Core Version:    0.6.0
 */