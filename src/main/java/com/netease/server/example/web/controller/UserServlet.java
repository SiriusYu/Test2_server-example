package com.netease.server.example.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 *
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 4607606190625660785L;

	/**
	 * Logger for this class.
	 */
	private static Logger logger = Logger.getLogger(UserServlet.class);

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		process(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("UserServlet post method is invoked.");
		response.setContentType("text/html;charset=UTF-8");
		process(request, response);
	}

	protected void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/* 获取用户名和密码(从请求表单) */
        String userName = request.getParameter("user");
	    String userPassword = request.getParameter("password");

	    /* 第一次登录创建Cookie */
	    Cookie[] cookies = request.getCookies();  /* 从请求中获取cookie(数组) */
	    if (null == cookies)  /* 第一次请求cookies为空 */
		{
		    /* Cookie保存用户名 */
	    	Cookie userNameCookie = new Cookie("user", userName);
		    userNameCookie.setMaxAge(30 * 60);/* 有效期30分钟 */
		    response.addCookie(userNameCookie);
		    
		    System.out.println("New Cookies  userName: " + userName);
	    }
	    else
		{
	        boolean bSameUserName = false;
	        
	        for (Cookie cookie : cookies) 
		    {
			    if (cookie.getValue().equals(userName)) 
			    {
			    	bSameUserName = true;
			    	System.out.println("Old userName: " + cookie.getValue());
			    	break;
			    }
		    }
		
		    if (false == bSameUserName)
		    {
		    	System.out.println("Different Cookie, current userName: " + userName);
		    }
		}
	
	    HttpSession session = request.getSession(); /* 从请求中获取session */
	    String sessionPassword = (String) session.getAttribute("password");
	    if (null == sessionPassword) /* 第一次登录时,Session中无password */
		{
	        /* Session保存密码 */
		    session.setAttribute("password", userPassword);
		    System.out.println("New session, password: " + userPassword);
	    }
	    else
	    {
	        /* 再次登录，使session失效 */
	    	System.out.println("Old session password(" + sessionPassword + ") to invalidate");
	        session.invalidate();
	    }

	    PrintWriter writer = response.getWriter();
		writer.println("<html>");
		writer.println("<head><title>用户中心</title></head>");
		writer.println("<body>");
		writer.println("<p>用户名：" + userName + "</p>");
		writer.println("<p>用户密码：" + userPassword + "</p>");
		writer.println("</body>");
		writer.println("</html>");
		writer.close();

	}
}
