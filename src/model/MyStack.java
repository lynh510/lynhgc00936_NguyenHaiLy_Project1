/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

//LinkedList implementation of Stack 
//Using MyLinkedList
public class MyStack<E> {

    MyLinkedList<E> ml;

    public MyStack() {
        ml = new MyLinkedList();
    }

    public boolean isEmpty() {
        return ml.isEmpty();
    }

    public void push(E e) {
        ml.addToEnd(e);
    }

    public E pop() {
        if (isEmpty()) {
            return null;
        }
        return ml.removeLast();
    }

    public Object[] toArray() {
        return ml.toArray();
    }

    public void clear() {
        ml = new MyLinkedList<>();
    }

    public E top() {
        return ml.get(ml.size() - 1);
    }

    //Advanced implements below:
    //because of original Stack of java.util also have a get by index method
    public E get(int i) {
        return ml.get(i);
    }

    //because of original Stack of java.util also have a size method
    public int size() {
        return ml.size();
    }

    //because of original Stack of java.util also have a remove method
    public E remove(int i) {
        return ml.remove(i);
    }
}
