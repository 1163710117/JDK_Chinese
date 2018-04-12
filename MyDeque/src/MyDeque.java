/*
 * 作者：李加兴
 */


import java.util.Iterator;
import java.util.Queue;

/**
 * 允许从两端操作的线性集合
 * @author 李加兴
 *
 * @param <E> 这个集合中的元素类型
 */
public interface MyDeque<E> extends Queue<E>{
	
	/**
	 * 在队列的最前端插入指定元素，若没足够则空间，则抛出异常
	 * @param e 需要插入的元素
	 */
	void addFirst(E e);
	
	/**
	 * 在队列的最后端插入指定元素，若没足够空间，则抛出异常
	 * @param e 需要插入的元素
	 */
	void addLast(E e);
	
	/**
	 * 若空间足够，向队列的最前端插入指定元素
	 * @param e 需要插入的元素
	 * @return 若插入成功，返回true，否则返回false
	 */
	boolean offerFirst(E e);
	
	/**
	 * 若空间足够，向队列的最后端插入指定元素
	 * @param e 需要插入的元素
	 * @return 若插入成功，返回true，否则返回false
	 */
	boolean offerLast(E e);
	
	/**
	 * 移除队列的第一个元素，如果队列为空，抛出异常
	 * @return 被移除的元素
	 */
	E removeFirst();
	
	/**
	 * 移除队列的最后一个元素，若队列为空，抛出异常
	 * @return 被移除的元素
	 */
	E removeLast();
	
	/**
	 * 移除队列的第一个元素
	 * @return 若队列为空，返回值为null，否则为被移除的元素
	 */
	E pollFirst();
	
	/**
	 * 移除队列的最后一位元素
	 * @return 若队列为空，返回值为null，否则为被移除的元素
	 */
	E pollLast();
	
	/**
	 * 获取队列的第一个元素，若队列为空，抛出异常
	 * @return 队列的第一个元素
	 */
	E getFirst();
	
	/**
	 * 获取队列的最后一个元素，若队列为空，抛出异常
	 * @return 队列的最后一个元素
	 */
	E getLast();
	
	/**
	 * 获取队列的第一个元素
	 * @return 若队列为空，返回值为null，否则为队列的第一个元素
	 */
	E peekFirst();
	
	/**
	 * 获取队列的最后一个元素
	 * @return 若队列为空，返回值为null，否则为队列的最后一个元素
	 */
	E peekLast();
	
	/**
	 * 删除首次出现的目标元素
	 * @param o 要删除的元素
	 * @return 如果目标元素存在，返回true，否则返回false
	 */
	boolean removeFirstOccurrence(Object o);
	
	/**
	 * 删除最后出现的目标元素
	 * @param o 要删除的元素
	 * @return 如果目标元素存在，返回true，否则返回false
	 */
	boolean removeLastOccurrence(Object o);
	
	//  *** 队列方法  ***
	
	/**
	 * 向队列尾部添加指定元素，若空间不足，则抛出异常
	 * @param 要添加的元素
	 * @return 若成功添加，返回true
	 */
	boolean add(E e);
	
	/**
	 * 向队列尾部添加指定元素
	 * @param 若成功添加，返回true，否则返回false
	 */
	boolean offer(E e);
	
	/**
	 * 删除队列的第一个元素，若队列为空，抛出异常
	 * @return 删除的元素
	 */
	E remove();
	
	/**
	 * 删除队列的第一个元素
	 * @return 如果队列为空，返回值为null，否则返回被删除的元素
	 */
	E poll();
	
	/**
	 * 获取队列的第一个元素，如果队列为空，抛出异常
	 * @return 队列的第一个元素
	 */
	E element();
	
	/**
	 * 获取队列的第一个元素
	 * @return 若队列为空，返回值为null，否则为队列的第一个元素
	 */
	E peek();
	
	//  ***  Stack methods  ***
	
	/**
	 * 将指定元素添加到队列的首端，若空间不足，抛出异常
	 * @param e 要添加的元素
	 */
	void push(E e);
	
	/**
	 * 将第一个元素弹出队列，若队列为空，抛出异常
	 * @return 队列的第一个元素
	 */
	E pop();
	
	// *** Collection methods ***
	
	/**
	 * 移除第一次出现的指定元素
	 * @param o 指定要移除的元素
	 * @return 如果存在指定元素，返回true，否则返回false
	 */
	boolean remove(Object o);
	
	/**
	 * 判断队列是否存在指定元素
	 * @param 指定元素
	 * @return 如果存在指定元素，返回true，否则返回false
	 */
	boolean contains(Object o);
	
	/**
	 * 返回队列中元素的个数
	 * @return 队列中元素的个数
	 */
	public int size();
	
	/**
	 * 返回队列的正序迭代器，即从第一个元素到最后一个元素
	 * @return 队列的正序迭代器
	 */
	Iterator<E> iterator();
	
	/**
	 * 返回队列的反序迭代器，即从最后一个元素到第一个元素
	 * @return 队列的反序迭代器
	 */
	Iterator<E>  descendingIterator();
	
}
