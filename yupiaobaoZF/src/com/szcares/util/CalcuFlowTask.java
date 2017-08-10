package com.szcares.util;

import java.util.Date;
import java.util.TimerTask;
import org.apache.log4j.Logger;

import com.szcares.action.CalculateFlow;

public class CalcuFlowTask extends TimerTask
{
  private static final Logger logger = Logger.getLogger(CalcuFlowTask.class);

  public void run()
  {
    Date date = new Date();
    logger.info(String.format("calculate flow start, the time is %s", date.toString()));//【runqian 2017-07-15】sonar 日志 bug 修复
    CalculateFlow.calculateFlow();
    logger.info("calculate flow end");
  }
}
