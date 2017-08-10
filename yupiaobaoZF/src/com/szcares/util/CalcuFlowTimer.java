package com.szcares.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import org.apache.log4j.Logger;

public class CalcuFlowTimer
{
  private static final Logger logger = Logger.getLogger(CalcuFlowTimer.class);
  //modify by chencl,case static 2017-7-12
  private  Timer timer;

  public void initTimer()
  {
    timer = new Timer();
    String targetHourStr = ConfigureReader.getProperty("excute.calculating.flow.time");

    int targetHour = Integer.parseInt(targetHourStr);

    Calendar calendar = Calendar.getInstance();

    calendar.set(11, targetHour);
    calendar.set(12, 0);
    calendar.set(13, 0);
    Date date = calendar.getTime();

    if (date.before(new Date())) {
      date = addDay(date, 1);
    }
    Timer timer = new Timer();

    timer.schedule(new CalcuFlowTask(), date, 86400000L);
  }

  public Date addDay(Date date, int num)
  {
    Calendar startDT = Calendar.getInstance();

    startDT.setTime(date);

    startDT.add(5, num);

    return startDT.getTime();
  }

  public void cancel()
  {
    if (null != timer)
      timer.cancel();
  }
}