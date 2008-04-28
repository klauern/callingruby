/** Taken from Yoko Harada's JRuby Tips blog:
 * http://yokolet.blogspot.com/2008/04/tips-for-jruby-engine-getinterface.html
 * 
 */
package klauer.callingruby.yokoharada;

import java.util.List;

public interface SimpleFile {
    void create(String filename);
    void write(List list);
    void close();
}
