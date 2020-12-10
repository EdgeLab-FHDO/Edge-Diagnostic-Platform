package InfrastructureManager.Configuration;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;

public class testStuff {


    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(testStuff.class);
//            logger.info("Hello World");
////            System.out.println("hello world");
//
//            long negative = 0;
//            negative = 10 - 20000;
//            System.out.println(negative);
//

        long up = 456;
        long down = 1000;

        long upDown = up/down;
        System.out.println(upDown);

//        Multimap<String, String> myMultimap = ArrayListMultimap.create();
//        HashMap<String, Long> hashTest = new HashMap<>();
//        HashMap<String, Long> hashTest1 = new HashMap<>();
//        Multimap<String, HashMap<String, Long>> myMultimap2 = ArrayListMultimap.create();
//        // Adding some key/value
////            myMultimap.put("Fruits", "Bannana");
////            myMultimap.put("Fruits", "Apple");
////            myMultimap.put("Fruits", "Pear");
////            myMultimap.put("Vegetables", "Carrot");
////            myMultimap.put("Vegetables", "Carrot");
////            myMultimap.put("Vegetables", "Carrot");
//        hashTest.put("client 1", 10L);
//        myMultimap2.put("Fruits", hashTest);
//        hashTest1.put("client 2", 100L);
//        myMultimap2.put("Fruits", hashTest1);
//
//        // Getting values
//        Collection<HashMap<String, Long>> fruits = myMultimap2.get("Fruits");
//        System.out.println("stuff from fruit \n" + fruits); // [Bannana, Apple, Pear]
//
//
//        //get client 1
//        for (HashMap<String, Long> client : fruits) {
//            if (client.containsKey("client 1")) {
//                Long getClient1 = client.get("client 1");
//                //update client 1
//                client.put("client 1", 200L);
//            }
//
//
////
//        }
//        System.out.println("stuff from fruit \n" + fruits); // [Bannana, Apple, Pear]}
//
//        System.out.println(myMultimap2);


    }


}
