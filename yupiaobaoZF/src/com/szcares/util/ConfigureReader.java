package com.szcares.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

public class ConfigureReader
{
  private static final Logger logger = Logger.getLogger(ConfigureReader.class);
  private static Properties properties = null;
  
  public static String getProperty(String key)
  {
    return properties.getProperty(key);
  }

  static
  {
    InputStream inputStream = null;
    String projectHome = System.getProperty("project.home").replaceAll("..", "") + Constants.SERVER_PATH;//2017-07-20 by runqian，路径过滤特殊字符
    /*modify by chencl 2017-7-12*/
    try
    {
      inputStream = new FileInputStream(projectHome);
      if (inputStream != null)
    	  properties = new Properties();
          properties.load(inputStream);
    } catch (FileNotFoundException  e) {
      logger.error("ConfigureReader 发生 FileNotFoundException 异常");
    } catch (IOException e) {
		logger.error("ConfigureReader 发生 IOException 异常");
	}finally{
		if(null!=inputStream){
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.error("inputStream close 异常");
			}
		}
	}
  }
}

/* Location:           C:\Users\kaiya_swen\Desktop\shopping\DomShopping\WEB-INF\classes\
 * Qualified Name:     com.travelsky.internalshop.util.ConfigureReader
 * JD-Core Version:    0.6.0
 */