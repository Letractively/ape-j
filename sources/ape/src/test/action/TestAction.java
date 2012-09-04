package test.action;

import java.io.IOException;
import java.util.Date;

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
	  
	  response.setContentType("text/html; charset=UTF-8");
	  System.out.println(getUser().getName());
	  System.out.println(id);
	  System.out.println(date);
	  response.getWriter().print(user.getName());
}

  private Date date ;
  public Date getDate() {
	return date;
}

public void setDate(Date date) {
	this.date = date;
}

public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}

public User getUser() {
	return user;
}

public void setUser(User user) {
	this.user = user;
}

private Integer id ;
 private User user ;
  
  
}
