package org.mospower;


import java.util.Iterator;
import java.util.NoSuchElementException;

public class  NewIterator<T> implements Iterator {
    private MyTriplet<T> firstIter;
    private int counterIter;
    private MyTriplet<T> savingTriplet = new MyTriplet<T>(null, null);
    private int realIndex = 0;
    private boolean flag = false;

    public NewIterator(MyTriplet<T> first, int counter){
        this.firstIter = first;
        this.counterIter = counter;
        //this.savingTriplet = first;

    }

    @Override
    public boolean hasNext() {
        if( flag == false && realIndex == savingTriplet.getSizeArray() -1 && savingTriplet.getTripletNext() != null ){
            this.savingTriplet = firstIter;
            for (int i = 0; i < firstIter.getSizeArray(); i++) {
                if (firstIter.getArray()[i] != null){
                    realIndex = i;
                    break;
                }
            }
            flag = true;
            return true;
        } else if (flag == false && realIndex < savingTriplet.getSizeArray() - 1) {
            this.savingTriplet = firstIter;
            for (int i = 0; i < firstIter.getSizeArray(); i++) {
                if (firstIter.getArray()[i] != null){
                    realIndex = i;
                    break;
                }
            }
            flag = true;
            return savingTriplet.getArray()[realIndex + 1] != null;
        }
        return false;
    }

//    @Override
//    public Object next() {
//        if (counterIter == 0){
//            throw new NoSuchElementException();
//        }
//        if (flag == false){
//            this.savingTriplet = firstIter;
//            for (int i = 0; i < firstIter.getSizeArray(); i++) {
//                if (firstIter.getArray()[i] != null){
//                    realIndex = i;
//                    break;
//                }
//            }
//            flag = true;
//            return firstIter.getArray()[realIndex];
//        }else{
//            if (hasNext()){
//                if (realIndex == savingTriplet.getSizeArray()-1){
//                    savingTriplet = savingTriplet.getTripletNext();
//                    realIndex = 0;
//                    return savingTriplet.getArray()[realIndex];
//                }else{
//                    realIndex += 1;
//                    return savingTriplet.getArray()[realIndex];
//                }
//            }else{
//                return null;
//            }
//        }
//    }
//}
//





//
//    @Override
//    public boolean hasNext() {
//        if( realIndex == savingTriplet.getSizeArray() -1 && savingTriplet.getTripletNext() != null ){
//            return true;
//        } else if (realIndex < savingTriplet.getSizeArray() - 1) {
//            return savingTriplet.getArray()[realIndex + 1] != null;
//        }
//        return false;
//    }
//
//    @Override
//    public Object next() {
//        if (counterIter == 0){
//            throw new NoSuchElementException();
//        }
//        if (flag == false){
//            this.savingTriplet = firstIter;
//            for (int i = 0; i < firstIter.getSizeArray(); i++) {
//                if (firstIter.getArray()[i] != null){
//                    realIndex = i;
//                    break;
//                }
//            }
//            flag = true;
//            return firstIter.getArray()[realIndex];
//        }else{
//            if (hasNext()){
//                if (realIndex == savingTriplet.getSizeArray()-1){
//                    savingTriplet = savingTriplet.getTripletNext();
//                    realIndex = 0;
//                    return savingTriplet.getArray()[realIndex];
//                }else{
//                    realIndex += 1;
//                    return savingTriplet.getArray()[realIndex];
////                    return realIndex;
//                }
//            }else{
//                return null;
//            }
//        }
//    }
//}

    @Override
    public Object next() {
        if (counterIter == 0){
            throw new NoSuchElementException();
        }
        this.savingTriplet = firstIter;
        for (int i = 0; i < firstIter.getSizeArray(); i++) {
            if (firstIter.getArray()[i] != null){
                realIndex = i;
                break;
            }
        }

        return firstIter.getArray()[realIndex];


        //                    return realIndex;

    }
}