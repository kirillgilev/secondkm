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
        if (maxSize >= counter){
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
        if (maxSize >= counter) {
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
        if (maxSize >= counter) {
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
        if (maxSize >= counter){
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



    //* Извлекает и удаляет последний элемент этого списка. Этот метод отличается от pollLast только тем, что он генерирует исключение, если этот список пуст.
    //Возвращается:
    //хвост этой деки
    //Бросает:
    //Исключение NoSuchElementException – если этот список пуст
    // *\\
    @Override
    public T removeLast() {
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


    //*   Извлекает и удаляет первый элемент этого списка или возвращает null, если этот список пуст.
    //Возвращается:
    //заголовок этого списка, или null, если этот список пуст*\\
    @Override
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

    //* Извлекает и удаляет последний элемент этого списка или возвращает null, если этот список пуст.
    //Возвращается:
    //конец этого списка, или null, если этот список пуст  *\\
    @Override
    public T pollLast() {
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
        return null;
    }



    //*  Извлекает, но не удаляет последний элемент этого списка. Этот метод отличается от peekLast только тем, что он генерирует исключение, если этот список пуст.
    //Возвращается:
    //хвост этой деки
    //Бросает:
    //Исключение NoSuchElementException – если этот список пуст *\\
    @Override
    public T getLast() {
        return null;
    }



    //*  Извлекает, но не удаляет первый элемент этого списка или возвращает null, если этот список пуст.
    //Возвращается:
    //заголовок этого списка, или null, если этот список пуст *\\
    @Override
    public T peekFirst() {
        return null;
    }




    //* Извлекает, но не удаляет последний элемент этого списка или возвращает null, если этот список пуст.
    //Возвращается:
    //конец этого списка, или null, если этот список пуст  *\\
    @Override
    public T peekLast() {
        return null;
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
            MyTriplet<T> nowMyTriplet = last; // присваиваем значениея триплета, с которым сейчас работаем
            int i = nowMyTriplet.getSizeArray();
            while (i >= 0){
                if (nowMyTriplet.getArray()[i] != null && indexNotNull == -1) {
                    indexNotNull = i;
                    if (!nowMyTriplet.getArray()[indexNotNull].getClass().equals(o.getClass())) {
                        throw new ClassCastException("Класс указанного элемента препятствует его добавлению в этот список");
                    }
                }
                if (nowMyTriplet.getArray()[i].equals(o) && nowMyTriplet.getArray()[i] != null){
                    vozvrat = true;
                    if (i == indexNotNull && indexNotNull == 0 && nowMyTriplet == last){
                        nowMyTriplet.setArray(indexNotNull, null);
                        if (nowMyTriplet != first){
                            nowMyTriplet = nowMyTriplet.getTripletPrev();
                            nowMyTriplet.setTripletNext(null);
                        }
                    }else{
                        while (i != indexNotNull){
                            nowMyTriplet.setArray(i, (T) nowMyTriplet.getArray()[i + 1]);
                            i++;
                        }
                        nowMyTriplet.setArray(indexNotNull, null);
                    }
//                    break;
                }
                i--;
                if (i == -1 && nowMyTriplet != first){
                    nowMyTriplet = nowMyTriplet.getTripletPrev();
                    i = nowMyTriplet.getSizeArray() - 1; //   i = 4
                }else if (i == -1 ){
                    return vozvrat;
                }
                // не дописан
            }
        }


        return false;
    }



    //*   *\\
    @Override
    public boolean add(T t) {
        return false;
    }



    //*   *\\
    @Override
    public boolean offer(T t) {
        return false;
    }



    //*   *\\
    @Override
    public T remove() {
        return null;
    }



    //*   *\\
    @Override
    public T poll() {
        return null;
    }



    //*   *\\
    @Override
    public T element() {
        return null;
    }



    //*   *\\
    @Override
    public T peek() {
        return null;
    }



    //*   *\\
    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }



    //*   *\\
    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }



    //*   *\\
    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }



    //*   *\\
    @Override
    public void clear() {

    }


    //*   *\\
    @Override
    public void push(T t) {

    }



    //*   *\\
    @Override
    public T pop() {
        return null;
    }



    //*   *\\
    @Override
    public boolean remove(java.lang.Object o) {
        return false;
    }



    //*   *\\
    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }



    //*   *\\
    @Override
    public boolean contains(java.lang.Object o) {
        return false;
    }


    //*   *\\
    @Override
    public int size() {
        return 0;
    }


    //*   *\\
    @Override
    public boolean isEmpty() {
        return false;
    }



    //*   *\\
    @Override
    public Iterator<T> iterator() {
        return null;
    }



    //*   *\\
    @Override
    public java.lang.Object[] toArray() {
        return new java.lang.Object[0];
    }



    //*   *\\
    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }



    //*Возвращает итератор по элементам в этом списке в правильной последовательности. Элементы будут возвращены в порядке от первого (head) до последнего (tail).
    //Возвращается:
    //итератор по элементам в этом списке в правильной последовательности   *\\
    @Override
    public Iterator<T> descendingIterator() {
        return null;
    }

    @Override
    public Object[] getContainerByIndex(int cIndex) {
        return new Object[0];
    }
}
