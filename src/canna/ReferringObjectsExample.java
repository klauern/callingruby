
// See referring document/blog from Yoko Harada:
// http://yokolet.blogspot.com/2008/03/tips-for-jruby-engine-how-to-refer.html

package canna;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ReferringObjectsExample {

    private ReferringObjectsExample() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("jruby");
        createObjectsInJava(engine);
        createObjectsInRuby(engine);
    }

    private void createObjectsInJava(ScriptEngine engine) throws ScriptException {
        // giving an object to Ruby as a global variable
        List list = new ArrayList();
        list.add("What's up?");
        list.add("How're you doing?");
        list.add("How have you been?");
        engine.put("list", list);
        String script = "$list.each {|msg| puts msg }";
        engine.eval(script);
        engine.put("first", 2008);
        script = "$first.step(2015, 2) {|i| puts i }";
        engine.eval(script);
    }

    // klauer: I'm not sure if this is exactly right.  I had to change a couple things
    //   - define the List as a List<String> in both seasons and colors instances
    //   - define the Map as a Map<String, Float> for gpas.  It works now, but
    // I'm not sure if I just abstracted something away, or if this was supposed
    // to compile without the need for generics.
    private void createObjectsInRuby(ScriptEngine engine) throws ScriptException {
        // referring an object as a global variable
        String script = "$seasons = ['spring', 'summer', 'fall', 'winter']";
        engine.eval(script);
        List<String> seasons = (List) engine.get("seasons");
        for (String season : seasons) {
            System.out.println(season);
        }
        // receiving an array object returned from Ruby
        script = "colors = ['red', 'green', 'white', 'blue'];"+
                 "colors.reverse";
        List<String> colors = (List) engine.eval(script);
        for (String color : colors) {
            System.out.println(color);
        }
        // receiving a hash object returned from Ruby
        script = "gpas1 = {\"Alice\" => 3.75, \"Bob\" => 4.0};"+
                 "gpas2 = {\"Alice\" => 3.92, \"Chris\" => 3.55};"+
                 "gpas1.merge!(gpas2)";
        Map<String, Float> gpas = (Map)engine.eval(script);
        for (String name : gpas.keySet()) {
            System.out.println(name +": " + gpas.get(name));
        }
    }

    public static void main(String[] args) throws ScriptException {
        new ReferringObjectsExample();
    }
}