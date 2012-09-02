package test.action;

import java.io.IOException;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.org.ape.BaseAction;




public class TestAction extends BaseAction {
	private final static Logger log= LoggerFactory.getLogger(TestAction.class);
  @Override
	protected void execute() throws ServletException, IOException {
	  response.setContentType("text/html; charset=UTF-8");
	  response.getWriter().print("你好，陈磊！");
	  log.debug("1234");
	}
  
  public void show() throws IOException {
	  response.setContentType("text/html; charset=UTF-8");
	  response.getWriter().print("你好，陈磊！");
	  log.debug("321");
}
  public void login() throws IOException {
	  String user = request.getParameter("user");
	  response.setContentType("text/html; charset=UTF-8");
	  System.out.println(user);
	  response.getWriter().print(user);
}

}
