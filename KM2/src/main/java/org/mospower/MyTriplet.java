package org.mospower;

import java.util.Arrays;

public class MyTriplet<T> {
    private MyTriplet<T> tripletNext;
    private MyTriplet<T> tripletPrev;
    private Object[] array;
    private int sizeArray = 5;
    public MyTriplet(MyTriplet<T> tripletNext, MyTriplet<T> tripletPrev){
        this.tripletNext = tripletNext;
        this.tripletPrev = tripletPrev;
        this.array = new Object[sizeArray];
    }

    public int getSizeArray() {
        return sizeArray;
    }

    public MyTriplet<T> getTripletNext() {
        return tripletNext;
    }

    public void setTripletNext(MyTriplet<T> tripletNext) {
        this.tripletNext = tripletNext;
    }

    public MyTriplet<T> getTripletPrev() {
        return tripletPrev;
    }

    public void setTripletPrev(MyTriplet<T> tripletPrev) {
        this.tripletPrev = tripletPrev;
    }

    public Object[] getArray() {
        return array;
    }

    public void setArray(int index, T element) { // добавление элемента в заданный индекс
        array[index] = element;
    }

//    @Override
//    public String toString() {
//        MyTriplet<T> nowTriplet = this;
//        String stroka = "MyTriplet{" +
//                ", array=" + Arrays.toString(nowTriplet.array);
//        while (nowTriplet.getTripletNext() != null){
//            nowTriplet = getTripletNext();
//            stroka += Arrays.toString(nowTriplet.array);
//        }
//        stroka += "}";
//        return stroka;
//    }
    @Override
    public String toString() {
        String returnovoe = "MyTriplet{" +
                ", array=" + Arrays.toString(array) +
                '}';
        if (tripletNext != null){
            returnovoe += ", array next = " + Arrays.toString(tripletNext.array);
        }
        return returnovoe;
    }
}
