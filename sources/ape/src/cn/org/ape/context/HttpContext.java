package cn.org.ape.context;

import java.io.IOException;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.org.ape.http.HttpRequest;
import cn.org.ape.http.HttpResponse;


/**
 * 主要用于 http的相应 ：<br>
 * <li> 定向跳转
 * <li> 重定向
 * <li> html/text显示
 * <li>Json的发生
 * 
 * <br>
 * @author 陈磊
 * @since jdk1.6
 * @version 2012/9/2
 *
 */
public class HttpContext 
{
	private static final Logger log = LoggerFactory.getLogger(HttpContext.class);
	
	/**
	 * Servlet页面跳转的路径是相对路径。forward方式只能跳转到本web应用中的页面上。
	 *<br>
	 *	跳转后浏览器地址栏不会变化。
	 *<br>
	 *使用这种方式跳转，传值可以使用三种方法：url中带parameter，session，request.setAttribute
	 *
	 * @param request
	 * @param response
	 * @param uri 跳转的相对路径 如：input.jsp
	 */
	public static void forward(HttpRequest request,HttpResponse response,String uri) {
		try {
			request.getRequestDispatcher(uri).forward(request, response);
		} catch (ServletException | IOException e) {
			log.error("访问的URI{}不存在！\n"+e,uri);
		}
		return;
	}
	
	/**
	 * 跳转后浏览器地址栏变化<br>
	 * <p>
	 * 传值出去的话，只能在url中带parameter或者放在session中，无法使用request.setAttribute来传递。<br> 
	 * @param url 重定向访问的路径  http://127.0.0.1:8080/ape/index.html
	 * @param response
	 */
	public static void sendRedirect(HttpResponse response,String url)
	{
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			log.error("访问的URL{}不存在！\n"+e,url);
		}
		return;
	}
	
	/**
	 * text/html 格式的返回
	 * @param request
	 * @param response
	 * @param html 显示的html
	 */
	protected static void printHtml(HttpRequest request,HttpResponse response,String html) 
	{
		try {
			response.setContentType("text/html; charset="+request.getCharacterEncoding());
			response.getWriter().print(html);
		} catch (IOException e) {
			log.error("显示text/html{}报错\n"+e,html);
		}
		return;
	}

}
