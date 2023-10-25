package org.mospower;

import com.sun.source.tree.CompilationUnitTree;
import lombok.Data;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.CountedCompleter;

@Data
public class NewDeque<T> implements Deque<T>, Containerable {
    private MyTriplet<T> first;
    private MyTriplet<T> last;
    private int maxSize;
    private int counter = 0;


    public NewDeque(){
        MyTriplet<T> mainTriplet = new MyTriplet<T>(null,null);
        this.first = mainTriplet;
        this.last = mainTriplet;
        this.maxSize = 1000; // Максимальное суммарное число элементов в триплетах

    }
    public NewDeque(int newMaxSize){
        MyTriplet<T> mainTriplet = new MyTriplet<T>(null,null);
        this.first = mainTriplet;
        this.last = mainTriplet;
        this.maxSize = newMaxSize;

    }
//    @Override
//    public String toString() {
//        return "NewDeque{" +
//                "\n first=" + first.toString() +
//                ", counter=" + counter +
//                '}';
//    }
    @Override
    public String toString() {
        return "NewDeque{" +
                "\n first=" + first.toString() +
                "," + "\n last=" + last.toString() +
                ", counter=" + counter +
                '}';
    }

    private void setMaxSize(int rewriteSize){
        this.maxSize = rewriteSize;
    }



    //*
    // Вставляет указанный элемент в начало этого списка, если это возможно сделать немедленно, не нарушая ограничений по емкости,
    // вызывая исключение IllegalStateException, если в данный момент нет свободного места.
    // При использовании deque с ограниченной пропускной способностью, как правило, предпочтительнее использовать метод offerFirst.
    //Параметры:
    //e – элемент, который нужно добавить
    //Бросает:
    //Исключение IllegalStateException – если элемент не может быть добавлен в данный момент из-за ограничений емкости
    //ClassCastException – если класс указанного элемента препятствует его добавлению в этот список
    //Исключение NullPointerException – если указанный элемент равен null и этот deque не разрешает элементы null
    //Исключение IllegalArgumentException – если какое-либо свойство указанного элемента препятствует его добавлению в этот список
    // *\\
    @Override
    public void addFirst(T t) {
        if (maxSize > counter){
            if (t == null){
                throw new NullPointerException("Указанный элемент равен null ");
            }
            if (counter == 0){
                first.setArray(4,t);
                counter +=1;

            }else{
                Object[] array = first.getArray();
                int indexElement = 0;
                for (int i = 0; i < first.getSizeArray(); i++){
                    if (array[i] != null){
                        indexElement = i;
                        break;
                    }
                }
                if (array[indexElement].getClass().equals(t.getClass())){
                    if (indexElement != 0){
                        first.setArray(indexElement - 1, t);
                        counter += 1;
                    }else{
                        MyTriplet<T> newLeftTriplet = new MyTriplet<>(first, null);
                        newLeftTriplet.setArray(newLeftTriplet.getSizeArray() - 1 , t);
                        counter += 1;
                        first.setTripletPrev(newLeftTriplet);
                        this.first = newLeftTriplet;
                    }

                }else{
                    throw new ClassCastException("Класс указанного элемента препятствует его добавлению в этот список");
                }
            }
        }else{
            throw new IllegalStateException("Элемент не может быть добавлен в данный момент из-за ограничений емкости");
        }
    }



//* Вставляет указанный элемент в конец этого списка,
// если это возможно сделать немедленно, не нарушая ограничений по емкости,
// вызывая исключение IllegalStateException, если в данный момент нет свободного места.
// При использовании хранилища с ограниченной пропускной способностью, как правило,
// предпочтительнее использовать метод offerLast.
//Этот метод эквивалентен добавлению.
//Параметры:
//e – элемент, который нужно добавить
//Бросает:
//Исключение IllegalStateException – если элемент не может быть добавлен в данный момент из-за ограничений емкости
//ClassCastException – если класс указанного элемента препятствует его добавлению в этот список
//Исключение NullPointerException – если указанный элемент равен null и этот deque не разрешает элементы null
//Исключение IllegalArgumentException – если какое-либо свойство
// указанного элемента препятствует его добавлению в этот список*\\
    @Override
    public void addLast(T t) {
        if (maxSize > counter) {
            if (t == null) {
                throw new NullPointerException("указанный элемент равен null");
            }
            if (counter == 0) {
                first.setArray(0, t);
                counter += 1;
            } else {
                int indexNotNull = 0;
                for (int i = last.getSizeArray() - 1; i >= 0; i--) {
                    if (last.getArray()[i] != null) {
                        indexNotNull = i;
                        break;
                    }
                }
                if (last.getArray()[indexNotNull].getClass().equals(t.getClass())) {
                    if (indexNotNull == last.getSizeArray() - 1) {
                        MyTriplet<T> newMyTriplet = new MyTriplet<T>(null, last);
                        last.setTripletNext(newMyTriplet);
                        this.last = newMyTriplet;
                        last.setArray(0, t);
                        counter += 1;
                    } else {
                        last.setArray(indexNotNull + 1, t);
                        counter += 1;
                    }
                } else {
                    throw new ClassCastException("класс указанного элемента препятствует его добавлению в этот список");
                }
            }
        } else {
            throw new IllegalStateException("элемент не может быть добавлен в данный момент из-за ограничений емкости");
        }
    }


//*
//Вставляет указанный элемент в начало этого списка, если только это не нарушит ограничения по емкости.
//При использовании deque с ограниченной емкостью этот метод обычно предпочтительнее метода addFirst,
//который может не вставить элемент, только вызвав исключение.
//Параметры:
//e – элемент, который нужно добавить
//Возвращается:
//true, если элемент был добавлен в этот список, иначе false
//Бросает:
//ClassCastException – если класс указанного элемента препятствует его добавлению в этот список
//Исключение NullPointerException – если указанный элемент равен null и этот deque не разрешает элементы null
//Исключение IllegalArgumentException – если какое-либо свойство указанного элемента препятствует его добавлению в этот список
// *\\
    @Override
    public boolean offerFirst(T t){
        if (maxSize > counter) {
            if (t == null) {
                throw new NullPointerException("Указанный элемент равен null ");
            }
            if (counter == 0) {
                first.setArray(0, t);
                counter += 1;
                return true;

            } else {
                Object[] array = first.getArray();
                int indexElement = 0;
                for (int i = 0; i < first.getSizeArray(); i++) {
                    if (array[i] != null) {
                        indexElement = i;
                        break;
                    }
                }
                if (array[indexElement].getClass().equals(t.getClass())) {
                    if (indexElement != 0) {
                        first.setArray(indexElement - 1, t);
                        counter += 1;
                        return true;

                    } else {
                        MyTriplet<T> newLeftTriplet = new MyTriplet<>(first, null);
                        newLeftTriplet.setArray(newLeftTriplet.getSizeArray() - 1, t);
                        counter += 1;
                        first.setTripletPrev(newLeftTriplet);
                        this.first = newLeftTriplet;
                        return true;
                    }


                } else {
                    throw new ClassCastException("Класс указанного элемента препятствует его добавлению в этот список");
                }
            }
        } else {
            throw new IllegalStateException("Элемент не может быть добавлен в данный момент из-за ограничений емкости");
        }
    }



    //*Вставляет указанный элемент в конец этого списка, если только это не нарушит ограничения по емкости.
    //При использовании deque с ограниченной емкостью этот метод, как правило,
    //предпочтительнее метода addLast, который может не вставить элемент, только вызвав исключение.
    //Параметры:
    //e – элемент, который нужно добавить
    //Возвращается:
    //true, если элемент был добавлен в этот список, иначе false
    //Бросает:
    //ClassCastException – если класс указанного элемента препятствует его добавлению в этот список
    //Исключение NullPointerException – если указанный элемент равен null и этот deque не разрешает элементы null
    //Исключение IllegalArgumentException – если какое-либо свойство указанного элемента препятствует его добавлению в этот список *\\
    @Override
    public boolean offerLast(T t) {
        if (maxSize > counter){
            if (t == null){
                throw new NullPointerException("указанный элемент равен null");
            }
            if (counter == 0){
                first.setArray(0, t);
                counter += 1;
                return true;
            }else{
                int indexNotNull = 0;
                for (int i =  last.getSizeArray() - 1; i >= 0; i--){
                    if (last.getArray()[i] != null ){
                        indexNotNull = i;
                        break;
                    }
                }
                if (last.getArray()[indexNotNull].getClass().equals(t.getClass())){
                    if(indexNotNull == last.getSizeArray() - 1){
                        MyTriplet<T> newMyTriplet = new MyTriplet<T>(null, last);
                        last.setTripletNext(newMyTriplet);
                        this.last = newMyTriplet;
                        last.setArray(0, t);
                        counter += 1;
                        return true;
                    }else{
                        last.setArray(indexNotNull + 1, t);
                        counter += 1;
                        return true;
                    }
                }else{
                    throw new ClassCastException("класс указанного элемента препятствует его добавлению в этот список");

                }
            }
        }else{
            throw new IllegalStateException("элемент не может быть добавлен в данный момент из-за ограничений емкости");

        }
        //return false;
    }



//*
//Извлекает и удаляет первый элемент этого списка. Этот метод отличается от pollFirst только тем,
// что он генерирует исключение, если этот список пуст.
//Возвращается: глава этого отдела
//Бросает:
//Исключение NoSuchElementException – если этот список пуст*\\
    @Override
    public T removeFirst() {
        if (counter == 0 ){
            throw new NoSuchElementException("Этот список пуст");
        }
        Object[] array = first.getArray();
        Object output = null;
        for (int i = 0; i < first.getSizeArray(); i++) {
            if (array[i] != null) { //  определяем null или не null
                output = array[i];
                first.setArray(i, null);
                counter -= 1;
                if (i == first.getSizeArray() - 1 && counter != 0){
                    this.first = first.getTripletNext();
                    first.setTripletPrev(null);
                }
                break;
            }
        }
        return (T) output;
    }



    //* Извлекает и удаляет последний элемент этого списка.
    // Этот метод отличается от pollLast только тем,
    // что он генерирует исключение,
    // если этот список пуст.
    //Возвращается:
    //хвост этой деки
    //Бросает:
    //Исключение NoSuchElementException – если этот список пуст
    // *\\
    @Override
    public T removeLast() {
        if (counter == 0){
            throw new NoSuchElementException("Этот список пуст");
        }
        Object[] array = last.getArray();
        Object output = null;
        int indexNotNull = 0;
        for (int i =  last.getSizeArray() - 1; i >= 0; i--){
            if (last.getArray()[i] != null ){
                indexNotNull = i;
                output = last.getArray()[indexNotNull];
                last.setArray(i, null);
                counter -= 1;
                break;
            }
        }
        if (indexNotNull == 0 && counter != 0 ){
            this.last = last.getTripletPrev();
            last.setTripletNext(null);
        }
        return (T) output;
    }
//        Object[] array = first.getArray();
//        Object output = null;
//        for (int i = 0; i < first.getSizeArray(); i++) {
//            if (array[i] != null) { //  определяем null или не null
//                output = array[i];
//                first.setArray(i, null);
//                counter -= 1;
//                if (i == first.getSizeArray() - 1 && counter != 0){
//                    this.first = first.getTripletNext();
//                    first.setTripletPrev(null);
//                }
//                break;
//            }
//        }
        //////////////////////////////////////////////////
//        Object[] array = first.getArray();
//        int indexElement = 0;
//        int indexNotNull = 0;
//        for (int i =  last.getSizeArray() - 1; i >= 0; i--){
//            if (last.getArray()[i] != null ){
//                indexNotNull = i;
//                break;
//            }
//        }
//        if (indexNotNull == last.getSizeArray()-1){
//            last.setArray(indexNotNull+1, null);
//        }
//        if (indexNotNull == 0){
//            throw new NoSuchElementException("этот список пуст");
//        }
//        return null;



    //*   Извлекает и удаляет первый элемент этого списка или возвращает null,
    // если этот список пуст.
    //Возвращается:
    //заголовок этого списка, или null, если этот список пуст*\\
    public T pollFirst() {
        Object[] array = first.getArray();
        int indexElement = 0;
        int noSuchElementInside = 0;
        for (int i = 0; i < first.getSizeArray(); i++){
            if (array[i] != null){
                indexElement = 1;
            }else{
                noSuchElementInside = 1;
            }
            if (indexElement !=0){
                first.setArray(indexElement, null);
                break;
            }
        }
        //if (indexElement == 0 && noSuchElementInside == 1 ){
          //  return null;
        //}
        return null;
    }

    //* Извлекает и удаляет последний элемент этого списка или возвращает null,
    // если этот список пуст.
    //Возвращается:
    //конец этого списка, или null, если этот список пуст  *\\
    @Override
    public T pollLast() {
//        if (counter == 0 ){
//            throw new NoSuchElementException("Этот список пуст");
//        }
//        Object[] array = first.getArray();
//        Object output = null;
//        for (int i = 0; i < first.getSizeArray(); i++) {
//            if (array[i] != null) { //  определяем null или не null
//                output = array[i];
//                first.setArray(i, null);
//                counter -= 1;
//                if (i == first.getSizeArray() - 1 && counter != 0){
//                    this.first = first.getTripletNext();
//                    first.setTripletPrev(null);
//                }
//                break;
//            }
//        }
//        return null;

       Object[] array = last.getArray();
       int indexElement = 0;
       int indexNotNull = 0;
       for (int i =  last.getSizeArray() - 1; i >= 0; i--){
           if (last.getArray()[i] != null ){
               indexNotNull = i;
               break;
           }
       }
       if (indexNotNull == last.getSizeArray()-1){
           last.setArray(indexNotNull+1, null);
       }
       if (indexNotNull == 0){
           return null;
       }
       return null;
    }

    //* Извлекает, но не удаляет первый элемент этого списка. Этот метод отличается от peekFirst только тем, что он генерирует исключение, если этот список пуст.
    //Возвращается:
    //глава этого отдела
    //Бросает:
    //Исключение NoSuchElementException – если этот список пуст  *\\
    @Override
    public T getFirst() {
        if (counter == 0 ){
            throw new NoSuchElementException("Этот список пуст");
        }
        Object[] array = first.getArray();
        Object output = null;
        for (int i = 0; i < first.getSizeArray(); i++) {
            if (array[i] != null) { //  определяем null или не null
                output = array[i];
                break;
            }
        }
        return (T) output;
    }



    //*  Извлекает, но не удаляет последний элемент этого списка. Этот метод отличается от peekLast только тем,
    //  что он генерирует исключение, если этот список пуст.
    //    //Возвращается:
    //    //хвост этой деки
    //    //Бросает:
    //    //Исключение NoSuchElementException – если этот список пуст *\\
    @Override
    public T getLast() {
        if (counter == 0 ){
            throw new NoSuchElementException("Этот список пуст");
        }
        Object[] array = last.getArray();
        Object output = null;
        for (int i =  last.getSizeArray() - 1; i >= 0; i--){
            if (last.getArray()[i] != null ){
                output = array[i];
                break;
            }
        }

        return (T) output;
    }



    //*  Извлекает, но не удаляет первый элемент этого списка или возвращает null, если этот список пуст.
    //Возвращается:
    //заголовок этого списка, или null, если этот список пуст *\\
    @Override
    public T peekFirst() {
        if (counter == 0 ){
            return null;
        }
        Object[] array = first.getArray();
        Object output = null;
        for (int i = 0; i < first.getSizeArray(); i++) {
            if (array[i] != null) { //  определяем null или не null
                output = array[i];
                break;
            }
        }
        return (T) output;
    }




    //* Извлекает, но не удаляет последний элемент этого списка или возвращает null, если этот список пуст.
    //Возвращается:
    //конец этого списка, или null, если этот список пуст  *\\
    @Override
    public T peekLast() {
        if (counter == 0){
            return null;
        }
        Object[] array = first.getArray();
        Object output = null;
        int indexNotNull = 0;
        for (int i =  last.getSizeArray() - 1; i >= 0; i--){
            if (last.getArray()[i] != null ){
                output = array[i];
                break;
            }
        }
        return (T) output;
    }




    //* Удаляет первое вхождение указанного элемента из этого списка.
    // Если deque не содержит элемента, он остается неизменным.
    // Более формально, удаляет первый элемент e таким образом, что Objects.равен(o, e) (если такой элемент существует).
    // Возвращает значение true, если этот deque содержал
    // указанный элемент (или, что эквивалентно, если этот deque
    // изменился в результате вызова).
    //Параметры:
    //o – элемент, который должен быть удален из этого списка, если он присутствует
    //Возвращается:
    //true, если элемент был удален в результате этого вызова
    //Бросает:
    //ClassCastException – если класс указанного элемента несовместим
    // с этим deque (необязательно)
    //Исключение NullPointerException – если указанный элемент равен null
    // и этот deque не разрешает элементы null (необязательно)  *\\
    @Override
    public boolean removeFirstOccurrence(java.lang.Object o) {
        if (o == null){
            throw new NullPointerException("Указанный элемент равен null");
        }
        if (counter == 0){
            return false;
        } else {
            int indexNotNullFirst = -1; // Flag first elem not null for first (сначала как false)
            boolean vozvrat = false; // возврат
            MyTriplet<T> nowMyTriplet = first;
            int i = 0;
            while (i < first.getSizeArray()){
                if (nowMyTriplet.getArray()[i] != null && indexNotNullFirst == -1) {
                  indexNotNullFirst = i;
                  if (!nowMyTriplet.getArray()[indexNotNullFirst].getClass().equals(o.getClass())){ //если класс индекса not-null элемента не совпадает с классом элемента 'o'
                      throw new ClassCastException("Класс указанного элемента препятствует его добавлению в этот список"); // кидаем эксепшн на ClassCastException
                  }
                }
                if (nowMyTriplet.getArray()[i] != null && nowMyTriplet.getArray()[i].equals(o) ){
                    vozvrat = true;
                    if (i == indexNotNullFirst && nowMyTriplet == first){
                        nowMyTriplet.setArray(indexNotNullFirst, null);
                        counter -= 1;
                        if (nowMyTriplet != last && indexNotNullFirst == nowMyTriplet.getSizeArray() - 1){
                            first = nowMyTriplet.getTripletNext();
                            first.setTripletPrev(null);
                        }
                        return vozvrat;
                    }else{
                        while (nowMyTriplet != last ){
                            if (i == nowMyTriplet.getSizeArray()-1 && nowMyTriplet != last){
                                nowMyTriplet.setArray(i, (T) nowMyTriplet.getTripletNext().getArray()[0]);
                                nowMyTriplet = nowMyTriplet.getTripletNext();
                                i = 0;
                            } else{
                                nowMyTriplet.setArray(i, (T) nowMyTriplet.getArray()[i + 1]);
                                i++;
                            }
                        }
                        while (nowMyTriplet.getArray()[i] != null && i != 4 ){
                            nowMyTriplet.setArray(i, (T) nowMyTriplet.getArray()[i + 1]);
                            i++;
                        }
                        if (i == 4){
                            nowMyTriplet.setArray(i, null);
                        } else {
                            nowMyTriplet.setArray(i - 1, null);
                        }
                        if (i - 1 == 0 && nowMyTriplet == last && nowMyTriplet.getTripletPrev() != null){
                            last = nowMyTriplet.getTripletPrev();
                            last.setTripletNext(null);
                        }
                        counter -= 1;
                        return vozvrat;
                    }
                }
                i++;
                if (i == nowMyTriplet.getSizeArray() && nowMyTriplet != last){
                    nowMyTriplet = nowMyTriplet.getTripletNext();
                    i = 0;
                }else if (i == nowMyTriplet.getSizeArray()){ // при оставшемся nowMyTriplet = last
                    return vozvrat;
                }
            }
        }
        return false;
    }


    //*  Удаляет последнее вхождение указанного элемента из этого списка.
    // Если deque не содержит элемента, он остается неизменным.
    // Более формально, удаляет последний элемент e таким образом,
    // что Objects.equals(o, e) (если такой элемент существует).
    // Возвращает значение true, если этот deque содержал
    // указанный элемент (или, что эквивалентно, если этот deque изменился
    // в результате вызова).
    //Параметры:
    //o – элемент, который должен быть удален из этого списка, если он присутствует
    //Возвращается:
    //true, если элемент был удален в результате этого вызова
    //Бросает:
    //ClassCastException – если класс указанного элемента несовместим
    // с этим deque (необязательно)
    //Исключение NullPointerException – если указанный элемент равен null
    // и этот deque не разрешает элементы null (необязательно) *\\
    @Override
    public boolean removeLastOccurrence(java.lang.Object o) {   // не дописан
        if (o == null){
            throw new NullPointerException("Указанный элемент равен null");
        }
        if (counter == 0) {
            return false;
        } else {
            int indexNotNull = -1; // first not null for last
            boolean vozvrat = false;
            MyTriplet<T> nowMyTriplet = last; // присваиваем значения триплета, с которым сейчас работаем
            int i = nowMyTriplet.getSizeArray() - 1;
            while (i >= 0){
                if (nowMyTriplet.getArray()[i] != null && indexNotNull == -1) {
                    indexNotNull = i;
                    if (!nowMyTriplet.getArray()[indexNotNull].getClass().equals(o.getClass())) {
                        throw new ClassCastException("Класс указанного элемента препятствует его добавлению в этот список");
                    }
                }
                if (nowMyTriplet.getArray()[i] != null && nowMyTriplet.getArray()[i].equals(o) ){
                    vozvrat = true;
                    if (i == indexNotNull && nowMyTriplet == last){
                        nowMyTriplet.setArray(indexNotNull, null);
                        counter -= 1;
                        if (nowMyTriplet != first && indexNotNull == 0){
                            last = nowMyTriplet.getTripletPrev();
                            last.setTripletNext(null);

                        }
                        return vozvrat;
                    }else{
                        while (nowMyTriplet != last ){
                            if (i == nowMyTriplet.getSizeArray()-1 && nowMyTriplet != last){
                                nowMyTriplet.setArray(i, (T) nowMyTriplet.getTripletNext().getArray()[0]);
                                nowMyTriplet = nowMyTriplet.getTripletNext();
                                i = 0;
                            } else{
                                nowMyTriplet.setArray(i, (T) nowMyTriplet.getArray()[i + 1]);
                                i++;
                            }
                        }
                        while (i != indexNotNull ){
                            nowMyTriplet.setArray(i, (T) nowMyTriplet.getArray()[i + 1]);
                            i++;
                        }
                        counter -= 1;
                        nowMyTriplet.setArray(indexNotNull, null);
                        if (indexNotNull == 0 && nowMyTriplet == last && nowMyTriplet.getTripletPrev() != null){
                            last = nowMyTriplet.getTripletPrev();
                            last.setTripletNext(null);
                        }
                        return vozvrat;
                    }
                }
                i--;
                if (i == -1 && nowMyTriplet != first){
                    nowMyTriplet = nowMyTriplet.getTripletPrev();
                    i = nowMyTriplet.getSizeArray() - 1; //   i = 4
                }else if (i == -1 ){
                    return vozvrat;
                }
            }
        }
        return false;
    }



    //* Вставляет указанный элемент в очередь,
    // представленную этим deque (другими словами, в хвост этого deque),
    // если это возможно сделать немедленно, не нарушая ограничений емкости,
    // возвращая true в случае успеха и вызывая исключение IllegalStateException,
    // если в данный момент нет свободного места.
    // При использовании deque с ограниченной вместимостью,
    // как правило, предпочтительнее использовать offer.
    //Этот метод эквивалентен addLast.
    //Параметры:
    //e – элемент, который нужно добавить
    //Возвращается:
    //true (как указано в Collection.add)
    //Бросает:
    //Исключение IllegalStateException – если элемент не может быть добавлен
    // в данный момент из-за ограничений емкости
    //ClassCastException – если класс указанного элемента препятствует
    // его добавлению в этот список
    //Исключение NullPointerException – если указанный элемент равен
    // null и этот deque не разрешает элементы null
    //Исключение IllegalArgumentException – если какое-либо
    // свойство указанного элемента препятствует его добавлению в
    // этот список  *\\
    @Override
    public boolean add(T t) {
        if (maxSize > counter) {
            if (t == null) {
                throw new NullPointerException("указанный элемент равен null");
            }
            if (counter == 0) {
                first.setArray(0, t);
                counter += 1;
            } else {
                int indexNotNull = 0;
                for (int i = last.getSizeArray() - 1; i >= 0; i--) {
                    if (last.getArray()[i] != null) {
                        indexNotNull = i;
                        break;
                    }
                }
                if (last.getArray()[indexNotNull].getClass().equals(t.getClass())) {
                    if (indexNotNull == last.getSizeArray() - 1) {
                        MyTriplet<T> newMyTriplet = new MyTriplet<T>(null, last);
                        last.setTripletNext(newMyTriplet);
                        this.last = newMyTriplet;
                        last.setArray(0, t);
                        counter += 1;
                    } else {
                        last.setArray(indexNotNull + 1, t);
                        counter += 1;
                    }
                } else {
                    throw new ClassCastException("класс указанного элемента препятствует его добавлению в этот список");
                }
            }
        return true;
        } else {
            throw new IllegalStateException("элемент не может быть добавлен в данный момент из-за ограничений емкости");
        }
    }



    //*  Вставляет указанный элемент в очередь, представленную этим списком
    // (другими словами, в хвост этого списка), если это возможно сделать немедленно,
    // не нарушая ограничений по емкости, возвращая true при успешном выполнении и false,
    // если в данный момент нет свободного места. При использовании deque
    // с ограниченной емкостью этот метод, как правило,
    // предпочтительнее метода add, который может не вставить элемент, только вызвав исключение.
    //Этот метод эквивалентен offerLast.
    //Параметры:
    //e – элемент, который нужно добавить
    //Возвращается:
    //true, если элемент был добавлен в этот список, иначе false
    //Бросает:
    //ClassCastException – если класс указанного элемента препятствует
    // его добавлению в этот список
    //Исключение NullPointerException – если указанный элемент равен null
    // и этот deque не разрешает элементы null
    //Исключение IllegalArgumentException – если какое-либо свойство
    // указанного элемента препятствует его добавлению в этот список *\\
    @Override
    public boolean offer(T t) {
        return offerLast(t);
    }
//        if (maxSize >= counter){
//            if (t == null){
//                throw new NullPointerException("указанный элемент равен null");
//            }
//            if (counter == 0){
//                first.setArray(0, t);
//                counter += 1;
//                return true;
//            }else{
//                int indexNotNull = 0;
//                for (int i =  last.getSizeArray() - 1; i >= 0; i--){
//                    if (last.getArray()[i] != null ){
//                        indexNotNull = i;
//                        break;
//                    }
//                }
//                if (last.getArray()[indexNotNull].getClass().equals(t.getClass())){
//                    if(indexNotNull == last.getSizeArray() - 1){
//                        MyTriplet<T> newMyTriplet = new MyTriplet<T>(null, last);
//                        last.setTripletNext(newMyTriplet);
//                        this.last = newMyTriplet;
//                        last.setArray(0, t);
//                        counter += 1;
//                        return true;
//                    }else{
//                        last.setArray(indexNotNull + 1, t);
//                        counter += 1;
//                        return true;
//                    }
//                }else{
//                    throw new ClassCastException("класс указанного элемента препятствует его добавлению в этот список");
//
//                }
//            }
//        }else{
//            throw new IllegalStateException("элемент не может быть добавлен в данный момент из-за ограничений емкости");
//
//        }
//    }



    //* Извлекает и удаляет начало очереди, представленное этим списком
    // (другими словами, первый элемент этого списка).
    // Этот метод отличается от poll() только тем,
    // что он генерирует исключение, если этот список пуст.
    //Этот метод эквивалентен removeFirst().
    //Возвращается:
    //глава очереди, представленный этим деком
    //Бросает:
    //Исключение NoSuchElementException – если этот список пуст  *\\
    @Override
    public T remove() {
        if (counter == 0 ){
            throw new NoSuchElementException("Этот список пуст");
        }
        Object[] array = first.getArray();
        Object output = null;
        for (int i = 0; i < first.getSizeArray(); i++) {
            if (array[i] != null) { //  определяем null или не null
                output = array[i];
                first.setArray(i, null);
                counter -= 1;
                if (i == first.getSizeArray() - 1 && counter != 0){
                    this.first = first.getTripletNext();
                    first.setTripletPrev(null);
                }
                break;
            }
        }
        return (T) output;
    }



    //* Извлекает и удаляет начало очереди, представленное этим списком
    // (другими словами, первый элемент этого списка), или возвращает null,
    // если этот список пуст.
    //Этот метод эквивалентен pollFirst().
    //Возвращается:
    //первый элемент этого списка, или null, если этот список пуст  *\\
    @Override
    public T poll() {
        Object[] array = first.getArray();
        int indexElement = 0;
        int noSuchElementInside = 0;
        for (int i = 0; i < first.getSizeArray(); i++){
            if (array[i] != null){
                indexElement = 1;
            }else{
                noSuchElementInside = 1;
            }
            if (indexElement !=0){
                first.setArray(indexElement, null);
                break;
            }
        }
//        if (counter == 0){
//            return null;
//        }
        //if (indexElement == 0 && noSuchElementInside == 1 ){
        //  return null;
        //}
        return null;
    }



    //* Извлекает, но не удаляет начало очереди,
    // представленное этим списком (другими словами, первый элемент этого списка).
    // Этот метод отличается от peek только тем,
    // что он генерирует исключение, если этот deque пуст.
    //Этот метод эквивалентен getFirst().
    //Возвращается:
    //глава очереди, представленный этим деком
    //Бросает:
    //Исключение NoSuchElementException – если этот список пуст  *\\
    @Override
    public T element() {
        if (counter == 0 ){
            throw new NoSuchElementException("Этот список пуст");
        }
        Object[] array = first.getArray();
        Object output = null;
        for (int i = 0; i < first.getSizeArray(); i++) {
            if (array[i] != null) { //  определяем null или не null
                output = array[i];
                break;
            }
        }
        return (T) output;
    }



    //* Извлекает, но не удаляет начало очереди, представленное
    // этим списком (другими словами, первый элемент этого списка),
    // или возвращает null, если этот список пуст.
    //Этот метод эквивалентен peekFirst().
    //Возвращается:
    //начало очереди, представленное этим списком,
    // или null, если этот список пуст  *\\
    @Override
    public T peek() {
        if (counter == 0 ){
            return null;
        }
        Object[] array = first.getArray();
        Object output = null;
        for (int i = 0; i < first.getSizeArray(); i++) {
            if (array[i] != null) { //  определяем null или не null
                output = array[i];
                break;
            }
        }
        return (T) output;
    }



    //* Добавляет все элементы в указанную коллекцию в конце этого запроса,
    // как будто вызывая addLast для каждого из них, в том порядке,
    // в котором они возвращаются итератором коллекции.
    //При использовании deque с ограниченной пропускной способностью,
    // как правило, предпочтительнее вызывать offer отдельно для каждого элемента.
    //Исключение, возникшее при попытке добавить элемент,
    // может привести к успешному добавлению только некоторых элементов
    // при возникновении соответствующего исключения.
    //Параметры:
    //c – элементы, которые будут вставлены в этот список
    //Возвращается:
    //истинно, если это значение изменилось в результате вызова

    //Бросает:
    //Исключение IllegalStateException – если не все элементы могут быть
    // добавлены в данный момент из-за ограничений на вставку
    //ClassCastException – если класс элемента указанной коллекции
    // препятствует его добавлению в этот список
    //Исключение NullPointerException – если указанная
    // коллекция содержит элемент null, и этот deque не разрешает элементы null,
    // или если указанная коллекция равна null
    //IllegalArgumentException – если som  *\\
    @Override
    public boolean addAll(Collection<? extends T> c) {
        int razmer = counter;

        try {
            for (T etoElement : c){
                add(etoElement);
            }
        } catch (IllegalStateException e){
            return razmer < counter;
        }
        return razmer < counter;
    }



//    //*  не реализую  *\\
    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }


//
//    //*  не реализую *\\
    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }



    //*  Помещает элемент в стек,
    // представленный этим deque (другими словами, во главе этого deque),
    // если это возможно сделать немедленно, не нарушая ограничений емкости,
    // вызывая исключение IllegalStateException, если в данный момент нет свободного места.
    //Этот метод эквивалентен addFirst.
    //Параметры:
    //e – элемент, который нужно нажать
    //Бросает:
    //Исключение IllegalStateException – если элемент не может быть
    // добавлен в данный момент из-за ограничений емкости
    //ClassCastException – если класс указанного элемента
    // препятствует его добавлению в этот список
    //Исключение NullPointerException – если указанный элемент
    // равен null и этот deque не разрешает элементы null
    //Исключение IllegalArgumentException – если какое-либо
    // свойство указанного элемента препятствует его добавлению в этот список *\\
    @Override
    public void push(T t) {
        if (maxSize > counter){
            if (t == null){
                throw new NullPointerException("Указанный элемент равен null ");
            }
            if (counter == 0){
                first.setArray(0,t);
                counter +=1;

            }else{
                Object[] array = first.getArray();
                int indexElement = 0;
                for (int i = 0; i < first.getSizeArray(); i++){
                    if (array[i] != null){
                        indexElement = i;
                        break;
                    }
                }
                if (array[indexElement].getClass().equals(t.getClass())){
                    if (indexElement != 0){
                        first.setArray(indexElement - 1, t);
                        counter += 1;
                    }else{
                        MyTriplet<T> newLeftTriplet = new MyTriplet<>(first, null);
                        newLeftTriplet.setArray(newLeftTriplet.getSizeArray() - 1 , t);
                        counter += 1;
                        first.setTripletPrev(newLeftTriplet);
                        this.first = newLeftTriplet;
                    }

                }else{
                    throw new ClassCastException("Класс указанного элемента препятствует его добавлению в этот список");
                }
            }
        }else{
            throw new IllegalStateException("Элемент не может быть добавлен в данный момент из-за ограничений емкости");
        }

    }



    //*  Извлекает элемент из стека, представленного этим deque.
    // Другими словами, удаляет и возвращает первый элемент этого списка.
    //Этот метод эквивалентен removeFirst().
    //Возвращается:
    //элемент в начале этого списка (который является вершиной стека, представленного этим списком)
    //Бросает:
    //Исключение NoSuchElementException – если этот список пуст *\\
    @Override
    public T pop() {
        Object[] array = first.getArray();
        int indexElement = 0;
        int indexNotNull = 0;
        for (int i =  last.getSizeArray() - 1; i >= 0; i--){
            if (last.getArray()[i] != null ){
                indexNotNull = i;
                break;
            }
        }
        if (indexNotNull == last.getSizeArray()-1){
            last.setArray(indexNotNull+1, null);
        }
        if (indexNotNull == 0){
            throw new NoSuchElementException("этот список пуст");
        }
        return null;
    }



    //*  Удаляет первое вхождение указанного элемента из этого списка.
    // Если deque не содержит элемента, он остается неизменным.
    // Более формально, удаляет первый элемент e таким образом,
    // что Objects.равен(o, e) (если такой элемент существует).
    // Возвращает значение true, если этот deque содержал указанный элемент (или, что эквивалентно,
    // если этот deque изменился в результате вызова).
    //Этот метод эквивалентен removeFirstOccurrence(объект).
    //Параметры:
    //o – элемент, который должен быть удален из этого списка, если он присутствует
    //Возвращается:
    //true, если элемент был удален в результате этого вызова
    //Бросает:
    //ClassCastException – если класс указанного элемента несовместим с этим deque (необязательно)
    //Исключение NullPointerException – если указанный элемент равен null и
    // этот deque не разрешает элементы null (необязательно) *\\
    @Override
    public boolean remove(java.lang.Object o) {
        if (o == null){
            throw new NullPointerException("Указанный элемент равен null");
        }
        if (counter == 0){
            return false;
        } else {
            int indexNotNullFirst = -1; // Flag first elem not null for first (сначала как false)
            boolean vozvrat = false; // возврат
            MyTriplet<T> nowMyTriplet = first;
            int i = 0;
            while (i < first.getSizeArray()){
                if (nowMyTriplet.getArray()[i] != null && indexNotNullFirst == -1) {
                    indexNotNullFirst = i;
                    if (!nowMyTriplet.getArray()[indexNotNullFirst].getClass().equals(o.getClass())){ //если класс индекса not-null элемента не совпадает с классом элемента 'o'
                        throw new ClassCastException("Класс указанного элемента препятствует его добавлению в этот список"); // кидаем эксепшн на ClassCastException
                    }
                }
                if (nowMyTriplet.getArray()[i] != null && nowMyTriplet.getArray()[i].equals(o) ){
                    vozvrat = true;
                    if (i == indexNotNullFirst && nowMyTriplet == first){
                        nowMyTriplet.setArray(indexNotNullFirst, null);
                        counter -= 1;
                        if (nowMyTriplet != last && indexNotNullFirst == nowMyTriplet.getSizeArray() - 1){
                            first = nowMyTriplet.getTripletNext();
                            first.setTripletPrev(null);
                        }
                        return vozvrat;
                    }else{
                        while (nowMyTriplet != last ){
                            if (i == nowMyTriplet.getSizeArray()-1 && nowMyTriplet != last){
                                nowMyTriplet.setArray(i, (T) nowMyTriplet.getTripletNext().getArray()[0]);
                                nowMyTriplet = nowMyTriplet.getTripletNext();
                                i = 0;
                            } else{
                                nowMyTriplet.setArray(i, (T) nowMyTriplet.getArray()[i + 1]);
                                i++;
                            }
                        }
                        while (nowMyTriplet.getArray()[i] != null && i != 4 ){
                            nowMyTriplet.setArray(i, (T) nowMyTriplet.getArray()[i + 1]);
                            i++;
                        }
                        if (i == 4){
                            nowMyTriplet.setArray(i, null);
                        } else {
                            nowMyTriplet.setArray(i - 1, null);
                        }
                        if (i - 1 == 0 && nowMyTriplet == last && nowMyTriplet.getTripletPrev() != null){
                            last = nowMyTriplet.getTripletPrev();
                            last.setTripletNext(null);
                        }
                        counter -= 1;
                        return vozvrat;
                    }
                }
                i++;
                if (i == nowMyTriplet.getSizeArray() && nowMyTriplet != last){
                    nowMyTriplet = nowMyTriplet.getTripletNext();
                    i = 0;
                }else if (i == nowMyTriplet.getSizeArray()){ // при оставшемся nowMyTriplet = last
                    return vozvrat;
                }
            }
        }
        return false;
    }



//    //*   *\\
    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }



    //*  Возвращает значение true,
    // если этот список содержит указанный элемент.
    // Более формально, возвращает true тогда и только тогда,
    // когда этот deque содержит хотя бы один элемент e, такой, что Objects.equals(o, e).
    //Параметры:
    //o – элемент, присутствие которого в этом списке должно быть проверено
    //Возвращается:
    //true, если этот список содержит указанный элемент,
    //Бросает:
    //ClassCastException – если класс указанного элемента
    // несовместим с этим deque (необязательно)
    //Исключение NullPointerException – если указанный элемент
    // равен null и этот deque не разрешает элементы null (необязательно)*\\
    public boolean contains(java.lang.Object o) {
        if (o == null){
            throw new NullPointerException("Указанный элемент равен null");
        }
        if (counter == 0) {
            return false;
        } else {
            boolean vozvrat = false;
            MyTriplet<T> nowMyTriplet = first; // присваиваем значения триплета, с которым сейчас работаем
            int i = 0;
            boolean flag = false;
            while (i < nowMyTriplet.getSizeArray()){
                if (nowMyTriplet.getArray()[i] != null && !flag) {
                    flag = true;
                    if (!nowMyTriplet.getArray()[i].getClass().equals(o.getClass())) {
                        throw new ClassCastException("Класс указанного элемента препятствует его добавлению в этот список");
                    }
                }
                if (nowMyTriplet.getArray()[i] != null && nowMyTriplet.getArray()[i].equals(o)){
                    vozvrat = true;
                    return vozvrat;
                }
                i++;
                if (i == nowMyTriplet.getSizeArray() && nowMyTriplet != last){
                    nowMyTriplet = nowMyTriplet.getTripletNext();
                    i = 0;
                }else if (i == nowMyTriplet.getSizeArray()) { // при оставшемся nowMyTriplet = last
                    return vozvrat;
                }
            }
        }
        return false;
    }


    //* Возвращает количество элементов в этом списке.
    //Возвращается:
    //количество элементов в этом списке  *\\
    @Override
    public int size() {
        return counter;
    }


//    //*   *\\
    @Override
    public boolean isEmpty() {
        return (size() == 0);
    }



    //* Возвращает итератор по элементам в этом списке в правильной последовательности.
    // Элементы будут возвращены в порядке от первого (head) до последнего (tail).
    //Возвращается:
    //итератор по элементам в этом списке в правильной последовательности *\\
    @Override
    public Iterator<T> iterator() {
        return new NewIterator<T>(first, counter );
    }



//    //*   *\\
    @Override
    public java.lang.Object[] toArray() {
        return new java.lang.Object[0];
    }
//
//
//
//    //*   *\\
    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }



    //*Возвращает итератор по элементам в этом списке в правильной последовательности. Элементы будут возвращены в порядке от первого (head) до последнего (tail).
    //Возвращается:
    //итератор по элементам в этом списке в правильной последовательности   *\\
    @Override
    public Iterator<T> descendingIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] getContainerByIndex(int cIndex) {
        int indexOfTriplet = 0;
        MyTriplet<T> nowMyTriplet = first;

        while (nowMyTriplet.getTripletNext() != null || nowMyTriplet == last){
            if (indexOfTriplet == cIndex){
                return nowMyTriplet.getArray();
            }
            indexOfTriplet += 1;
            nowMyTriplet = nowMyTriplet.getTripletNext();
            if (nowMyTriplet == null) {
                return null;
            }
        }
        if (nowMyTriplet == first && indexOfTriplet == cIndex){
            return nowMyTriplet.getArray();
        }
        return null;
    }

    @Override
    public void clear() {
        MyTriplet<T> mainTriplet = new MyTriplet<T>(null, null);
        first =  mainTriplet;
        last = mainTriplet;
        counter = 0;
    }
}

