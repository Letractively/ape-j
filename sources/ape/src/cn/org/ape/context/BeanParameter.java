package cn.org.ape.context;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.ListOrderedMap;

import org.apache.commons.io.FileUtils;
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
	@SuppressWarnings("rawtypes")
	public void populate(Object bean, Map m) 
	{
		populate( bean, request,m);	
	}
	
	/**
	 * 初始化 beanUtils
	 * @param bean 要初始的对象
	 * @param request 前台页面传入的数据
	 * @param m 
	 */
	@SuppressWarnings("static-access")
	public static void  populate(Object bean,HttpRequest request, Map<?, ?> m) 
	{
		ListOrderedMap orderedMap = new ListOrderedMap();
		Map<?, ?> map=request.getParameterMap();
		orderedMap.putAll(map);
		PropertyUtils propertyUtils = new PropertyUtils();
		@SuppressWarnings("unchecked")
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
						if (StringUtils.containsIgnoreCase(objectString, "java.lang.String")) 
						{
							//设置String
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
								//设置Number
								propertyUtils.setProperty(bean, string, NumberUtils.createNumber(request.getParameter(string)));
							}
							
						}else if (StringUtils.containsIgnoreCase(objectString, "java.util.Date")) 
						{
							String s = request.getParameter(string);
							if (StringUtils.isNotEmpty(s))
							{
								//设置时间
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
						
					} catch (IllegalAccessException e)
					{log.error("给类{}赋值错误！",bean);}
					catch (InvocationTargetException e)
					{log.error("给类{}赋值错误！",bean);}
					catch ( NoSuchMethodException e) 
					{
						log.error("给类{}赋值错误！",bean);
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
							if (PropertyUtils.getProperty(bean, name[0]) == null)
				              {
								//如果没有初始化，求初始
				                PropertyUtils.setProperty(bean, name[0], object);
				              }
				              ClassParsing cp = new ClassParsing();
				              ClassLoader classloader = object.getClass().getClassLoader();
				              if (classloader != null) {
				                URL url = classloader.getResource(StringUtils.replace(object.getClass().getName(), ".", "/") + ".class");
				                File file = FileUtils.toFile(url);
				                if (file != null)
				                {
				                  ClassReader cr = new ClassReader(FileUtils.openInputStream(file));
				                  cr.accept(cp, 0);
				                }
				              }
							Map<?, ?> ma=cp.getMap();
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
					
					
					} 
					catch (InstantiationException e ){}
					catch (IllegalAccessException e){}
					catch (ClassNotFoundException e){} 
					catch (InvocationTargetException e){}
					catch (NoSuchMethodException e){}
					catch (IOException e) {
						log.error("给类{}赋值错误！",bean);
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
		Map<?, ?> properties = request.getParameterMap();
	
			try {
				BeanUtils.populate(bean, properties );
			} catch (IllegalAccessException e) {
				log.error("初始化数据");
			} catch (InvocationTargetException e) {
				log.error("初始化数据");
			}
		
	}
	

}
