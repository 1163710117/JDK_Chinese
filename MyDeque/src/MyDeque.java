/*
 * ���ߣ������
 */


import java.util.Iterator;
import java.util.Queue;

/**
 * ��������˲��������Լ���
 * @author �����
 *
 * @param <E> ��������е�Ԫ������
 */
public interface MyDeque<E> extends Queue<E>{
	
	/**
	 * �ڶ��е���ǰ�˲���ָ��Ԫ�أ���û�㹻��ռ䣬���׳��쳣
	 * @param e ��Ҫ�����Ԫ��
	 */
	void addFirst(E e);
	
	/**
	 * �ڶ��е����˲���ָ��Ԫ�أ���û�㹻�ռ䣬���׳��쳣
	 * @param e ��Ҫ�����Ԫ��
	 */
	void addLast(E e);
	
	/**
	 * ���ռ��㹻������е���ǰ�˲���ָ��Ԫ��
	 * @param e ��Ҫ�����Ԫ��
	 * @return ������ɹ�������true�����򷵻�false
	 */
	boolean offerFirst(E e);
	
	/**
	 * ���ռ��㹻������е����˲���ָ��Ԫ��
	 * @param e ��Ҫ�����Ԫ��
	 * @return ������ɹ�������true�����򷵻�false
	 */
	boolean offerLast(E e);
	
	/**
	 * �Ƴ����еĵ�һ��Ԫ�أ��������Ϊ�գ��׳��쳣
	 * @return ���Ƴ���Ԫ��
	 */
	E removeFirst();
	
	/**
	 * �Ƴ����е����һ��Ԫ�أ�������Ϊ�գ��׳��쳣
	 * @return ���Ƴ���Ԫ��
	 */
	E removeLast();
	
	/**
	 * �Ƴ����еĵ�һ��Ԫ��
	 * @return ������Ϊ�գ�����ֵΪnull������Ϊ���Ƴ���Ԫ��
	 */
	E pollFirst();
	
	/**
	 * �Ƴ����е����һλԪ��
	 * @return ������Ϊ�գ�����ֵΪnull������Ϊ���Ƴ���Ԫ��
	 */
	E pollLast();
	
	/**
	 * ��ȡ���еĵ�һ��Ԫ�أ�������Ϊ�գ��׳��쳣
	 * @return ���еĵ�һ��Ԫ��
	 */
	E getFirst();
	
	/**
	 * ��ȡ���е����һ��Ԫ�أ�������Ϊ�գ��׳��쳣
	 * @return ���е����һ��Ԫ��
	 */
	E getLast();
	
	/**
	 * ��ȡ���еĵ�һ��Ԫ��
	 * @return ������Ϊ�գ�����ֵΪnull������Ϊ���еĵ�һ��Ԫ��
	 */
	E peekFirst();
	
	/**
	 * ��ȡ���е����һ��Ԫ��
	 * @return ������Ϊ�գ�����ֵΪnull������Ϊ���е����һ��Ԫ��
	 */
	E peekLast();
	
	/**
	 * ɾ���״γ��ֵ�Ŀ��Ԫ��
	 * @param o Ҫɾ����Ԫ��
	 * @return ���Ŀ��Ԫ�ش��ڣ�����true�����򷵻�false
	 */
	boolean removeFirstOccurrence(Object o);
	
	/**
	 * ɾ�������ֵ�Ŀ��Ԫ��
	 * @param o Ҫɾ����Ԫ��
	 * @return ���Ŀ��Ԫ�ش��ڣ�����true�����򷵻�false
	 */
	boolean removeLastOccurrence(Object o);
	
	//  *** ���з���  ***
	
	/**
	 * �����β�����ָ��Ԫ�أ����ռ䲻�㣬���׳��쳣
	 * @param Ҫ��ӵ�Ԫ��
	 * @return ���ɹ���ӣ�����true
	 */
	boolean add(E e);
	
	/**
	 * �����β�����ָ��Ԫ��
	 * @param ���ɹ���ӣ�����true�����򷵻�false
	 */
	boolean offer(E e);
	
	/**
	 * ɾ�����еĵ�һ��Ԫ�أ�������Ϊ�գ��׳��쳣
	 * @return ɾ����Ԫ��
	 */
	E remove();
	
	/**
	 * ɾ�����еĵ�һ��Ԫ��
	 * @return �������Ϊ�գ�����ֵΪnull�����򷵻ر�ɾ����Ԫ��
	 */
	E poll();
	
	/**
	 * ��ȡ���еĵ�һ��Ԫ�أ��������Ϊ�գ��׳��쳣
	 * @return ���еĵ�һ��Ԫ��
	 */
	E element();
	
	/**
	 * ��ȡ���еĵ�һ��Ԫ��
	 * @return ������Ϊ�գ�����ֵΪnull������Ϊ���еĵ�һ��Ԫ��
	 */
	E peek();
	
	//  ***  Stack methods  ***
	
	/**
	 * ��ָ��Ԫ����ӵ����е��׶ˣ����ռ䲻�㣬�׳��쳣
	 * @param e Ҫ��ӵ�Ԫ��
	 */
	void push(E e);
	
	/**
	 * ����һ��Ԫ�ص������У�������Ϊ�գ��׳��쳣
	 * @return ���еĵ�һ��Ԫ��
	 */
	E pop();
	
	// *** Collection methods ***
	
	/**
	 * �Ƴ���һ�γ��ֵ�ָ��Ԫ��
	 * @param o ָ��Ҫ�Ƴ���Ԫ��
	 * @return �������ָ��Ԫ�أ�����true�����򷵻�false
	 */
	boolean remove(Object o);
	
	/**
	 * �ж϶����Ƿ����ָ��Ԫ��
	 * @param ָ��Ԫ��
	 * @return �������ָ��Ԫ�أ�����true�����򷵻�false
	 */
	boolean contains(Object o);
	
	/**
	 * ���ض�����Ԫ�صĸ���
	 * @return ������Ԫ�صĸ���
	 */
	public int size();
	
	/**
	 * ���ض��е���������������ӵ�һ��Ԫ�ص����һ��Ԫ��
	 * @return ���е����������
	 */
	Iterator<E> iterator();
	
	/**
	 * ���ض��еķ�����������������һ��Ԫ�ص���һ��Ԫ��
	 * @return ���еķ��������
	 */
	Iterator<E>  descendingIterator();
	
}
