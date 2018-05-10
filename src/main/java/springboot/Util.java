
package springboot;

public class Util {
    public static void log(Object ...message) {
        for (Object m: message) {
            System.out.println(m);
        }
    }
}
