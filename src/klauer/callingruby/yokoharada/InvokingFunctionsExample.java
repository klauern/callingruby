/*
 * In this example, Yoko Harada shows an example of how to invoke various types of
 * methods in JRuby.  Some of the features of the JSR-223 API is being able to 
 * pass multiple arguments to a Ruby method, getting back various types, and even
 * returning multiple types back (Ruby allows for multiple return values).
 * 
 * InvokeFunction() is similar to invokeMethod(), where invokeFunction calls
 * functions in the global namespace, also called the top-level namespace. See
 * the invokeMethod() example for calling methods held within a class or module.
 * 
 * See the blog entry: 
 * http://yokolet.blogspot.com/2008/03/tips-for-jruby-engine-how-to-invoke.html
 */
package klauer.callingruby.yokoharada;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Yoko Harada
 */
public class InvokingFunctionsExample {

    /**
     * There is one exception to method calls in Ruby that cannot be done using
     * the invokeFunction() and invokeMethod() calls: Blocks.  Since ruby can 
     * yield to another method a block of code to execute, these types of methods
     * will not work with this API.  According to yoko, there is no way to do this
     * currently in Java. 
     * 
     * Perhaps with the ongoing closures debate in Java 7 we might see this functionality
     * fit in with the scripting API.
     */
    
    /**
     * This constructor creates the basis for this set of methods explaining how to
     * invoke various types of methods in JRuby.
     * @throws javax.script.ScriptException
     * @throws java.lang.NoSuchMethodException
     */
    private InvokingFunctionsExample()
            throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("jruby");
        invokeSimpleFunction(engine);
        invokeFunctionWithArguments(engine);
        Map map = new HashMap();
        map.put("ruby", "red");
        map.put("pearl", "white");
        map.put("rhino", "gray");
        map.put("rose", "red");
        map.put("nimbus", "gray");
        map.put("gardenia", "white");
        map.put("camellia", "red");
        invokeFunctionWithReturns(engine, map);
        invokeFunctionWithGlobalVariables(engine, map);
    }

    /**
     * A simple funciton taking no arguments.  The script is provided, in the string,
     * of course.
     * @param engine
     * @throws javax.script.ScriptException
     * @throws java.lang.NoSuchMethodException
     */ 
    private void invokeSimpleFunction(ScriptEngine engine)
            throws ScriptException, NoSuchMethodException {
        String script =
                "def say_something()" +
                "puts \"I\'m sleepy because I went to bed three in the morning!\"" +
                "end";
        engine.eval(script);
        Invocable invocable = (Invocable) engine;
        invocable.invokeFunction("say_something");
    }

    /**
     * This will invoke a Ruby method with multiple arguments.  Since Ruby can
     * take in a parameter with various methods (see *list), we can still
     * pass to the invokeFunction() just passing it multiple parameters as we
     * see fit.
     * @param engine
     * @throws javax.script.ScriptException
     * @throws java.lang.NoSuchMethodException
     */
    private void invokeFunctionWithArguments(ScriptEngine engine)
            throws ScriptException, NoSuchMethodException {
        String script =
                "def come_back(type, *list)" +
                "print \"#{type}: #{list.join(\',\')}\";" +
                "print \"...\";" +
                "list.reverse_each {|l| print l, \",\"};" +
                "print \"\n\";" +
                "end";
        engine.eval(script);
        Invocable invocable = (Invocable) engine;
        invocable.invokeFunction("come_back",
                "sol-fa",
                "do", "re", "mi", "fa", "so", "ra", "ti", "do");
    }
    
    /**
     * This Ruby script will return a subset of the methods passed into it,
     * essentially multiple return values.  We can handle this in the API, 
     * since all invokeFunction() calls return java.lang.Object, which we can
     * cast.  If the function returns multiple values, we can cast this return-type
     * as a List.  @see printValues()
     * @param engine
     * @param map
     * @throws javax.script.ScriptException
     * @throws java.lang.NoSuchMethodException
     */
    private void invokeFunctionWithReturns(ScriptEngine engine, Map map)
            throws ScriptException, NoSuchMethodException {
        String script =
                "def get_by_value(hash, value)" +
                "hash.select { |k,v| v == value  }" +
                "end";
        engine.eval(script);
        Invocable invocable = (Invocable) engine;
        Object object = invocable.invokeFunction("get_by_value", map, "red");
        printValues("red", object);
        object = invocable.invokeFunction("get_by_value", map, "gray");
        printValues("gray", object);

    }

    /**
     * From the invokeFunctionWithReturns() method, we can parse a function that
     * returns multiple values by casting it as an instanceof a List.  See below.
     * @param value
     * @param object
     */
    private void printValues(String value, Object object) {
        System.out.print(value + ": ");
        if (object instanceof List) {
            for (Object element : (List) object) {
                if (element instanceof List) {
                    for (Object param : (List) element) {
                        System.out.print(param + " ");
                    }
                }
            }
        }
        System.out.println();
    }

    /**
     * Yoko Harada spoke about how cluttered passing multiple values can be in your
     * script.  To solve this, you can place these variables as global variables
     * in your namespace in Ruby, which the script will have access to in its
     * methods.  See the method and script below.
     * @param engine
     * @param map
     * @throws javax.script.ScriptException
     * @throws java.lang.NoSuchMethodException
     */
    private void invokeFunctionWithGlobalVariables(ScriptEngine engine, Map map)
            throws ScriptException, NoSuchMethodException {
        String script =
                "def get_by_value(value)" +
                "$hash.select { |k,v| v == value  }" +
                "end";
        engine.put("hash", map);
        engine.eval(script);
        Invocable invocable = (Invocable) engine;
        Object object = invocable.invokeFunction("get_by_value", "white");
        printValues("white", object);
    }

    public static void main(String[] args)
            throws ScriptException, NoSuchMethodException {
        new InvokingFunctionsExample();
    }
}
