/*
 *���ߣ������
 */


import java.util.*;
import java.util.function.Consumer;

import sun.misc.SharedSecrets;

public class MyArrayDeque<E> extends AbstractCollection<E>
                           implements MyDeque<E>
{
	/**
	 * �洢deque��Ԫ�ص�����
	 */
	transient Object[] elements;
	
	/**
	 * dequeͷ��Ԫ�ص��±�
	 */
	transient int head;
	
	/**
	 * ������������ӵ�dequeβ��Ԫ�ص��±�
	 */
	transient int tail;
	
	/**
	 * ���ǽ����ڴ���deque����С������������2����
	 */
	private static final int MIN_INITIAL_CAPACITY = 8;
	
	//    *****   deque����͵���ʵ��    *****
	
    private static int calculateSize(int numElements) {
        int initialCapacity = MIN_INITIAL_CAPACITY;
        // �ҵ�����ʵ�����������Ԫ��
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            initialCapacity |= (initialCapacity >>>  1);
            initialCapacity |= (initialCapacity >>>  2);
            initialCapacity |= (initialCapacity >>>  4);
            initialCapacity |= (initialCapacity >>>  8);
            initialCapacity |= (initialCapacity >>> 16);
            initialCapacity++;

            if (initialCapacity < 0)   
                initialCapacity >>>= 1;
        }
        return initialCapacity;
    }
    
    /**
     * ������������������������Ԫ��
     * @param numElements Ԫ������
     */
    private void allocateElements(int numElements) {
        elements = new Object[calculateSize(numElements)];
    }
    
    /**
     * ʹdeque�������ӱ���ֻ�е�deque���˵�ʱ�����
     */
    private void doubleCapacity() {
        assert head == tail;
        int p = head;
        int n = elements.length;
        int r = n - p; // p�ұ�Ԫ�ص�����
        int newCapacity = n << 1;
        if (newCapacity < 0)
            throw new IllegalStateException("Sorry, deque too big");
        Object[] a = new Object[newCapacity];
        System.arraycopy(elements, p, a, 0, r);
        System.arraycopy(elements, 0, a, r, p);
        elements = a;
        head = 0;
        tail = n;
    }
    
    /**
     * �������е�Ԫ�ذ�˳���Ƶ�ָ�������У��ٶ�����������㹻��
     *
     * @return ���Ĳ���
     */
    private <T> T[] copyElements(T[] a) {
        if (head < tail) {
            System.arraycopy(elements, head, a, 0, size());
        } else if (head > tail) {
            int headPortionLen = elements.length - head;
            System.arraycopy(elements, head, a, 0, headPortionLen);
            System.arraycopy(elements, 0, a, headPortionLen, tail);
        }
        return a;
    }
    
    /**
     * ���� һ��������16��Ԫ�صĳ�ʼ��deque
     */
    public MyArrayDeque() {
        elements = new Object[16];
    }
    
    /**
     * ����һ��������ָ������Ԫ�صĳ�ʼdeque
     *
     * @param numElements  deque������Ԫ�ص���С��
     */
    public MyArrayDeque(int numElements) {
        allocateElements(numElements);
    }
    
    /**
     * ����һ������ָ��������Ԫ�ص�deque
     *
     * @param c Ҫ��Ԫ�ر��浽deque�ļ���
     * @throws NullPointerException ����������Ϊ��
     */
    public MyArrayDeque(Collection<? extends E> c) {
        allocateElements(c.size());
        addAll(c);
    }
    
    //��Ҫ�Ĳ������ȡ������addFirst��
    // addLast��pollFirst pollLast���������������ڡ�
    //��Щ���档
    
    /**
     * ��ָ��Ԫ�ز��뵽deque��ǰ��
     *
     * @param e Ҫ��ӵ�Ԫ��
     * @throws NullPointerException ���Ԫ��Ϊnull
     */
    public void addFirst(E e) {
        if (e == null)
            throw new NullPointerException();
        elements[head = (head - 1) & (elements.length - 1)] = e;
        if (head == tail)
            doubleCapacity();
    }
    
    /**
     * ��ָ��Ԫ�ز��뵽deque��β��
     *
     * @param e Ҫ�����Ԫ��
     * @throws NullPointerException ���Ԫ��Ϊnull
     */
    public void addLast(E e) {
        if (e == null)
            throw new NullPointerException();
        elements[tail] = e;
        if ( (tail = (tail + 1) & (elements.length - 1)) == head)
            doubleCapacity();
    }
    
    /**
     * ����ָ��Ԫ�ص�dequeͷ��
     *
     * @param e Ҫ�����Ԫ��
     * @return ������ɹ�����true
     * @throws NullPointerException ���ָ��Ԫ��ΪNull
     */
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }
    
    /**
     * ����ָ��Ԫ�ص�dequeβ��
     *
     * @param e Ҫ�����Ԫ��
     * @return ������ɹ�����true
     * @throws NullPointerException ���ָ��Ԫ��ΪNull
     */
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }
    
    /**
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E removeFirst() {
        E x = pollFirst();
        if (x == null)
            throw new NoSuchElementException();
        return x;
    }
    
    /**
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E removeLast() {
        E x = pollLast();
        if (x == null)
            throw new NoSuchElementException();
        return x;
    }

    public E pollFirst() {
        int h = head;
        @SuppressWarnings("unchecked")
        E result = (E) elements[h];
        // Ԫ��Ϊnull���dequeΪ��
        if (result == null)
            return null;
        elements[h] = null;     
        head = (h + 1) & (elements.length - 1);
        return result;
    }

    public E pollLast() {
        int t = (tail - 1) & (elements.length - 1);
        @SuppressWarnings("unchecked")
        E result = (E) elements[t];
        if (result == null)
            return null;
        elements[t] = null;
        tail = t;
        return result;
    }

    /**
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E getFirst() {
        @SuppressWarnings("unchecked")
        E result = (E) elements[head];
        if (result == null)
            throw new NoSuchElementException();
        return result;
    }

    /**
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E getLast() {
        @SuppressWarnings("unchecked")
        E result = (E) elements[(tail - 1) & (elements.length - 1)];
        if (result == null)
            throw new NoSuchElementException();
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public E peekFirst() {
        // elements[head] Ϊnull���dequeΪ��
        return (E) elements[head];
    }

    @SuppressWarnings("unchecked")
    public E peekLast() {
        return (E) elements[(tail - 1) & (elements.length - 1)];
    }
    
    /**
     * �Ƴ��״γ��ֵ�ָ��Ԫ��
     *
     * @param o Ҫ���Ƴ���ָ��Ԫ�أ��������
     * @return ���ָ��Ԫ�ش��ڣ�����true�����򷵻�false
     */
    public boolean removeFirstOccurrence(Object o) {
        if (o == null)
            return false;
        int mask = elements.length - 1;
        int i = head;
        Object x;
        while ( (x = elements[i]) != null) {
            if (o.equals(x)) {
                delete(i);
                return true;
            }
            i = (i + 1) & mask;
        }
        return false;
    }
    
    /**
     * ɾ�������ֵ�ָ��Ԫ��
     *
     * @param o ��Ҫɾ����ָ��Ԫ�أ��������
     * @return ���ָ��Ԫ�ش��ڣ�����true�����򷵻�false
     */
    public boolean removeLastOccurrence(Object o) {
        if (o == null)
            return false;
        int mask = elements.length - 1;
        int i = (tail - 1) & mask;
        Object x;
        while ( (x = elements[i]) != null) {
            if (o.equals(x)) {
                delete(i);
                return true;
            }
            i = (i - 1) & mask;
        }
        return false;
    }
    
    //   *** deque����  ***
    
    /**
     * ���ָ��Ԫ��ֵdequeβ��
     *
     * ���������addLast��ͬ
     * 
     * @param e ��Ҫ��ӵ�Ԫ��
     * @return ��ӳɹ��򷵻�true
     * @throws NullPointerException ���ָ��Ԫ��Ϊnull
     */
    public boolean add(E e) {
        addLast(e);
        return true;
    }
    
    /**
     * ��ָ��Ԫ�ز���ĵ�dequeβ��
     *
     * ���������offerLast��ͬ
     * 
     * @param e ��Ҫ��ӵ�Ԫ��
     * @return ��ӳɹ��򷵻�true
     * @throws NullPointerException ���ָ��Ԫ��Ϊnull
     */
    public boolean offer(E e) {
        return offerLast(e);
    }
    
    /**
     * ��ȡ���Ƴ�dequeͷ���ĵ�һ��Ԫ�أ����dequeΪ�գ��׳��쳣
     *
     * ���������removeFirst��ͬ
     * 
     * @return dequeͷ����Ԫ��
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E remove() {
        return removeFirst();
    }
    
    /**
     * ��ȡ���Ƴ�dequeͷ���ĵ�һ��Ԫ��
     * 
     * ���������pollFirst��ͬ
     * 
     * @return dequeͷ���ĵ�һ��Ԫ�أ�����Ϊnull���dequeΪ��
     */
    public E poll() {
        return pollFirst();
    }
    
    /**
     * ��ȡ��һ��Ԫ�أ����dequeΪ�գ��׳��쳣
     *
     * ���������getFirst��ͬ
     *
     * @return dequeͷ����Ԫ��
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E element() {
        return getFirst();
    }
    
    /**
     * ��ȡdeque�ĵ�һ��Ԫ��
     * 
     * ���������peekFirst��ͬ
     *
     * @return deque�ĵ�һ��Ԫ�أ����dequeΪ�գ�����null
     */
    public E peek() {
        return peekFirst();
    }
    
    //   *** ջ����   ***
    
    /**
     * ѹջ������ָ��Ԫ����ӵ�dequeͷ��
     *
     * ���������addFirst��ͬ
     *
     * @param e Ҫѹ���Ԫ��
     * @throws NullPointerException ���ָ��Ԫ��Ϊnull
     */
    public void push(E e) {
        addFirst(e);
    }
    
    /**
     *��ջ������ȡ���Ƴ�deque�ĵ�һ��Ԫ��
     *
     * ���������removeFirst��ͬ
     *
     * @return deque�ĵ�һ��Ԫ�أ���ջ��Ԫ��
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E pop() {
        return removeFirst();
    }
    
    private void checkInvariants() {
        assert elements[tail] == null;
        assert head == tail ? elements[head] == null :
            (elements[head] != null &&
             elements[(tail - 1) & (elements.length - 1)] != null);
        assert elements[(head - 1) & (elements.length - 1)] == null;
    }
    
    /**
     * �Ƴ�������ָ���±��Ԫ�أ�������Ҫ����ͷ����β������ᵼ��������Ԫ�ص��ƶ�
     *
     * @return ���Ԫ������ƶ�������true�����򷵻�false
     */
    private boolean delete(int i) {
        checkInvariants();
        final Object[] elements = this.elements;
        final int mask = elements.length - 1;
        final int h = head;
        final int t = tail;
        final int front = (i - h) & mask;
        final int back  = (t - i) & mask;

        if (front >= ((t - h) & mask))
            throw new ConcurrentModificationException();

        // �Ż���С��Ԫ
        if (front < back) {
            if (h <= i) {
                System.arraycopy(elements, h, elements, h + 1, front);
            } else { 
                System.arraycopy(elements, 0, elements, 1, i);
                elements[0] = elements[mask];
                System.arraycopy(elements, h, elements, h + 1, mask - h);
            }
            elements[h] = null;
            head = (h + 1) & mask;
            return false;
        } else {
            if (i < t) { 
                System.arraycopy(elements, i + 1, elements, i, back);
                tail = t - 1;
            } else { 
                System.arraycopy(elements, i + 1, elements, i, mask - i);
                elements[mask] = elements[0];
                System.arraycopy(elements, 1, elements, 0, t);
                tail = (t - 1) & mask;
            }
            return true;
        }
    }
    
    //    ***  ���Ϸ���   ***
    
    /**
     * ���ض����е�Ԫ������
     *
     * @return deque�е�Ԫ����
     */
    public int size() {
        return (tail - head) & (elements.length - 1);
    }
    
    /**
     * �ж�deque�Ƿ���Ԫ��
     *
     * @return ���dequeΪ�գ�����true�����򷵻�false
     */
    public boolean isEmpty() {
        return head == tail;
    }
    
    /**
     * ����һ��deque�ĵ�������Ԫ�ػ��ͷ����β����˳������
     *
     * @return ���deque�ĵ�����
     */
    public Iterator<E> iterator() {
        return new DeqIterator();
    }
    
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }
    
    private class DeqIterator implements Iterator<E> {
        /**
         * �������ĵ����з��ص�Ԫ�ص�����
         */
        private int cursor = head;

        /**
         * ���캯���е�β����¼ (Ҳ���Ƴ��е�),ֹͣ�������������
         */
        private int fence = tail;

        /**
         * ������ĵ����·��ص�Ԫ�����������Ԫ�ر�ɾ��������Ϊ-1
         */
        private int lastRet = -1;

        public boolean hasNext() {
            return cursor != fence;
        }

        public E next() {
            if (cursor == fence)
                throw new NoSuchElementException();
            @SuppressWarnings("unchecked")
            E result = (E) elements[cursor];
            if (tail != fence || result == null)
                throw new ConcurrentModificationException();
            lastRet = cursor;
            cursor = (cursor + 1) & (elements.length - 1);
            return result;
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            if (delete(lastRet)) { 
                cursor = (cursor - 1) & (elements.length - 1);
                fence = tail;
            }
            lastRet = -1;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            Object[] a = elements;
            int m = a.length - 1, f = fence, i = cursor;
            cursor = f;
            while (i != f) {
                @SuppressWarnings("unchecked") E e = (E)a[i];
                i = (i + 1) & m;
                if (e == null)
                    throw new ConcurrentModificationException();
                action.accept(e);
            }
        }
    }
    
    private class DescendingIterator implements Iterator<E> {
        /*
         * �������DeqIterator�ľ���Ԫ�ص�˳�����෴��
         */
        private int cursor = tail;
        private int fence = head;
        private int lastRet = -1;

        public boolean hasNext() {
            return cursor != fence;
        }

        public E next() {
            if (cursor == fence)
                throw new NoSuchElementException();
            cursor = (cursor - 1) & (elements.length - 1);
            @SuppressWarnings("unchecked")
            E result = (E) elements[cursor];
            if (head != fence || result == null)
                throw new ConcurrentModificationException();
            lastRet = cursor;
            return result;
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            if (!delete(lastRet)) {
                cursor = (cursor + 1) & (elements.length - 1);
                fence = head;
            }
            lastRet = -1;
        }
    }
    
    /**
     * ���deque�Ƿ���ָ��Ԫ��
     *
     * @param o ������Ԫ��
     * @return ���deque����ָ��Ԫ�أ�����true�����򷵻�false
     */
    public boolean contains(Object o) {
        if (o == null)
            return false;
        int mask = elements.length - 1;
        int i = head;
        Object x;
        while ( (x = elements[i]) != null) {
            if (o.equals(x))
                return true;
            i = (i + 1) & mask;
        }
        return false;
    }
    
    /**
     * �Ƴ���һ�γ��ֵ�ָ��Ԫ�أ���deque����ָ��Ԫ�أ�deque���ı�
     *
     * ���������removeFirstOccurrence��ͬ
     *
     * @param o ָ��Ҫɾ����Ԫ��
     * @return ����true���Ԫ�ش��ڣ����򷵻�false
     */
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }
    
    /**
     * ���deque�е�����Ԫ�أ����÷�����deque����Ϊ��deque
     */
    public void clear() {
        int h = head;
        int t = tail;
        if (h != t) { // ������е�Ԫ
            head = tail = 0;
            int i = h;
            int mask = elements.length - 1;
            do {
                elements[i] = null;
                i = (i + 1) & mask;
            } while (i != t);
        }
    }
    
    /**
     * ����һ���Ժ��ʵ�˳�򣨴ӵ�һ�������һ��������deque������Ԫ�ص�����
     *
     * �������Ϊ�����飬�û����Ըı���
     *
     * @return һ������deque������Ԫ�ص�����
     */
    public Object[] toArray() {
        return copyElements(new Object[size()]);
    }
    
    /**
     * ����һ���Ժ��ʵ�˳�򣨴ӵ�һ�������һ��������deque������Ԫ�ص�����
     * 
     * ��ָ�������ܹ�����deque�е��������飬�򷵻������dequeԪ�صĲ������飬���򷵻�һ�������飨Ԫ������Ϊ������Ԫ�����ͣ�
     *
     * @param a dequeԪ�ؽ�Ҫ��ӵ�������
     * @return һ������deque������Ԫ�ص�����
     * @throws ArrayStoreException �������Ԫ�ص����Ͳ���dequeԪ�صĳ���
     * @throws NullPointerException �������Ϊ��
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        int size = size();
        if (a.length < size)
            a = (T[])java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        copyElements(a);
        if (a.length > size)
            a[size] = null;
        return a;
    }
    
    //   *** ���󷽷�  ***
    
    /**
     * ����һ��deque�ĸ���
     *
     * @return ���deque�ĸ���
     */
    public MyArrayDeque<E> clone() {
        try {
            @SuppressWarnings("unchecked")
            MyArrayDeque<E> result = (MyArrayDeque<E>) super.clone();
            result.elements = Arrays.copyOf(elements, elements.length);
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    
    private static final long serialVersionUID = 2340985798034038923L;

    /**
     * �����deque���浽һ����(�����л���)��
     *
     * @serialData deque�ĵ�ǰ��С��֮��������Ԫ�أ�ÿ���������ã�˳���ͷ��β
     */
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        s.defaultWriteObject();

        // д����С
        s.writeInt(size());

        // ��˳��д��Ԫ��
        int mask = elements.length - 1;
        for (int i = head; i != tail; i = (i + 1) & mask)
            s.writeObject(elements[i]);
    }
    
    /**
     * ���������¶������deque(�������л���)
     */
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();

        // ��ȡ��С�ͷ�������
        int size = s.readInt();
        int capacity = calculateSize(size);
        SharedSecrets.getJavaOISAccess().checkArray(s, Object[].class, capacity);
        allocateElements(size);
        head = 0;
        tail = size;

        // ����ȷ˳���Ķ�����Ԫ��
        for (int i = 0; i < size; i++)
            elements[i] = s.readObject();
    }
    
    /**
     * ����һ��deque��Ԫ�صĺ��ڰ󶨵Ĳ��б���������
     *
     * @return dequeԪ�صĲ��б���������
     * @since 1.8
     */
    public Spliterator<E> spliterator() {
        return new DeqSpliterator<E>(this, -1, -1);
    }
    
    static final class DeqSpliterator<E> implements Spliterator<E> {
        private final MyArrayDeque<E> deq;
        private int fence;  // ֵΪ-1ֱ����һ��ʹ��
        private int index;  // ��ǰ�������ڱ���/������޸�

        /** �������Ǹ�������ͷ�Χ���µ�spliterator */
        DeqSpliterator(MyArrayDeque<E> deq, int origin, int fence) {
            this.deq = deq;
            this.index = origin;
            this.fence = fence;
        }

        private int getFence() { // ǿ�ȳ�ʼ��
            int t;
            if ((t = fence) < 0) {
                t = fence = deq.tail;
                index = deq.head;
            }
            return t;
        }

        public DeqSpliterator<E> trySplit() {
            int t = getFence(), h = index, n = deq.elements.length;
            if (h != t && ((h + 1) & (n - 1)) != t) {
                if (h > t)
                    t += n;
                int m = ((h + t) >>> 1) & (n - 1);
                return new DeqSpliterator<>(deq, h, index = m);
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            if (consumer == null)
                throw new NullPointerException();
            Object[] a = deq.elements;
            int m = a.length - 1, f = getFence(), i = index;
            index = f;
            while (i != f) {
                @SuppressWarnings("unchecked") E e = (E)a[i];
                i = (i + 1) & m;
                if (e == null)
                    throw new ConcurrentModificationException();
                consumer.accept(e);
            }
        }

        public boolean tryAdvance(Consumer<? super E> consumer) {
            if (consumer == null)
                throw new NullPointerException();
            Object[] a = deq.elements;
            int m = a.length - 1, f = getFence(), i = index;
            if (i != fence) {
                @SuppressWarnings("unchecked") E e = (E)a[i];
                index = (i + 1) & m;
                if (e == null)
                    throw new ConcurrentModificationException();
                consumer.accept(e);
                return true;
            }
            return false;
        }

        public long estimateSize() {
            int n = getFence() - index;
            if (n < 0)
                n += deq.elements.length;
            return (long) n;
        }

        @Override
        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED |
                Spliterator.NONNULL | Spliterator.SUBSIZED;
        }
    }
    
}
   