/** P2Pattern class
 *  @author Josh Hug & Vivant Sakore
 */

public class P2Pattern {
    /* Pattern to match a valid date of the form MM/DD/YYYY. Eg: 9/22/2019 */
    public static final String P1 = "((0?[1-9])|(1[0-2]))\\/((0?[1-9])|([12]\\d)|(3[01]))\\/((19)|(20)\\d{2})|([1-9]\\d{3}\\d*)";

    /** Pattern to match 61b notation for literal IntLists. */
    public static final String P2 = "\\(([0-9]\\d*\\,\\s*)*[0-9]\\d*\\)";

    /* Pattern to match a valid domain name. Eg: www.support.facebook-login.com */
    public static final String P3 = "";

    /* Pattern to match a valid java variable name. Eg: _child13$ */
    public static final String P4 = "([a-z]|\\_|\\$)(\\d|[a-z]|\\_|\\$)*";

    /* Pattern to match a valid IPv4 address. Eg: 127.0.0.1 */
    public static final String P5 = "(\\d?\\d?[0-9]\\.){3}\\d?\\d?[0-9]";

}
