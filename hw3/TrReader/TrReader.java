import java.io.Reader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Translating Reader: a stream that is a translation of an
 * existing reader.
 *
 * @author Mingjie Chen
 */
public class TrReader extends Reader {
    /**
     * A new TrReader that produces the stream of characters produced
     * by STR, converting all characters that occur in FROM to the
     * corresponding characters in TO.  That is, change occurrences of
     * FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     * in STR unchanged.  FROM and TO must have the same length.
     */
    private Reader _str;
    private String _from;
    private String _to;

    public TrReader(Reader str, String from, String to) throws IOException {
        // TODO: YOUR CODE HERE
        this._str = str;
        this._from = from;
        this._to = to;
    }

    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */

//    public int read() throws IOException {
//        char[] buf = new char[1];
//        int count = read(buf, 0, 1);
//        return count > 0 ? buf[0] : -1;
//    }


    public int read(char cbuf[], int off, int count) throws IOException {
        int res = _str.read(cbuf, off, count);
        for(int i = 0;i<cbuf.length;i++){
            if(_from.indexOf(cbuf[i])!=-1){
                cbuf[i] = _to.charAt(_from.indexOf(cbuf[i]));
            }
        }
        return res;
    }

    public void close() throws IOException {
    }
}
