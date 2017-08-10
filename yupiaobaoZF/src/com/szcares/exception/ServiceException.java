package com.szcares.exception;

import javax.servlet.ServletException;

public class ServiceException extends ServletException
{
  private static final long serialVersionUID = -5615525744541790435L;

  public ServiceException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public ServiceException(String message)
  {
    super(message);
  }

  public ServiceException(Throwable cause)
  {
    super(cause);
  }
}
