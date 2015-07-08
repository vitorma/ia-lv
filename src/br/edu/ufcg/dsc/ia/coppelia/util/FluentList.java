package br.edu.ufcg.dsc.ia.coppelia.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class FluentList<T> implements List<T> {
    private List<T> innerList = new LinkedList<T>();

    public FluentList<T> excluding(T element) {
        this.remove(element);
        return this;
    }

    public FluentList<T> excludingAll(Collection<? extends T> c) {
        this.removeAll(c);
        return this;
    }

    public FluentList<T> including(T element) {
        this.innerList.add(element);
        return this;
    }

    public FluentList<T> includingAll(Collection<? extends T> c) {
        this.addAll(c);
        return this;
    }

    /* List interface */

    @Override
    public boolean add(T element) {
        return innerList.add(element);
    }

    @Override
    public void add(int index, T element) {
        innerList.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return innerList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return addAll(index, c);
    }

    @Override
    public void clear() {
        innerList.clear();
    }

    @Override
    public boolean contains(Object o) {
        return innerList.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return innerList.containsAll(c);
    }

    @Override
    public T get(int index) {
        return innerList.get(index);
    }

    @Override
    public int indexOf(Object o) {
        return innerList.indexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return innerList.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return innerList.iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return innerList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return innerList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return innerList.listIterator();
    }

    @Override
    public boolean remove(Object o) {
        return innerList.remove(o);
    }

    @Override
    public T remove(int index) {
        return innerList.remove(index);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return innerList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return innerList.retainAll(c);
    }

    @Override
    public T set(int index, T element) {
        return innerList.set(index, element);
    }

    @Override
    public int size() {
        return innerList.size();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return innerList.subList(fromIndex, toIndex);
    }

    @Override
    public Object[] toArray() {
        return innerList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return innerList.toArray(a);
    }
}