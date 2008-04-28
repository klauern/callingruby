/**
 * This set of classes and methods explains how the JSR-223 Scripting API can
 * allow the scripting engine to implement Java interfaces.  In this example,
 * Yoko Harada shows how you can create interfaces in Java which will be 
 * implemented by the JRuby runtime.  See the Remarkable.java, Removable.java,
 * and SimpleFile.java interfaces for the details.
 * 
 * Note: Minor changes were made to this class by means of specifying the alternative
 * package namespace that SimpleFile or Removable and Remarkable are declared in.
 * canna to klauer.callingruby.yokoharada
 * @author Yoko Harada
 * See: http://yokolet.blogspot.com/2008/04/tips-for-jruby-engine-getinterface.html
 */

package klauer.callingruby.yokoharada;

import java.util.ArrayList;
import java.util.List;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class GetInterfacesExample {
    
    /**
     * To run these examples, the constructo first creates the JRuby engine,
     * and then calls each of these examples.
     * 
     * @throws javax.script.ScriptException
     */
    private GetInterfacesExample() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("jruby");
        getInterfaceByTopLevel(engine);
        getInterfaceByClass(engine);
        getInterfaceMultipleStuffs(engine);
    }
    
    /**
     * The most basic way of implementing a Java Interface is to call the two
     * lines--require 'java' and include_class <interface you want to implement>
     * From here it is a matter of defining what those interfaces specified you do.
     * 
     * You need not worry about returning an instnace of the script to Java, since
     * the engine gets a receiver of 'self' back from the JRuby runtime itself.
     * 
     * In Java, you have to cas
     * What this example does is implement a very simple interface, SimpleFile.java,
     * that has three methods to it: create(), write(message), and close().
     * 
     * @param engine
     * @throws javax.script.ScriptException
     */
    private void getInterfaceByTopLevel(ScriptEngine engine) throws ScriptException {
        String script =
                "require \'java\'\n" +
                "include_class \'klauer.callingruby.yokoharada.SimpleFile\'\n" +
                "def create(name)" +
                  "@name = name;" +
                  "@tmpfile = File.new(name, \"w\");" +
                  "@tmpfile.chmod(0600);" +
                "end\n" +
                "def write(message)" +
                  "message.each { |m| @tmpfile.puts(m) }" +
                "end\n" +
                "def close()" +
                  "@tmpfile.close;" +
                  "puts \"The file, #{@name}, has #{File.size(@name)} bytes.\"" +
                "end";
        
        engine.eval(script);
        Invocable invocable = (Invocable) engine;
        SimpleFile simpleFile = invocable.getInterface(SimpleFile.class);
        simpleFile.create("simplefile.txt");
        List list = new ArrayList();
        list.add("A bird in the hand is worth two in the bush.");
        list.add("Birds of a feather flock together.");
        list.add("Every bird loves to hear himself sing.");
        simpleFile.write(list);
        simpleFile.close();
    }
    
    
    /**
     * If we want to implement a Java interface that exists inside of a Ruby 
     * class, we change our two initial method calls in JRuby to:
     * require 'java'
     * class <class you've defined>
     *   import <interface you want to implement>
     * and define your methods in that interface, <b>inside</b> of a class.
     * 
     * All other steps are the same as before.
     * @param engine
     * @throws javax.script.ScriptException
     */
    private void getInterfaceByClass(ScriptEngine engine) throws ScriptException {
        String script =
                "class SimpleFileImple\n" +
                  "import \'klauer.callingruby.yokoharada.SimpleFile\'\n" +
                  "def initialize(name)" +
                    "@name = name;" +
                    "@tmpfile = File.new(name, \"w\");" +
                    "@tmpfile.chmod(0600)" +
                  "end\n" +
                  "def write(message)" +
                    "message.each { |m| @tmpfile.puts(m) }" +
                  "end\n" +
                  "def close()" +
                    "@tmpfile.close;" +
                    "puts \"The file, #{@name}, has #{File.size(@name)} bytes.\"" +
                  "end\n" +
                "end\n" +
                "SimpleFileImple.new($name)";
        
        engine.put("name", "simplefile2.txt");
        Object object = engine.eval(script);
        Invocable invocable = (Invocable) engine;
        SimpleFile simpleFile = invocable.getInterface(object, SimpleFile.class);
        List list = new ArrayList();
        list.add("When it is a question of money, everybody is of the same religion.");
        list.add("Money is the wise man's religion.");
        simpleFile.write(list);
        simpleFile.close();
    }
    
    /**
     * Slightly different than the two methods 
     * {@link GetInterfacesExample#getInterfaceByTopLevel(javax.script.ScriptEngine) }
     * and {@link GetInterfacesExample#getInterfaceByClass(javax.script.ScriptEngine)  },
     * this example implements multiple Interfaces.  To do this, we import both of the
     * interface classes and implement their members.
     * 
     * In Java, we would get an instance of a java.util.List returned to us that
     * we can cast the java.lang.Object to, using it's implemented members.
     * @param engine
     * @throws javax.script.ScriptException
     */
    private void getInterfaceMultipleStuffs(ScriptEngine engine) throws ScriptException {
        String script = 
                "class Flowers\n" +
                  "import \'klauer.callingruby.yokoharada.Remarkable\'\n" +
                  "import \'klauer.callingruby.yokoharada.Removable\'\n" +
                  "@@hash = {\'red\' => \'ruby\', \'white\' => \'pearl\'}\n" +
                  "def initialize(color, names)" +
                    "@color = color;" +
                    "@names = names;" +
                  "end\n" +
                  "def remark\n" +
                    "puts \"#{@names.join(\', \')}. Beautiful like a #{@@hash[@color]}!\"" +
                  "end\n" +
                  "def remove(index)" +
                    "print \"If I remove #{@names[index]}, \";" +
                    "@names.delete_at(index);" +
                    "print \"others will be #{@names.join(\', \')}.\n\"" +
                  "end\n" +
                "end\n" +
                "red = Flowers.new(\"red\", [\"cameliia\", \"hibiscus\", \"rose\", \"canna\"])\n" +
                "white = Flowers.new(\"white\", [\"gardenia\", \"lily\", \"magnolia\"])\n" +
                "return red, white";
        
        Object objects = engine.eval(script);
        Invocable invocable = (Invocable) engine;
        if (objects instanceof List) {
            for (Object object : (List)objects) {
                Object flower = invocable.getInterface(object, Remarkable.class);
                ((Remarkable)flower).remark();
                flower = invocable.getInterface(object, Removable.class);
                ((Removable)flower).remove(1);
            }
        }
    }
    
    public static void main(String[] args)
            throws ScriptException, NoSuchMethodException {
        new GetInterfacesExample();
    }
}