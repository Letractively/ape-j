package cn.org.ape;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.org.ape.http.HttpRequest;
import cn.org.ape.http.HttpResponse;





public  abstract class BaseAction  
{

	private static final Logger log= LoggerFactory.getLogger(BaseAction.class);
	@SuppressWarnings("rawtypes")
	private static Class[] NO_ARGS_CLASS = new Class[0];
	private static Object[] NO_ARGS_OBJECT = new Object[0];
	protected HttpRequest request;
	protected HttpResponse response;
	protected ServletContext context;
	protected HttpSession session;
	
	
	public void init(HttpRequest request, HttpResponse response) {
		this.request = request;
		this.response = response;
		this.context = request.getServletContext();
		this.session = request.getSession();
	}
	
	protected  void run() throws ServletException, IOException
	{
		String action = request.getAction();
		try {
			
			this.getClass().getMethod(action, NO_ARGS_CLASS).invoke(this, NO_ARGS_OBJECT);
			
			
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			log.error("{} 类中 没有实现 {} 方法 \n" + e,this.getClass(),action);
			response.sendError(HttpResponse.SC_NOT_FOUND);//返回404
		}
	};
	
	protected void destroy()
	{
		
	}

	protected void execute() throws ServletException, IOException 
	{
		log.error("{} 类中 没有继承 execute() 方法 \n" ,this.getClass());
		response.sendError(HttpResponse.SC_NOT_FOUND);//返回404
	
	}
	
	
	

}
