package cn.org.ape;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.org.ape.http.HttpRequest;
import cn.org.ape.http.HttpResponse;




/**
 * 所以的action类的父类
 * @author 陈磊
 *
 */
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
	
	
	/**
	 * 初始化
	 * @param request
	 * @param response
	 */
	public void init(HttpRequest request, HttpResponse response) {
		this.request = request;
		this.response = response;
		this.context = request.getServletContext();
		this.session = request.getSession();
	}
	
	/**
	 * 执行的类
	 * @throws ServletException
	 * @throws IOException
	 */
	protected  void run() throws ServletException, IOException
	{
		String action = request.getAction();
		try {
			//aop调用 执行 action 方法
			this.getClass().getMethod(action, NO_ARGS_CLASS).invoke(this, NO_ARGS_OBJECT);
			
			
		} catch (IllegalAccessException e){
			log.error("{} 类中 没有实现 {} 方法 \n" + e,this.getClass(),action);
			response.sendError(HttpResponse.SC_NOT_FOUND);//返回404
		}
		catch (IllegalArgumentException e){
			log.error("{} 类中 没有实现 {} 方法 \n" + e,this.getClass(),action);
			response.sendError(HttpResponse.SC_NOT_FOUND);//返回404
		}
		
		catch (InvocationTargetException e){
			log.error("{} 类中 没有实现 {} 方法 \n" + e,this.getClass(),action);
			response.sendError(HttpResponse.SC_NOT_FOUND);//返回404
		}
		catch ( NoSuchMethodException e){
			log.error("{} 类中 没有实现 {} 方法 \n" + e,this.getClass(),action);
			response.sendError(HttpResponse.SC_NOT_FOUND);//返回404
		}
		catch (SecurityException e) {
			log.error("{} 类中 没有实现 {} 方法 \n" + e,this.getClass(),action);
			response.sendError(HttpResponse.SC_NOT_FOUND);//返回404
		}
	};
	
	protected void destroy()
	{
		
	}
	
	/**
	 * 默认执行的方法
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void execute() throws ServletException, IOException 
	{
		log.error("{} 类中 没有继承 execute() 方法 \n" ,this.getClass());
		response.sendError(HttpResponse.SC_NOT_FOUND);//返回404
	
	}
	
	/**
	 * 跳转后浏览器地址栏变化<br>
	 * <p>
	 * 传值出去的话，只能在url中带parameter或者放在session中，无法使用request.setAttribute来传递。<br> 
	 * @param url 重定向访问的路径  http://127.0.0.1:8080/ape/index.html
	 */
	protected void sendRedirect(String url)
	{
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			log.error("访问的URL{}不存在！\n"+e,url);
		}
		return;
	}
	
	/**
	 * Servlet页面跳转的路径是相对路径。forward方式只能跳转到本web应用中的页面上。
	 *<br>
	 *	跳转后浏览器地址栏不会变化。
	 *<br>
	 *使用这种方式跳转，传值可以使用三种方法：url中带parameter，session，request.setAttribute
	 *
	 * @param uri 跳转的相对路径 如：input.jsp
	 */
	public void forward(String uri) {
		try {
			request.getRequestDispatcher(uri).forward(request, response);
		} 
		catch (ServletException e){
			log.error("访问的URI{}不存在！\n"+e,uri);
		}
		catch (IOException e) {
			log.error("访问的URI{}不存在！\n"+e,uri);
		}
		return;
	}
	
	
	/**
	 * text/html 格式的返回
	 * @param html 显示的html
	 */
	protected void printHtml(String html) 
	{
		try {
			response.setContentType("text/html; charset="+request.getCharacterEncoding());
			response.getWriter().print(html);
		} catch (IOException e) {
			log.error("显示text/html{}报错\n"+e,html);
		}
		return;
	}
	
	/**
	 * application/json 格式的返回json
	 * @param json 返回的json
	 */
	protected void printJson(String json) {
		try {
			response.setContentType("application/json; charset="+request.getCharacterEncoding());
			response.getWriter().print(json);
		} catch (IOException e) {
			log.error("返回application/json{}报错\n"+e,json);
		}
		return;
	}
	
	

}
