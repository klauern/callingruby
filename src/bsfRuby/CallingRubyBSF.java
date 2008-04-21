/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bsfRuby;

import javax.swing.JFrame;
import javax.swing.JLabel;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

/**
 *
 * @author klauer
 */
public class CallingRubyBSF 
{
    JLabel mylabel = new JLabel();

    public CallingRubyBSF() throws BSFException {
        BSFManager.registerScriptingEngine("ruby", 
                                           "org.jruby.javasupport.bsf.JRubyEngine", 
                                           new String[] { "rb" });

        BSFManager manager = new BSFManager();

        /* Import an object using declareBean then you can access it in JRuby with $<name> */

        manager.declareBean("label", mylabel, JFrame.class);
        manager.exec("ruby", "(java)", 1, 1, "$label.setText(\"This is a test.\")");
    }
    
    public static void main(String[] args) throws BSFException
    {
        new CallingRubyBSF();
    }
}
