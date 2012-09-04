package cn.org.ape.http;

import javax.servlet.http.HttpServletRequest;

public interface HttpRequest extends HttpServletRequest {

	public String basePath();
	public String getAction();
	public String getModule();
}
