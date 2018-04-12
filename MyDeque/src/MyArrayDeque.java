/*
 *作者：李加兴
 */


import java.util.*;
import java.util.function.Consumer;

import sun.misc.SharedSecrets;

public class MyArrayDeque<E> extends AbstractCollection<E>
                           implements MyDeque<E>
{
	/**
	 * 存储deque的元素的数组
	 */
	transient Object[] elements;
	
	/**
	 * deque头部元素的下标
	 */
	transient int head;
	
	/**
	 * 接下来将被添加到deque尾部元素的下标
	 */
	transient int tail;
	
	/**
	 * 我们将用于创建deque的最小容量，必须是2的幂
	 */
	private static final int MIN_INITIAL_CAPACITY = 8;
	
	//    *****   deque分配和调整实例    *****
	
    private static int calculateSize(int numElements) {
        int initialCapacity = MIN_INITIAL_CAPACITY;
        // 找到最合适的容量来保存元素
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
     * 分配空数组来保存给定数量的元素
     * @param numElements 元素数量
     */
    private void allocateElements(int numElements) {
        elements = new Object[calculateSize(numElements)];
    }
    
    /**
     * 使deque的容量加倍，只有当deque满了的时候调用
     */
    private void doubleCapacity() {
        assert head == tail;
        int p = head;
        int n = elements.length;
        int r = n - p; // p右边元素的数量
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
     * 将数组中的元素按顺序复制到指定数组中，假定了这个数组足够大
     *
     * @return 它的参数
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
     * 构造 一个能容纳16个元素的初始空deque
     */
    public MyArrayDeque() {
        elements = new Object[16];
    }
    
    /**
     * 构造一个能容纳指定数量元素的初始deque
     *
     * @param numElements  deque能容纳元素的最小数
     */
    public MyArrayDeque(int numElements) {
        allocateElements(numElements);
    }
    
    /**
     * 构造一个包含指定集合中元素的deque
     *
     * @param c 要将元素保存到deque的集合
     * @throws NullPointerException 如果这个集合为空
     */
    public MyArrayDeque(Collection<? extends E> c) {
        allocateElements(c.size());
        addAll(c);
    }
    
    //主要的插入和提取方法是addFirst，
    // addLast、pollFirst pollLast。其他方法定义在。
    //这些方面。
    
    /**
     * 将指定元素插入到deque的前端
     *
     * @param e 要添加的元素
     * @throws NullPointerException 如果元素为null
     */
    public void addFirst(E e) {
        if (e == null)
            throw new NullPointerException();
        elements[head = (head - 1) & (elements.length - 1)] = e;
        if (head == tail)
            doubleCapacity();
    }
    
    /**
     * 将指定元素插入到deque的尾部
     *
     * @param e 要插入的元素
     * @throws NullPointerException 如果元素为null
     */
    public void addLast(E e) {
        if (e == null)
            throw new NullPointerException();
        elements[tail] = e;
        if ( (tail = (tail + 1) & (elements.length - 1)) == head)
            doubleCapacity();
    }
    
    /**
     * 插入指定元素到deque头部
     *
     * @param e 要插入的元素
     * @return 若插入成功返回true
     * @throws NullPointerException 如果指定元素为Null
     */
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }
    
    /**
     * 插入指定元素到deque尾部
     *
     * @param e 要插入的元素
     * @return 若插入成功返回true
     * @throws NullPointerException 如果指定元素为Null
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
        // 元素为null如果deque为空
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
        // elements[head] 为null如果deque为空
        return (E) elements[head];
    }

    @SuppressWarnings("unchecked")
    public E peekLast() {
        return (E) elements[(tail - 1) & (elements.length - 1)];
    }
    
    /**
     * 移除首次出现的指定元素
     *
     * @param o 要被移除的指定元素，如果存在
     * @return 如果指定元素存在，返回true，否则返回false
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
     * 删除最后出现的指定元素
     *
     * @param o 将要删除的指定元素，如果存在
     * @return 如果指定元素存在，返回true，否则返回false
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
    
    //   *** deque方法  ***
    
    /**
     * 添加指定元素值deque尾部
     *
     * 这个方法与addLast等同
     * 
     * @param e 将要添加的元素
     * @return 添加成功则返回true
     * @throws NullPointerException 如果指定元素为null
     */
    public boolean add(E e) {
        addLast(e);
        return true;
    }
    
    /**
     * 将指定元素插入的到deque尾部
     *
     * 这个方法与offerLast等同
     * 
     * @param e 将要添加的元素
     * @return 添加成功则返回true
     * @throws NullPointerException 如果指定元素为null
     */
    public boolean offer(E e) {
        return offerLast(e);
    }
    
    /**
     * 获取并移除deque头部的第一个元素，如果deque为空，抛出异常
     *
     * 这个方法与removeFirst等同
     * 
     * @return deque头部的元素
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E remove() {
        return removeFirst();
    }
    
    /**
     * 获取并移除deque头部的第一个元素
     * 
     * 这个方法与pollFirst等同
     * 
     * @return deque头部的第一个元素，或者为null如果deque为空
     */
    public E poll() {
        return pollFirst();
    }
    
    /**
     * 获取第一个元素，如果deque为空，抛出异常
     *
     * 这个方法与getFirst等同
     *
     * @return deque头部的元素
     * @throws NoSuchElementException {@inheritDoc}
     */
    public E element() {
        return getFirst();
    }
    
    /**
     * 获取deque的第一个元素
     * 
     * 这个方法与peekFirst等同
     *
     * @return deque的第一个元素，如果deque为空，返回null
     */
    public E peek() {
        return peekFirst();
    }
    
    //   *** 栈方法   ***
    
    /**
     * 压栈，即将指定元素添加到deque头部
     *
     * 这个方法与addFirst等同
     *
     * @param e 要压入的元素
     * @throws NullPointerException 如果指定元素为null
     */
    public void push(E e) {
        addFirst(e);
    }
    
    /**
     *出栈，即获取并移除deque的第一个元素
     *
     * 这个方法和removeFirst等同
     *
     * @return deque的第一个元素，即栈顶元素
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
     * 移除数组中指定下标的元素，根据需要调节头部和尾部。这会导致数组中元素的移动
     *
     * @return 如果元素向后移动，返回true，否则返回false
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

        // 优化最小单元
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
    
    //    ***  集合方法   ***
    
    /**
     * 返回对列中的元素数量
     *
     * @return deque中的元素数
     */
    public int size() {
        return (tail - head) & (elements.length - 1);
    }
    
    /**
     * 判断deque是否含有元素
     *
     * @return 如果deque为空，返回true，否则返回false
     */
    public boolean isEmpty() {
        return head == tail;
    }
    
    /**
     * 返回一个deque的迭代器，元素会从头部到尾部按顺序排列
     *
     * @return 这个deque的迭代器
     */
    public Iterator<E> iterator() {
        return new DeqIterator();
    }
    
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }
    
    private class DeqIterator implements Iterator<E> {
        /**
         * 将在随后的调用中返回的元素的索引
         */
        private int cursor = head;

        /**
         * 构造函数中的尾部记录 (也是移除中的),停止迭代并检查修正
         */
        private int fence = tail;

        /**
         * 最最近的调用下返回的元素索引，如果元素被删除，重置为-1
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
         * 这个类是DeqIterator的镜像，元素的顺序是相反的
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
     * 检查deque是否含有指定元素
     *
     * @param o 待检查的元素
     * @return 如果deque包含指定元素，返回true，否则返回false
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
     * 移除第一次出现的指定元素，若deque中无指定元素，deque不改变
     *
     * 这个方法与removeFirstOccurrence等同
     *
     * @param o 指定要删除的元素
     * @return 返回true如果元素存在，否则返回false
     */
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }
    
    /**
     * 清除deque中的所有元素，调用方法后，deque将会为空deque
     */
    public void clear() {
        int h = head;
        int t = tail;
        if (h != t) { // 清除所有单元
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
     * 返回一个以合适的顺序（从第一个到最后一个）包含deque中所有元素的数组
     *
     * 这个数组为新数组，用户可以改变它
     *
     * @return 一个包含deque中所有元素的数组
     */
    public Object[] toArray() {
        return copyElements(new Object[size()]);
    }
    
    /**
     * 返回一个以合适的顺序（从第一个到最后一个）包含deque中所有元素的数组
     * 
     * 若指定数组能够容纳deque中的所有数组，则返回添加了deque元素的参数数组，否则返回一个新数组（元素类型为数组中元素类型）
     *
     * @param a deque元素将要添加到的数组
     * @return 一个包含deque中所有元素的数组
     * @throws ArrayStoreException 如果数组元素的类型不是deque元素的超类
     * @throws NullPointerException 如果数组为空
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
    
    //   *** 对象方法  ***
    
    /**
     * 返回一个deque的复制
     *
     * @return 这个deque的复制
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
     * 将这个deque保存到一个流(即序列化它)。
     *
     * @serialData deque的当前大小，之后是所有元素（每个对象引用）顺序从头到尾
     */
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        s.defaultWriteObject();

        // 写出大小
        s.writeInt(size());

        // 按顺序写出元素
        int mask = elements.length - 1;
        for (int i = head; i != tail; i = (i + 1) & mask)
            s.writeObject(elements[i]);
    }
    
    /**
     * 从流中重新定义这个deque(即反序列化它)
     */
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();

        // 读取大小和分配数组
        int size = s.readInt();
        int capacity = calculateSize(size);
        SharedSecrets.getJavaOISAccess().checkArray(s, Object[].class, capacity);
        allocateElements(size);
        head = 0;
        tail = size;

        // 按正确顺序阅读所有元素
        for (int i = 0; i < size; i++)
            elements[i] = s.readObject();
    }
    
    /**
     * 创建一个deque中元素的后期绑定的并行遍历迭代器
     *
     * @return deque元素的并行遍历迭代器
     * @since 1.8
     */
    public Spliterator<E> spliterator() {
        return new DeqSpliterator<E>(this, -1, -1);
    }
    
    static final class DeqSpliterator<E> implements Spliterator<E> {
        private final MyArrayDeque<E> deq;
        private int fence;  // 值为-1直到第一次使用
        private int index;  // 当前索引，在遍历/拆分上修改

        /** 创建覆盖给定数组和范围的新的spliterator */
        DeqSpliterator(MyArrayDeque<E> deq, int origin, int fence) {
            this.deq = deq;
            this.index = origin;
            this.fence = fence;
        }

        private int getFence() { // 强迫初始化
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
   