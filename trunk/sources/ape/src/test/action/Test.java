package test.action;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.objectweb.asm.ClassReader;

import cn.org.ape.asm.ClassParsing;

public class Test {

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws ParseException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		// TODO Auto-generated method stub
		ClassParsing cp = new ClassParsing() ;
		ClassReader cr = new ClassReader("test/action/TestAction");
		cr.accept(cp, 0);
		Map map=cp.getMap();
		ListOrderedMap orderedMap = new ListOrderedMap();
		orderedMap.putAll(map);
		for (Object string : orderedMap.asList()) {
			System.out.println(string);
		}
		
		String f ="com.cn.com";
		f= f+".class";
		//Object ob = Class.forName("java.lang.String").newInstance();
		System.out.println(f);
		
		
		
		
	}

}
