package cn.org.ape;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.org.ape.http.HttpRequest;
import cn.org.ape.http.HttpResponse;
import cn.org.ape.http.RequestContext;
import cn.org.ape.http.ResponseContext;

@WebFilter(urlPatterns="/servlet/*",
	initParams={@WebInitParam(name="encoding",value = "UTF-8")})
public class ApeFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(ApeFilter.class);
	private static final String ENCODING = "encoding";
	private static String encoding; //字体编码 默认utf8
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 该过滤器主要用于request的转码
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		HttpRequest request=new RequestContext((HttpServletRequest) servletRequest);
		HttpResponse response = new ResponseContext((HttpServletResponse) servletResponse);
		// 主要是修改post方式提交的数据
		request.setCharacterEncoding(encoding); //设设置编码 
		//修改get方式提交的数据
		/**
		 * get方式的修改只有将取得的数据强行转码
		 * value = new String(value.getBytes("ISO-8859-1"),  
		 *	        "UTF-8");
		 *但这里要知道原始的编码格式 如 tomcat 默认的"ISO-8859-1" 但也可以修改成UTF-8
		 *所以这里不做Get方式的转码
		 * 
		 */
		chain.doFilter(request, response);
		
	}

	/**
	 * 初始化系统编码 :<p>
	 * <p><param-name>encoding</param-name>
	 *	<p><param-value>UTF-8</param-value>
  	 *	<p></init-param>
  	 * 系统默认的是UTF-8
	 */
	@Override
	public void init(FilterConfig conf) throws ServletException {
		encoding=conf.getInitParameter(ENCODING);
		if (StringUtils.isEmpty(encoding)) 
		{
			encoding="UTF-8";//系统默认使用UTF-8
		}
		log.debug("系统采用{}编码过滤",encoding);
		
	}
	
	
	

	

}
