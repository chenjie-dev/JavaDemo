package List;

import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] args) {

        List<String> list = new ArrayList<String>();

        for(int i = 0; i < 10; i++) {
            list.add(Integer.toString(i));
        }

        for(String s : list) {
            System.out.println(s);
        }
        System.out.println("Testing");
    }
}
