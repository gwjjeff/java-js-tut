package script;

import static java.lang.System.err;
import static java.lang.System.out;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Before;
import org.junit.Test;

public class InvocableTest {
	ScriptEngineManager manager;
	ScriptEngine engine;
	@Before
	public void initEngine() {
		manager = new ScriptEngineManager();
		engine = manager.getEngineByName("javascript");
	}
	@Test
	public void testFunction() {
		String name = "jeff测试";
		if (engine instanceof Invocable) {
			try {
				engine.eval("" +
						"function reverse(name) {					" +
						"    var output = '';						" +
						"    for (i = 0; i <= name.length; i++) {	" +
						"        output = name.charAt(i) + output	" +
						"    }										" +
						"    return output;							" +
						"}											" +
			"");
				Invocable invokeEngine = (Invocable) engine;
				Object o = invokeEngine.invokeFunction("reverse", name);
				out.printf("翻转后的字符串：%s", o);
			} catch (NoSuchMethodException e) {
				err.println(e);
			} catch (ScriptException e) {
				err.println(e);
			}
		} else {
			err.println("这个脚本引擎不支持动态调用");
		}
	}
	@Test
	public void testEvalBool() {
		evalBool("1<2");
		evalBool("1>2");
		evalBool("1#2");
	}
	public void evalBool(String boolExpr) {
		Object o=null;
		try {
			o = engine.eval("(function(){if("+boolExpr+"){ return true;}else{return false;}})()");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		out.printf("结果：%s", o);
		out.println();
	}
	public boolean evalBool2(String boolExpr) {
		Object o=null;
		try {
			o = engine.eval("(function(){if("+boolExpr+"){ return true;}else{return false;}})()");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return "true".equals(o) ? true : false;
	}
	@Test
	public void testListEngines() {
		List<ScriptEngineFactory> factoryList = manager.getEngineFactories();
	    for (ScriptEngineFactory factory : factoryList) {
	    	System.out.println(factory.getEngineName());
	        System.out.println(factory.getEngineVersion());
	        System.out.println(factory.getLanguageName());
	        System.out.println(factory.getLanguageVersion());
	        System.out.println(factory.getExtensions());
	        System.out.println(factory.getMimeTypes());
	        System.out.println(factory.getNames());
	    }
	}
	@Test
	public void testEvalScript() {
		String script = "print ('www.java2s.com')";
		try {
			engine.eval(script);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void runScriptFile() {
	    try {
	      FileReader reader = new FileReader("src/script/yourFile.js");
	      engine.eval(reader);
	      reader.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	@Test
	public void testBinding() throws ScriptException {
		engine.put("a", 1);
		engine.put("b", 5);

		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		Object a = bindings.get("a");
		Object b = bindings.get("b");
		System.out.println("a = " + a);
		System.out.println("b = " + b);

		Object result = engine.eval("c = a + b;");
		System.out.println("a + b = " + result);
	}
	interface Adder {
		int add(int a, int b);
	}
	@Test
	public void testInterface() throws Exception {
		engine.eval("function add (a, b) {c = a + b; return c; }");
	    Invocable jsInvoke = (Invocable) engine;

	    Object result1 = jsInvoke.invokeFunction("add", new Object[] { 10, 5 });
	    System.out.println(result1);

	    Adder adder = jsInvoke.getInterface(Adder.class);
	    int result2 = adder.add(10, 5);
	    System.out.println(result2);
	}
	@Test
	public void testThread() throws Exception {
		engine.eval("function run() {print('www.java2s.com');}");
		Invocable invokeEngine = (Invocable) engine;
		Runnable runner = invokeEngine.getInterface(Runnable.class);
		Thread t = new Thread(runner);
		t.start();
		t.join();
	}
	@Test
	public void testCompiledScript() throws Exception {
		Compilable jsCompile = (Compilable) engine;
	    CompiledScript script = jsCompile.compile("function hi () {print ('www.java2s.com !'); }; hi ();");
	    for (int i = 0; i < 5; i++) {
	      script.eval();
	    }
	    System.out.println();
	    engine.put("counter", 0);
	    script = jsCompile.compile("function count(){counter=counter+1;return counter;}; count();");
        System.out.println(script.eval());
        System.out.println(script.eval());
        System.out.println(script.eval());
	}
	@Test
	public void testEvalResult() {
		try {
			Double hour = (Double) engine.eval("var date = new Date();" + "date.getHours();");
			String msg;
			if (hour < 10) {
				msg = "Good morning";
			} else if (hour < 16) {
				msg = "Good afternoon";
			} else if (hour < 20) {
				msg = "Good evening";
			} else {
				msg = "Good night";
			}
			System.out.println(hour);
			System.out.println(msg);
		} catch (ScriptException e) {
			System.err.println(e);
		}
	}
	@Test
	public void testArgumentAndVar() {
		try {
			engine.put("name", "abcde");
			engine.eval("var output = '';for (i = 0; i <= name.length; i++) {"
					+ "  output = name.charAt(i)+'-' + output" + "}");
			String name = (String) engine.get("output");
			System.out.println(name);
		} catch (ScriptException e) {
			System.err.println(e);
		}
	}
	@Test
	public void testJavaObject() throws Exception {
		engine.eval("importPackage(javax.swing);var optionPane =JOptionPane.showMessageDialog(null, 'Hello!');");
		// engine.eval("importClass(javax.swing.JOptionPane);var optionPane =JOptionPane.showMessageDialog(null, 'Hello!');");
	}
	@Test
	public void TestJavaList() throws Exception {
		List<String> list1 = Arrays.asList("A", "B", "C", "D", "E");
	    engine.put("list1", list1);
	    String jsCode = "var index; var values =list1.toArray();"
	        + "println('Java to Javascript');for(index in values) {"
	        + "  println(values[index]);}";
	    engine.eval(jsCode);

	    System.out.println("Javascript to Java");
	    jsCode = "importPackage(java.util);var list2 = Arrays.asList(['A', 'B', 'C']); ";
	    engine.eval(jsCode);
	    List<String> list2 = (List<String>) engine.get("list2");
	    for (String val : list2) {
	      System.out.println(val);
	    }
	}
	@Test
	public void testObjectMethod() throws Exception {
		engine.eval(new InputStreamReader(this.getClass().getResourceAsStream("scripting.js")));
		System.out.println("=======");
		List<String> list1 = (List<String>) engine.get("list1");
		if (list1 != null) {
			for (String s : (List<String>) list1) {
				System.out.println(s);
			}
		}
		System.out.println("=======");
		Invocable engineInv = (Invocable) engine;
		Object obj = engine.get("listObject");
		Object list2 = engineInv.invokeMethod(obj, "getList2");
		if (list2 != null) {
			for (String s : (List<String>) list2) {
				System.out.println(s);
			}
		}
	}
	@Test
	public void testJAVA() throws Exception {
		engine.eval(new InputStreamReader(this.getClass().getResourceAsStream("greeting.js")));
	}
}