package io.github.seal139.jSwarm.datatype;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface NativeList<E extends Number> extends Iterable<E>, RandomAccess, Serializable {

	/**
     * Get native off-heap pointer address. This value is not static and may change upon resize or reset
     * @return Off-heap pointer address
     */
	long getNativeAddress();
    
	/**
	 * Set the element at the specified position in this list
	 * @param index Position index
	 * @param value Value
	 */
    void set(long index, E value);
    
    /**
     * Returns the element at the specified position in this list.
     * @param index position index
     * @return Element value
     */
    E get(long index);
    
    /**
     * Return the first element in this list
     * @return Element value
     */
    default E getFirst() { return get(0); }

    /**
     * Return the last element in this list
     * @return Element value
     */
    default E getLast() { return get(size() - 1); }
    
    /**
     * Returns the number of elements in this list.
     * @return Number of elements
     */
    long size();
    
    /**
     * Returns true if this collection contains no elements.
     * @return true if list is empty. otherwise false
     */
    default boolean isEmpty() { return size() == 0L; }
    
    /**
     * Return the number maximum element based on allocated memory in this list 
     * @return Number of maximum element
     */
    long capacity();

    /**
     * Inserts the specified element at the last position in this list
     * 
     * @param arg0 Value
     * @return true when successfully inserted
     */
    boolean add(E arg0);
    
    /**
     * Bulk insert elements at the last position in this list
     * @param c elements to add
     * @return true if successfully added. Otherwise, false
     */
    boolean addAll(Collection<E> c);
    
    /**
     * Bulk insert elements at the last position in this list
     * @param c elements to add
     * @return true if successfully added. Otherwise, false
     */
    boolean addAll(NativeList<E> c);
    
    /**
     * Bulk insert elements at the last position in this list
     * @param c elements to add
     * @return true if successfully added. Otherwise, false
     */
    boolean addAll(@SuppressWarnings("unchecked") E... c);
    
    /**
     * Remove element at specified index
     * @param index Element index to remove
     * @return true if successfully removed. Otherwise, false
     */
    boolean remove(long index);
    
    /**
     * Find element that matches the input parameter and remove it from list. 
     * This function only remove the first element found by evaluating equality.
     * Please note that the removed element is based on search algorithm used by the implementation 
     * and is not dictated to be the first, the last, or any specific order
     * @param o Value to be removed from list
     * @return true if successfully removed. Otherwise, false
     */
    boolean remove(E o);
    
    /**
     * remove the first element from list
     * @return true if successfully removed. Otherwise, false
     */
    boolean removeFirst();
    
    /**
     * remove the last element from list
     * @return true if successfully removed. Otherwise, false
     */
    boolean removeLast();
    
    /**
     * Remove all elements specified on parameter
     * @param c elements to be remove
     * @return true if successfully removed. Otherwise, false
     */
    boolean removeAll(Collection<E> c);
    
    /**
     * Remove all elements specified on parameter
     * @param c elements to be remove
     * @return true if successfully removed. Otherwise, false
     */
    boolean removeAll(NativeList<E> c);

    /**
     * Remove all elements specified on parameter
     * @param c elements to be remove
     * @return true if successfully removed. Otherwise, false
     */
    boolean removeAll(@SuppressWarnings("unchecked") E... c);

    /**
     * Remove all elements but specified on parameter
     * @param c elements to be retained
     * @return true if successfully removed. Otherwise, false
     */
    boolean retainAll(Collection<E> c);
    
    /**
     * Remove all elements but specified on parameter
     * @param c elements to be retained
     * @return true if successfully removed. Otherwise, false
     */
    boolean retainAll(NativeList<E> c);
    
    /**
     * Remove all elements but specified on parameter
     * @param c elements to be retained
     * @return true if successfully removed. Otherwise, false
     */
    boolean retainAll(@SuppressWarnings("unchecked") E... c);
    
    /**
     * Remove all elements and reset back capacity to initial size
     */
    void clear();

    /**
     * Find the first element in the list. Please note that this function is for interopability with {@link java.util.List}
     * @param o Element to find
     * @return index of element found
     */
    long indexOf(Object o);
    
    /**
     * Find the first element in the list
     * @param o Element to find
     * @return index of element found
     */
    long indexOf(E o);

    /**
     * Find the last element in the list. Please note that this function is for interopability with {@link java.util.List}
     * @param o Element to find
     * @return index of element found
     */
    long lastIndexOf(Object o);
    
    /**
     * Find the last element in the list.
     * @param o Element to find
     * @return index of element found
     */
    long lastIndexOf(E o);

    /**
     * Determine if this list is contains specified element
     * @param o Element to find
     * @return true if contains. Otherwise false
     */
    boolean contains(E o) ;
    
    /**
     * Determine if this list is contains specified element. Please note that this function is for interopability with {@link java.util.List}
     * @param o Element to find
     * @return true if contains. Otherwise false
     */
    boolean contains(Object o) ;
    
    /**
     * Determine if list contains all elements specified by parameter
     * @param c specified elements
     * @return true if this list contains all elements. Otherwise false
     */
    boolean containsAll(Collection<?> c);
    
    /**
     * Determine if list contains all elements specified by parameter
     * @param c specified elements
     * @return true if this list contains all elements. Otherwise false
     */
    boolean containsAll(NativeList<E> c);
    
    /**
     * Determine if list contains all elements specified by parameter
     * @param c specified elements
     * @return true if this list contains all elements. Otherwise false
     */
    boolean containsAll(@SuppressWarnings("unchecked") E... c);
    

    /**
     * Wrap value for java standard {@link Collection} or {@link java.util.List} interopability. 
     * This function is guaranteed to be zero-copy.
     * Because this List is based on long indexer, wrap into {@link java.util.List} of {@link java.util.List}
     * @return A readonly {@link java.util.List} of {@link java.util.List} containing elements
     */
    List<List<E>> asList();

    /**
     * Get sublist projection from specified index (inclusive) to specified index (exclusive)
     * @param fromIndex From (inclusive)
     * @param toIndex To (Exclusive)
     * @return Projection {@link NativeList}
     */
    NativeList<E> subList(long fromIndex, long toIndex);
    
    /**
     * Returns a sequential Stream with this collection as its source.
     * @return Stream
     */
    Stream<E> stream();

    /**
     * Returns a possibly parallel Stream with this collection as its source.
     * @return Possibly parallel Stream
     */
    Stream<E> parallelStream();

    @Override
    Iterator<E> iterator();
    
    @Override
    void forEach(Consumer<? super E> action);

    @Override
    Spliterator<E> spliterator();
}
