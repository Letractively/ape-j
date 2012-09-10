package cn.org.ape.asm;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 通过asm 取得类中的变量
 * @author 陈磊
 *
 */
public  class ClassParsing extends ClassVisitor {

	private Map<String, String> map = new HashMap<String, String>();
	public ClassParsing() 
	{
		super(Opcodes.ASM4);
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		String string = StringChange.getString(desc);
		if(StringUtils.isNotEmpty(string))
		{
			map.put(name, string);
		}
		
		return null;
	}
	
	/**
	 * 取得 属性名称和属性类型
	 * @return
	 */
	public Map<String, String> getMap() {
		return map;
		
	}

}
