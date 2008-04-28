/*
 * This class shows how you can use the JSR-223 Scripting API in Java 6 to
 * call Ruby methods using the invokeMethod() call.  This is similar to the
 * invokeFunction() Example seen, however, invokeMethod() allows you to call
 * methods and functions that are defined in a class or module.
 * 
 * See the blog entry for this on Yoko Harada's blog:
 * http://yokolet.blogspot.com/2008/03/tips-for-jruby-engine-invokemethod.html
 */
package klauer.callingruby.yokoharada;

import java.util.List;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Yoko Harada
 */
public class InvokingMethodsExample {

    private InvokingMethodsExample()
            throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("jruby");
        invokeSimpleMethod(engine);
        invokeMethodWithMultipleInstances(engine);
        invokeMethodWithGlobalVariables(engine);
    }

    private void invokeSimpleMethod(ScriptEngine engine)
            throws ScriptException, NoSuchMethodException {
        String script =
                "module SomeModule\n" +
                "def say()" +
                "puts \"Hi, there!\"" +
                "end\n" +
                "end\n" +
                "class SomeClass\n" +
                "include SomeModule;" +
                "end\n" +
                "SomeClass.new";
        Object object = engine.eval(script);
        Invocable invocable = (Invocable) engine;
        invocable.invokeMethod(object, "say");
        script =
                "class AnotherClass\n" +
                "def say_it_again()" +
                "puts \"OK. I said, \'Hi, there.\'\"" +
                "end\n" +
                "end\n" +
                "AnotherClass.new";
        object = engine.eval(script);
        invocable.invokeMethod(object, "say_it_again");
    }

    private void invokeMethodWithMultipleInstances(ScriptEngine engine)
            throws ScriptException, NoSuchMethodException {
        String script =
                "class Flowers\n" +
                "@@hash = {\'red\' => \'ruby\', \'white\' => \'pearl\'}\n" +
                "def initialize(color, names)" +
                "@color = color;" +
                "@names = names;" +
                "end\n" +
                "def comment\n" +
                "puts \"#{@names.join(\', \')}. Beautiful like a #{@@hash[@color]}!\"" +
                "end\n" +
                "def others(index)" +
                "print \"If I omit #{@names[index]}, \";" +
                "@names.delete_at(index);" +
                "print \"others are #{@names.join(\', \')}.\n\"" +
                "end\n" +
                "end\n" +
                "red = Flowers.new(\"red\", [\"cameliia\", \"hibiscus\", \"rose\", \"canna\"])\n" +
                "white = Flowers.new(\"white\", [\"gardenia\", \"lily\", \"daisy\"])\n" +
                "return red, white";
        Object objects = engine.eval(script);
        Invocable invocable = (Invocable) engine;
        if (objects instanceof List) {
            for (Object object : (List) objects) {
                invocable.invokeMethod(object, "comment");
                invocable.invokeMethod(object, "others", 1);
            }
        }
    }

    private void invokeMethodWithGlobalVariables(ScriptEngine engine)
            throws ScriptException, NoSuchMethodException {
        String script =
                "class Flowers\n" +
                "@@hash = {\'red\' => \'ruby\', \'white\' => \'pearl\'}\n" +
                "def initialize(color, names)" +
                "@color = color;" +
                "@names = names;" +
                "end\n" +
                "def comment\n" +
                "puts \"#{@names.join(\', \')}. Beautiful like a #{@@hash[@color]}!\"" +
                "end\n" +
                "def others(index)" +
                "print \"If I omit #{@names[index]}, \";" +
                "@names.delete_at(index);" +
                "print \"others are #{@names.join(\', \')}.\n\"" +
                "end\n" +
                "end\n" +
                "$red = Flowers.new(\"red\", [\"cameliia\", \"hibiscus\", \"rose\", \"canna\"])\n" +
                "$white = Flowers.new(\"white\", [\"gardenia\", \"lily\", \"daisy\"])";
        engine.eval(script);
        Object red = engine.get("red");
        Object white = engine.get("white");
        Invocable invocable = (Invocable) engine;
        invocable.invokeMethod(red, "comment");
        invocable.invokeMethod(white, "comment");
        invocable.invokeMethod(red, "others", 1);
        invocable.invokeMethod(white, "others", 2);
    }

    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
        new InvokingMethodsExample();
    }
}
