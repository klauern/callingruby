/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package canna;

import java.util.List;
//import org.jruby.Ruby;
//import org.jruby.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author klauer
 */
public class GettingEngineExample {
 GettingEngineExample() throws ScriptException {
     caseOne();
     caseTwo();
     caseThree();
     caseFour();
 }

 void caseOne() throws ScriptException {
     ScriptEngineManager manager = new ScriptEngineManager();
     ScriptEngine engine = manager.getEngineByName("jruby");
     ScriptEngineFactory factory = engine.getFactory();
     System.out.println("engine name: " + factory.getEngineName());
     System.out.println("version: " + factory.getEngineVersion());
     engine.eval("puts 'Case one: getEngineByName(\"jruby\").'");
 }

 void caseTwo() throws ScriptException {
     ScriptEngineManager manager = new ScriptEngineManager();
     ScriptEngine engine = manager.getEngineByName("ruby");
     ScriptEngineFactory factory = engine.getFactory();
     System.out.println("engine name: " + factory.getEngineName());
     System.out.println("version: " + factory.getEngineVersion());
     engine.eval("puts 'Case two: getEngineByName(\"ruby\").'");
 }

 void caseThree() throws ScriptException {
     ScriptEngineManager manager = new ScriptEngineManager();
     ScriptEngine engine = manager.getEngineByExtension("rb");
     ScriptEngineFactory factory = engine.getFactory();
     System.out.println("engine name: " + factory.getEngineName());
     System.out.println("version: " + factory.getEngineVersion());
     engine.eval("puts 'Case three: getEngineByExtension(\"rb\").'");
 }

 void caseFour() {
     ScriptEngineManager manager = new ScriptEngineManager();
     ScriptEngine engine = manager.getEngineByExtension("rb");
     ScriptEngineFactory factory = engine.getFactory();
     List<String> names = factory.getMimeTypes();
     System.out.println("mime types are ...");
     for (String name : names) {
         System.out.print(name);
     }
 }

 public static void main(String[] args) throws ScriptException {
     new GettingEngineExample();
 }
}
