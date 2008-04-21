/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package usingGems;

import java.util.ArrayList;
import java.util.List;
import org.jruby.Ruby;
import org.jruby.RubyRuntimeAdapter;
import org.jruby.javasupport.JavaEmbedUtils;

/**
 * This is an attempt to try to interact with JRuby via Java.  I'm using 
 * Plain 'ol JRuby 1.1, and I'm not entirely sure which has to be done.  THis is
 * pulled directly off of the wiki on how to call Ruby from Java.
 * @author klauer
 */
public class UsingRubyGems {

    public List<String> jruby_properties;
    
    public static Ruby runtime;
    public static RubyRuntimeAdapter evaler;

    public static void main(String[] args) {
        runtime = JavaEmbedUtils.initialize(new ArrayList());
        evaler = JavaEmbedUtils.newRuntimeAdapter();
        
        evaler.eval(runtime, "puts ");
        
        
        // See the Gotchas in CallSomeRuby.java
        System.setProperty("JRUBY.BASE", ); // the jruby-1.1/ directory
        System.setProperty("JRUBY.HOME", );   // wherever your /bin dir is for JRuby
        System.setProperty("JRUBY.SHELL", );  // /bin/sh or cmd.exe depending
        System.setProperty("JRUBY.LIB", );  // jruby-1.1/lib
        System.setProperty("JRUBY.SCRIPT", ); // either jruby or jruby.bat org.jruby.Main %JRUBY_OPTS% %_RUBY_OPTS%
        
        JavaEmbedUtils.terminate(runtime);
    }
}
