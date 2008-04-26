/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package klauer.callingruby.jrubywiki;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jruby.Ruby;
import org.jruby.RubyRuntimeAdapter;
import org.jruby.javasupport.JavaEmbedUtils;

/**
 *
 * @author klauer
 */
public class CallSomeRuby {

    public CallSomeRuby() throws CloneNotSupportedException, FileNotFoundException {
        Ruby runtime = JavaEmbedUtils.initialize(new ArrayList());
        RubyRuntimeAdapter evaler = JavaEmbedUtils.newRuntimeAdapter();
        FileReader fr = new FileReader("script.rb");
        BufferedReader br = new BufferedReader(fr);
        try {
            while (br.ready()) {
                evaler.eval(runtime, br.readLine());
            }
        } catch (IOException ex) {
            Logger.getLogger(CallSomeRuby.class.getName()).log(Level.SEVERE, null, ex);
        }
        JavaEmbedUtils.terminate(runtime);

    }

    public static void main(String[] args) throws FileNotFoundException, CloneNotSupportedException {
        new CallSomeRuby();
    }
}

/*
 * Gotchas
From JRuby Wiki: http://wiki.jruby.org/wiki/Java_Integration#Gotchas
 
If you plan on calling gems from an embedded script, there are a couple of things you need to be aware of:

If you require 'rubygems', you need to make sure you set a few system properties: 
    jruby.base, 
    jruby.home, 
    jruby.lib, 
    jruby.shell, and 
    jruby.script. You can look in jruby.bat to see how these can be set from the 
 command line. If, for some reason, you can't set them on the command line, 
 you'll need to set them programmatically, or else you'll receive a NullPointerException 
 when RbConfigLibrary loads.

Also, make sure you get the load path set properly. Running jirb and calling 
 $:.inspect should give you a good idea what paths need to be included. All of 
 those paths can be set the same way you'd set a Java classpath. However, one 
 reference to *lib/ruby/1.8* has to remain relative. This is because some files 
 (*digest/sha2*, for example) are loaded from the jruby.jar. If you are running 
 unit tests from Ant, you may have problems because Ant tends to expand pathelements. 
 Fortunately, it's easy enough to append lib/ruby/1.8 to the load path before calling 
 require 'rubygems' in your scripts.

Here is an example load path:

    * C:/common/jruby-0.9.2/lib
    * C:/common/jruby-0.9.2/lib/ruby/site_ruby/1.8
    * C:/common/jruby-0.9.2/lib/ruby/site_ruby/1.8/java
    * C:/common/jruby-0.9.2/lib/ruby/site_ruby
    * C:/common/jruby-0.9.2/lib/ruby/1.8
    * C:/common/jruby-0.9.2/lib/ruby/1.8/java
    * lib/ruby/1.8 

These settings are specific to my system. Make sure the paths are correct for your system.

If you declare a bean using BSF, make sure you undeclare it when you are done using 
 it even if you declare another bean using the same name. BSF internally adds 
 declared beans to a vector, and only removes them once they are undeclared. Or, 
 as an alternative, you can call registerBean and access the object from JRuby 
 using the global $bsh reference. 
 */