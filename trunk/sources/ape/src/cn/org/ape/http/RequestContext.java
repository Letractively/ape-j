package cn.org.ape.http;


import java.util.StringTokenizer;

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

	/**
	 * 取得真实的ip地址<br>
	 * 由于如果网站通过Apache等类似的代理软件网站就不能取到客户端的真实IP，而是代理服务器的IP<br>
	 * 通过重新改方法，就可以取得真实的客户端IP
	 * 如果ip为空 返回 0.0.0.0
	 */
	@Override
	public String getRemoteAddr()
	{
		// We look if the request is forwarded
		// If it is not call the older function.
        String ip = super.getHeader("x-forwarded-for");
        
        if (ip == null) {
        	ip = super.getRemoteAddr();
        }
        else {
        	// Process the IP to keep the last IP (real ip of the computer on the net)
            StringTokenizer tokenizer = new StringTokenizer(ip, ",");

            // Ignore all tokens, except the last one
            for (int i = 0; i < tokenizer.countTokens() -1 ; i++) {
            	tokenizer.nextElement();
            }
            
            ip = tokenizer.nextToken().trim();
            
            if (ip.equals("")) {
            	ip = null;
            }
        }
        // If the ip is still null, we put 0.0.0.0 to avoid null values
        if (ip == null) {
        	ip = "0.0.0.0";
        }
        
        return ip;
	}
	
	/**
	 * 重写getParameter方法。主要是处理get方式提交的数据编码问题
	 */
//	@Override
//	public String getParameter(String name) {
//		 String value = super.getParameter(name); 
//		 if (StringUtils.isEmpty(value)) //把 "" 和 null 直接返回 null
//		 {
//			return null;
//		 }
//		String method = this.getMethod();
//		if (StringUtils.equalsIgnoreCase(method, "get")) //不考虑大小 写的比较
//		{
//			try {
//				value = new String(value.getBytes("ISO-8859-1"),  
//				        this.getCharacterEncoding());
//				System.out.println(value+"123");
//				
//			} catch (UnsupportedEncodingException e) {
//				log.debug("{}转码成{}错误！",value,this.getCharacterEncoding());
//			}  
//		}
//		return value;
//	}
	
	
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

	
	/**
	 * 同http中的：<br>
	 * String path = request.getContextPath();<br>
	 *String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	 * <br>
	 * <p>
	 * 取得当前工程的URL如： http://127.0.0.1:8080/ape/
	 */
	@Override
	public String basePath() {
		String path = getContextPath(); //取得工程名ape
		
		return getScheme()+"://"+getServerName()+":"+getServerPort()+path+"/";
	}
	

}
