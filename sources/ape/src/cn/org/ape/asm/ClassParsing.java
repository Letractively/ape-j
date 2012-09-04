package cn.org.ape.asm;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public  class ClassParsing extends ClassVisitor {

	private Map map = new HashMap();
	public ClassParsing() 
	{
		super(Opcodes.ASM4);
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		System.out.println(" " + desc + " " + name);
		String string = StringChange.getString(desc);
		if(StringUtils.isNotEmpty(string))
		{
			map.put(name, string);
		}
		
		return null;
	}
	
	public Map getMap() {
		return map;
		
	}

}
