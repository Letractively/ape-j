package cn.org.ape.context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.ListOrderedMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.objectweb.asm.ClassReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.org.ape.asm.ClassParsing;
import cn.org.ape.http.HttpRequest;

public class BeanParameter 
{
	private static final Logger log = LoggerFactory.getLogger(BeanParameter.class);
	private HttpRequest request;
	public BeanParameter(HttpRequest request)
	{
		this.request = request;
		
	}
	public void populate(Object bean) 
	{
		//populate( bean, request);	
	}
	
	/**
	 * 初始化 beanUtils
	 * @param bean 要初始的对象
	 * @param request 前台页面传入的数据
	 * @param m 
	 */
	public static void  populate(Object bean,HttpRequest request, Map m) 
	{
		ListOrderedMap orderedMap = new ListOrderedMap();
		Map map=request.getParameterMap();
		orderedMap.putAll(map);
		PropertyUtils propertyUtils = new PropertyUtils();
		List<String> list=orderedMap.asList();
		for (String string : list) 
		{
			String[] name = StringUtils.split(string, ".");
			
			if (name.length==1) 
			{
				String objectString= (String) m.get(string);
				if (propertyUtils.isReadable(bean, string)&&propertyUtils.isWriteable(bean, string)&&objectString!=null)
				{
					try {
						//Object object = propertyUtils.getProperty(bean, string);
						//String objectString =object!=null?object.getClass().toString():"";
						if (StringUtils.containsIgnoreCase(objectString, "java.lang.String")) 
						{
							propertyUtils.setProperty(bean, string, request.getParameter(string));
						}else if (StringUtils.containsIgnoreCase(objectString, "java.lang.Integer")
								||StringUtils.containsIgnoreCase(objectString, "java.lang.Short")
								||StringUtils.containsIgnoreCase(objectString, "java.lang.Long")
								||StringUtils.containsIgnoreCase(objectString, "java.lang.Float")
								||StringUtils.containsIgnoreCase(objectString, "java.lang.Double")
								||StringUtils.containsIgnoreCase(objectString, "java.lang.Byte")
								||StringUtils.containsIgnoreCase(objectString, "java.math.BigInteger")
								||StringUtils.containsIgnoreCase(objectString, "java.math.BigDecimal")) 
						{
							if (NumberUtils.isNumber(request.getParameter(string))) {
								propertyUtils.setProperty(bean, string, NumberUtils.createNumber(request.getParameter(string)));
							}
							
						}else if (StringUtils.containsIgnoreCase(objectString, "java.util.Date")) 
						{
							String s = request.getParameter(string);
							if (StringUtils.isNotEmpty(s))
							{
								String[] parsePatterns ={"yyyy/MM/dd","yyyy/MM/dd HH:mm:ss",
										"yyyy.MM.dd","yyyy.MM.dd HH:mm:ss", 
										"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss",
										"HH:mm:ss"};
								Date parsedDate = null;
								try {
									parsedDate= DateUtils.parseDate(s,
											parsePatterns);
								} catch (ParseException e ) {
									log.error("{}不是正确的时间格式",s);
								}
								propertyUtils.setProperty(bean, string, parsedDate);
							}	
						}
						
					} catch (IllegalAccessException | InvocationTargetException
							| NoSuchMethodException e) {
					
						e.printStackTrace();
					}
				}
			}else if (name.length==2) 
			{
				String objectString= (String) m.get(name[0]);
				if (StringUtils.isNotEmpty(objectString))
				{
					try {
				
						Object object=Class.forName(objectString).newInstance();
						if (propertyUtils.isReadable(bean, name[0])&&propertyUtils.isWriteable(bean, name[0])) 
						{
							propertyUtils.setProperty(bean, name[0], object);
							ClassParsing cp = new ClassParsing() ;
							InputStream io=new FileInputStream("G:/work space/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/ape/WEB-INF/classes/test/action/User.class");
							ClassReader cr = new ClassReader(io);
							cr.accept(cp, 0);
							Map ma=cp.getMap();
							String ob= (String) ma.get(name[1]);
							if (propertyUtils.isReadable(bean, string)&&propertyUtils.isWriteable(bean, string)&&ob!=null)
							{
								if (StringUtils.containsIgnoreCase(ob, "java.lang.String")) 
								{
									propertyUtils.setProperty(bean, string, request.getParameter(string));
								}else if (StringUtils.containsIgnoreCase(ob, "java.lang.Integer")
										||StringUtils.containsIgnoreCase(ob, "java.lang.Short")
										||StringUtils.containsIgnoreCase(ob, "java.lang.Long")
										||StringUtils.containsIgnoreCase(ob, "java.lang.Float")
										||StringUtils.containsIgnoreCase(ob, "java.lang.Double")
										||StringUtils.containsIgnoreCase(ob, "java.lang.Byte")
										||StringUtils.containsIgnoreCase(ob, "java.math.BigInteger")
										||StringUtils.containsIgnoreCase(ob, "java.math.BigDecimal")) 
								{
									if (NumberUtils.isNumber(request.getParameter(string))) {
										propertyUtils.setProperty(bean, string, NumberUtils.createNumber(request.getParameter(string)));
									}
									
								}else if (StringUtils.containsIgnoreCase(ob, "java.util.Date")) 
								{
									String s = request.getParameter(string);
									if (StringUtils.isNotEmpty(s))
									{
										String[] parsePatterns ={"yyyy/MM/dd","yyyy/MM/dd HH:mm:ss",
												"yyyy.MM.dd","yyyy.MM.dd HH:mm:ss", 
												"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss",
												"HH:mm:ss"};
										Date parsedDate = null;
										try {
											parsedDate= DateUtils.parseDate(s,
													parsePatterns);
										} catch (ParseException e ) {
											log.error("{}不是正确的时间格式",s);
										}
										propertyUtils.setProperty(bean, string, parsedDate);
									}	
								}
							}
							
							
							
						}
					
					
					} catch (InstantiationException | IllegalAccessException
							| ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
		
	}
	
	/**
	 * 设置bean的数据
	 * @param bean
	 * @param request
	 */
	public static void setBean(Object bean,HttpRequest request)
	{
		Map properties = request.getParameterMap();
		try {
			BeanUtils.populate(bean, properties );
		} catch (IllegalAccessException | InvocationTargetException e) {
			log.error("初始化数据");
		}
	}
	

}
