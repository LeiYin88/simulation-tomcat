public class Demo {
    static int v = 0;

    public static void main(String[] args) {
        method(v);
        System.out.println(v);
    }

    public static void method(int v){
        v = 10;
    }
}
