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

    /**
     * Sets up the set of examples by creating the JRuby scripting instance.
     * @throws javax.script.ScriptException
     * @throws java.lang.NoSuchMethodException
     */
    private InvokingMethodsExample()
            throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("jruby");
        //invokeSimpleMethod(engine);
        invokeMethodWithMultipleInstances(engine);
        //invokeMethodWithGlobalVariables(engine);
    }

    /**
     * This is the simplest example for calling a method inside a class or module
     * In this example, we have a module SomeModule defined that has a method,
     * say().  To call this method, we first create an Invocable type, returning
     * an instance of the module itself.  From here we can call the method inside
     * the Invocable module.
     * @param engine
     * @throws javax.script.ScriptException
     * @throws java.lang.NoSuchMethodException
     */
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
        // We must eval the script prior to having access to it.  This must be
        // done for all class- and module-level access.  It may be a performance
        // penalty, but I think that's why JRuby has a compiler now--jrubyc.
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
        // We do this again to show another example (reusing the Object).
        object = engine.eval(script);
        invocable.invokeMethod(object, "say_it_again");
    }

    /**
     * If we wanted to get more than one instance from a Ruby return method, we can take
     * advantage of Ruby returning to us a java.util.List object that we can parse
     * for the instances we want to use.  In this case, we are going to get back
     * two instances which we will execute methods on.
     * 
     * In this example, we will get back two items from Ruby's script return,
     * and with each of these we will call the Ruby script comment() and others().
     * @param engine
     * @throws javax.script.ScriptException
     * @throws java.lang.NoSuchMethodException
     */
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

    /**
     * As shown in invodeMethodWithMultipleInstances(), we can get back multiple
     * instances of a script as a java.lang.Object, casting it to a List.  This allows
     * us access to each of the returned instances.  An alternative method to doing this
     * is to assign these values to the global variable namespace.  In Ruby, this is
     * done by prepending the variables with '$'.  From this, we can call the script
     * engine to get back to us the visible global variables.
     * 
     * This example does nearly the same as 
     * {@link InvokingMethodsExample#invokeMethodWithMultipleInstances(javax.script.ScriptEngine) }, 
     * except we assign the values to a global namespace instead.  The flexibility
     * for us comes from Java invoking the engine to get at each instance.
     * @param engine
     * @throws javax.script.ScriptException
     * @throws java.lang.NoSuchMethodException
     */
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
