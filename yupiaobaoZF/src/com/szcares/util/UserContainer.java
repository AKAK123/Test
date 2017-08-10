package com.szcares.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.szcares.bean.User;

public class UserContainer
{
  private static final Logger logger = Logger.getLogger(UserContainer.class);
  private static Properties properties = null;
  private static Map<String, String> userIndexMap = null;
 

  public static String getUserIndexByIP(String ip)
  {
    Map userIPMap = getUserIPMap();
    return (String)userIPMap.get(ip);
  }

  public static String getProperty(String commonKey, String userIndex)
  {
    String actualKey = commonKey.replaceFirst("0", userIndex);

    return properties.getProperty(actualKey);
  }

  public static User getUserByIndex(String userIndex)
  {
    User user = new User();
    user.setOffice(getProperty("user0.office", userIndex));
    user.setUsername(getProperty("SECURITY_user0.username", userIndex));
    user.setPassword(getProperty("SECURITY_user0.password", userIndex));
    return user;
  }

  @SuppressWarnings("unchecked")
public static List<User> getAllUser()
  {
    userIndexMap = getUserIPMap();
    Iterator userIpIte = userIndexMap.keySet().iterator();
    List userList = new ArrayList();

    while (userIpIte.hasNext()) {
      String userIndex = (String)userIndexMap.get(userIpIte.next());
      User user = getUserByIndex(userIndex);
      userList.add(user);
    }
    return userList;
  }

  @SuppressWarnings("unchecked")
private static Map<String, String> getUserIPMap()
  {
    Map IdMap = null;

    IdMap = new HashMap();
    for (int index = 0; ; index++) {
      String indexStr = String.valueOf(index);
      String ip = getProperty("user0.ip", indexStr);
      if (StringUtils.isBlank(ip))
        break;
      if (StringUtils.isNotEmpty(ip)) {
        IdMap.put(ip, indexStr);
      }
    }
    return IdMap;
  }

  public static User getUserByIndex(String userIndex, String calssName)
  {
    User user = new User();
    user.setOffice(getProperty("user0.office", userIndex));
    user.setUsername(getProperty("SECURITY_user0.username", userIndex));

    user.setPassword(getProperty("SECURITY_user0.password", userIndex));

    return user;
  }

  static
  {
    InputStream inputStream = null;
    String projectHome = System.getProperty("project.home").replaceAll("..", "")+ Constants.USER_PATH;//2017-07-20 by runqian，路径过滤特殊字符
    try
    {
      inputStream = new FileInputStream(projectHome);
    } catch (FileNotFoundException e) {
      logger.error("UserContainer.getUserByIndex 发生 FileNotFoundException 异常");
    }

    properties = new Properties();
    try {
      if (inputStream != null)
        properties.load(inputStream);
    }
    catch (IOException e) {
      logger.error("UserContainer.getUserByIndex 发生 IOException 异常");
    } finally {
    	if(inputStream != null) {
    		try {
    			inputStream.close();
			} catch (Exception e2) {
				logger.error("inputStream close exception!");
			}
    	}
    }
  }
}