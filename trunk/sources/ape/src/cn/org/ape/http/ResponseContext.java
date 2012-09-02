package cn.org.ape.http;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ResponseContext extends HttpServletResponseWrapper implements HttpResponse {

	public ResponseContext(HttpServletResponse response) {
		super(response);
		// TODO Auto-generated constructor stub
	}


}
