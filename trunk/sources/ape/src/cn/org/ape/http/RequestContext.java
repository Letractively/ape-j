package cn.org.ape.http;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestContext extends HttpServletRequestWrapper implements HttpRequest {
   private final static Logger log=LoggerFactory.getLogger(RequestContext.class); 
	
	public RequestContext(HttpServletRequest request) {
		super(request);
	}

	
	@Override
	public String getParameter(String name) {
		 String value = super.getParameter(name); 
		 if (StringUtils.isEmpty(value)) //把 "" 和 null 直接返回 null
		 {
			return null;
		 }
		String method = this.getMethod();
		if (StringUtils.equalsIgnoreCase(method, "get")) //不考虑大小 写的比较
		{
			try {
				value = new String(value.getBytes("ISO-8859-1"),  
				        this.getCharacterEncoding());
				System.out.println(value+"123");
				
			} catch (UnsupportedEncodingException e) {
				log.debug("{}转码成{}错误！",value,this.getCharacterEncoding());
			}  
		}
		return value;
	}/**
	 * 重写getParameter方法。主要是处理get方式提交的数据编码问题
	 */
	
	
	/**
	 * 用于返回请求的方法
	 */
	@Override
	public String getAction() {
		String module_action=this._getModuleAction();
		String action = null;
		if (StringUtils.isNotEmpty(module_action))
		{
			String[] parts= StringUtils.split(module_action, ".");
			if (parts.length>1) 
			{
				action=parts[1];
				action=StringUtils.split(action, "?")[0];
				//action=StringUtils.split(action, "#")[0];
				//action=StringUtils.split(action, "&")[0];
			}
		}
		return action;
	}

	
	/**
	 * 用于返回请求的执行类
	 */
	@Override
	public String getModule() {
		String module_action=this._getModuleAction();
		if (StringUtils.isNotEmpty(module_action))
		{
			return StringUtils.split(module_action, ".")[0];
		}
		log.error("请求module为空");
		return null;
	}

	
	/**
	 * 取得Module_Action
	 * @return
	 */
	private String _getModuleAction() 
	{
		String uri=this.getRequestURI();//URI
		String servletPath = this.getServletPath();//servlet访问路径
		String path= this.getContextPath();//项目名称
		String module_action=null;
		String[] parts=StringUtils.split(uri,"/");
		if(StringUtils.isNotEmpty(path)&&StringUtils.isNotEmpty(servletPath)&&parts.length>2) 
		{
			module_action=parts[2];	
			
		}else if (StringUtils.isNotEmpty(path+servletPath)&&parts.length>1) 
		{
			module_action=parts[1];
		}
		else if (parts.length>1) {
			module_action=parts[0];
		}
		
		return module_action;
	}
	

}
