import java.util.Map;
import java.util.Properties;

public class Test {
    public static void main(String[] args) {
        Map<String, String> map = System.getenv();
        map.forEach((k,v) -> {
            System.out.println(k + " = " + v);
        });

        String computername = System.getenv("COMPUTERNAME");
        System.out.println(computername);
    }
}
