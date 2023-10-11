package org.mospower;
import com.sun.source.tree.NewArrayTree;

import java.util.ArrayDeque;


public class Main {

    public static void main(String[] args) {
        NewDeque<Object> proverka = new NewDeque<>();
        Object itIsFalse;
        for (int i = 0; i < 20; i++){
            proverka.addFirst(i);
            System.out.println(proverka.toString());
            //System.out.println(itIsFalse);
        }
        for (int i = 0; i < 21; i++){
            itIsFalse = proverka.removeFirst();
            System.out.println(proverka.toString());
            System.out.println(itIsFalse);

        }
    }



//    public static void main(String[] args) {
//        NewDeque<Integer> proverka = new NewDeque<>();
//        for (int i = 0; i < 10; i++){
//            proverka.addLast(i);
//            System.out.println(proverka.toString());
//        }
//
//
//
//    }
}