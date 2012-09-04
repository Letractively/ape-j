package cn.org.ape.asm;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;

public class StringChange 
{
  private static ListOrderedMap listOrderedMap = new ListOrderedMap();
	static
	{
		listOrderedMap.put("Z", "java.lang.Boolean");//boolean
		listOrderedMap.put("C", "java.lang.String");//char
		listOrderedMap.put("B", "java.lang.String");//byte
		listOrderedMap.put("S", "java.lang.Short");//short
		listOrderedMap.put("I", "java.lang.Integer");//int
		listOrderedMap.put("F", "java.lang.Float");//float
		listOrderedMap.put("L", "java.lang.Long");//long
		listOrderedMap.put("D", "java.lang.Double");//double
	}
	
	public static String getString(String string) 
	{
		if (string.length()==1) 
		{
			string = _vlang(string);
		}
		else
		{
			if (StringUtils.startsWith(string, "[")) {
				return null;
			}
			string = StringUtils.replaceChars(string, "/", ".");
			string = StringUtils.replaceChars(string, ";", "");
			if (StringUtils.startsWith(string, "L")) 
			{
				string = StringUtils.replace(string, "L", "", 1);
			}
		}
		return string;
		
	}
	
	private static String _vlang(String string) {
		
		return (String) listOrderedMap.get(string);
		
	}
	
	
	
}
