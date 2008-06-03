/**
 * 
 */
package klauer.callingruby.rubygems;

import java.util.ArrayList;
import java.util.List;
import org.jruby.Ruby;
import org.jruby.RubyRuntimeAdapter;
import org.jruby.javasupport.JavaEmbedUtils;

/**
 * This is an attempt to try to interact with JRuby via Java.  I'm using 
 * Plain 'ol JRuby 1.1, and I'm not entirely sure which has to be done.  THis is
 * pulled directly off of the wiki on how to call Ruby from Java.
 * 
 * @author klauer
 */
public class UsingRubyGems {

   public List<String> jruby_properties;
   public static Ruby runtime;
   public static RubyRuntimeAdapter evaler;

   /* I guess I don't need this String at all...
   private static String ruby_classpath = ";C:\\Development\\Ruby\\jruby-1.1.1\\lib;" +
   "C:\\Development\\Ruby\\jruby-1.1.1\\lib\\ruby\\site_ruby\\1.8; " +
   "C:\\Development\\Ruby\\jruby-1.1.1\\lib\\ruby\\site_ruby\\1.8\\java" +
   "C:\\Development\\Ruby\\jruby-1.1.1\\lib\\ruby\\site_ruby;" +
   "C:\\Development\\Ruby\\jruby-1.1.1\\lib\\ruby\\1.8;" +
   //"C:\\Development\\Ruby\\jruby-1.1.1\\lib\\ruby\\1.8\\java;" +
   //"lib\\ruby\\1.8;" +
   //"C:\\Development\\Ruby\\jruby-1.1.1\\bin\\..\\lib\\bsf.jar" +
   "C:\\Development\\Ruby\\jruby-1.1.1\\bin\\..\\lib\\jruby.jar";
    */
   public static void main(String[] args) {

      //String classpath = System.getProperty("java.class.path");
      // See the Gotchas in CallSomeRuby.java
      //System.setProperty("jruby.base", "C:\\Development\\Ruby\\jruby-1.1.1"); // the jruby-1.1/ directory
      System.setProperty("jruby.home", "C:\\Development\\Ruby\\jruby-1.1.1\\bin\\..");   // wherever your /bin dir is for JRuby
      System.setProperty("jruby.shell", "cmd.exe");  // /bin/sh or cmd.exe depending
      System.setProperty("jruby.lib", "C:\\Development\\Ruby\\jruby-1.1.1\\bin\\..\\lib");  // jruby-1.1/lib (Must be relative, not explicit)
      System.setProperty("jruby.script", "jruby.bat");          //  org.jruby.Main %JRUBY_OPTS% %_RUBY_OPTS%"); // either jruby or jruby.bat org.jruby.Main %JRUBY_OPTS% %_RUBY_OPTS%
      //System.setProperty("java.class.path", classpath);
      //System.getProperties("java.library.path");
      runtime = JavaEmbedUtils.initialize(new ArrayList());
      evaler = JavaEmbedUtils.newRuntimeAdapter();

      evaler.eval(runtime, "puts \" Hello World \"");
      evaler.eval(runtime, "require 'rubygems'\n" +
              "require 'highline'\n" +
              "a = HighLine.new\n" +
              //"a.ask(\"Company?  \") { |q| q.default = \"none\" }" +
              "");

      // This should return 'Mac OS X' on OS X,
      // 'Windows XP' on my laptop.
      System.out.println("OS is: " + System.getProperty("os.name"));

      JavaEmbedUtils.terminate(runtime);
   }
}