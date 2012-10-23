package cn.org.ape;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.objectweb.asm.ClassReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.org.ape.asm.ClassParsing;
import cn.org.ape.context.BeanParameter;
import cn.org.ape.http.HttpRequest;
import cn.org.ape.http.HttpResponse;
import cn.org.ape.http.RequestContext;
import cn.org.ape.http.ResponseContext;


/**
 * Servlet的接口 默认的URL :servlet/*<br>
 * 同时也可以配置文件 如：<br >
 * 
 *  <br >
 * <servlet><br >
 *   <servlet-name>servlet</servlet-name><br >
 *   <servlet-class>cn.org.ape.ApeServlet</servlet-class><br >
 *   <init-param><br >
 *   <param-name>module</param-name><br >
 *   <param-value>module.properties</param-value><br >
 *   </init-param><br >
 *   <!-- 是否使用 异步处理  默认是false 不使用-->
 *   <async-supported>true</async-supported><br >
 *   <multipart-config></multipart-config><br >
 * </servlet><br >
 * <br >
 * <br >
  * <servlet-mapping><br >
 *   <servlet-name>servlet</servlet-name><br >
 *   <url-pattern>/action/*</url-pattern><br >
 * </servlet-mapping><br >
 * <br >
 * @author 陈磊
 *
 */
@WebServlet(urlPatterns= "/servlet/*")
public class ApeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger log= LoggerFactory.getLogger(ApeServlet.class);
	
	/**
	 * 初始化,主要是完成module的内存加载<br>
	 * 从配置文件中读取module对于类
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		String module = getInitParameter("module"); //<init-param>
		if (module==null) 
		{
			module = "ape.properties"; //设置默认的配置文件
		}
		this._createModules(module);
	}
	
	
  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	  HttpRequest request=new RequestContext(req);
	  HttpResponse response = new ResponseContext(resp);
	 String module= request.getModule();//取得调用类
	 String action= request.getAction();  //取得调方法
	 BaseAction baseAction = modules.get(module); //初始调用类
	 if (baseAction==null) 
	 {
		 //没有找到Module 返回404
		response.sendError(HttpResponse.SC_NOT_FOUND);//返回404
		return ;
	 }
	 	
	 //通过asm 把request的属性赋值到调用类中属性中
	  ClassParsing cp = new ClassParsing();
	  ClassLoader classloader = baseAction.getClass().getClassLoader();
	  if (classloader != null)
	    {
		  //取得调用类的url
	      URL url = classloader.getResource(StringUtils.replace(baseAction.getClass().getName(), ".", "/") + ".class");
	      File file = FileUtils.toFile(url);
	      if (file != null)
	      {
	        ClassReader cr = new ClassReader(FileUtils.openInputStream(file));
	        cr.accept(cp, 0);
	      }

	    }
	Map<?, ?> map=cp.getMap();
	baseAction.init(request, response); //初始化baseAction
	BeanParameter.populate(baseAction, request,map);//初始化bean
	if (StringUtils.isNotEmpty(action))
	{
		baseAction.run(); //执行
	}
	else 
	{
		baseAction.execute(); //默认执行
	}
	
 
  }
  
  
  /**
   * 销毁
   */
  @Override
  public void destroy() {
		// TODO Auto-generated method stub
	  	log.debug("destroy");
		super.destroy();
	}

  
  /**
   * 把配置文件的内容 存放于 HashMap<String, BaseAction>
   * @param module 配置文件的名称
   */
  private  void _createModules(String module)  
  {  
	  
		try {
			//module这个文件会自动在下面几个地方被搜寻：当前目录 、用户主目录 、classpath
			PropertiesConfiguration conf = new PropertiesConfiguration(module);
			//当配置文件的内容发生改变了的时候，Commons　Configuration能自 动重新读取配置文件的内容。
			conf.setReloadingStrategy(new FileChangedReloadingStrategy()); 
			
			@SuppressWarnings("unchecked")
			List<String> keys=IteratorUtils.toList(conf.getKeys());
			for (String key : keys) 
			{
				String moduleClass = conf.getString(key);
				BaseAction baseAction = _retrieveModule(moduleClass);
				modules.put(key, baseAction);
			}
		} catch (ConfigurationException e) {
			log.error("找不到配置文件 : {}\n"+e, module);
		}	
  }
  
  /**
   * 通过类的名称 ，返回名称对于的对象。如果没有找到该对象，返回NULL
   * @param moduleClass 类的名称 如：test.action.TestAction
   * @return 返回 Object对象
   */
  private  BaseAction _retrieveModule(String moduleClass) 
  {
	  try {
		BaseAction baseAction = (BaseAction) Class.forName(moduleClass).newInstance();
		return baseAction;
	} catch (InstantiationException e){
		log.error("不存在 ：{} 类\n"+e,moduleClass);
		return null;
	}
	  catch (IllegalAccessException e){
		  log.error("不存在 ：{} 类\n"+e,moduleClass);
			return null;
	  }
		catch (ClassNotFoundException e) {
		log.error("不存在 ：{} 类\n"+e,moduleClass);
		return null;
		
	}
  }
  
 

  private final static HashMap<String, BaseAction> modules = new HashMap<String, BaseAction>();

}
