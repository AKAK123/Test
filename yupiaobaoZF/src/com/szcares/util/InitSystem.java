package com.szcares.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;

public class InitSystem extends HttpServlet
{
  private static final long serialVersionUID = -5983568368779180492L;
  private static final Logger logger = Logger.getLogger(InitSystem.class);
  //modify by chencl 2017-7-12
  private  CalcuFlowTimer calcuFlowTimer;

  public void init()
    throws ServletException
  {
    super.init();
    String projectHome = getServletContext().getRealPath("");
    System.setProperty("project.home", projectHome);

    logger.info("the calculated flow service start");
    calcuFlowTimer = new CalcuFlowTimer();
    calcuFlowTimer.initTimer();
    logger.info("the internal shopping service start");
  }

  public void destroy()
  {
    super.destroy();
    if (null != calcuFlowTimer)
      calcuFlowTimer.cancel();
  }
}