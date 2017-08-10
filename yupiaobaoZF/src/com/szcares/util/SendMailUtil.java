package com.szcares.util;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.szcares.bean.MailBean;

/**
 * Created by huc on 2017/2/3.
 * 发送异常邮件工具类
 */

public class SendMailUtil {

    
	private static final JavaMailSenderImpl  javaMailSender = new JavaMailSenderImpl();
	private static final Logger logger = Logger.getLogger(SendMailUtil.class);
	

	/**
     * 创建MimeMessage
     * @param mailBean
     * @return mailBean
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
	public static  MimeMessage createMimeMessage(MailBean mailBean) {
		
    	javaMailSender.setDefaultEncoding("utf-8");
    	javaMailSender.setHost(mailBean.getHost());
    	javaMailSender.setUsername(mailBean.getUser());
    	javaMailSender.setPassword(mailBean.getPassWord());
    	
    	//创建 MimeMessage 消息
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        
        MimeMessageHelper messageHelper;
        //2017-07-20 by runq，修复缺少抛出异常bug
		try {
			messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			messageHelper.setFrom(mailBean.getUser(), mailBean.getUserName());
			messageHelper.setTo(mailBean.getToEmails());
	        messageHelper.setSubject(mailBean.getSubject());
	        messageHelper.setText(mailBean.getContext(), true); // html: true
		} catch (MessagingException e) {
			logger.error("发生 MessagingException 异常！");
		} catch (UnsupportedEncodingException e) {
			logger.error("发生 UnsupportedEncodingException 异常！");
		}
        
        return mimeMessage;
    }

	/**
	 * 邮件发送
	 * @param mailBean
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
    public static void sendMail(MailBean mailBean) throws UnsupportedEncodingException, MessagingException{
        MimeMessage msg = createMimeMessage(mailBean);
        try{
			javaMailSender.send(msg);
        }catch (Exception e){
            logger.error("SendMailUtil.sendMail 发生异常");
        }
    }
    

}
