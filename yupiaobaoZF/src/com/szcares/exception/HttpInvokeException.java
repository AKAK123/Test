package com.szcares.exception;

public class HttpInvokeException extends Exception
{
  private static final long serialVersionUID = 605946195560652706L;

  public HttpInvokeException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public HttpInvokeException(String message)
  {
    super(message);
  }

  public HttpInvokeException(Throwable cause)
  {
    super(cause);
  }
}

/* Location:           C:\Users\kaiya_swen\Desktop\shopping\DomShopping\WEB-INF\classes\
 * Qualified Name:     com.travelsky.internalshop.exception.HttpInvokeException
 * JD-Core Version:    0.6.0
 */