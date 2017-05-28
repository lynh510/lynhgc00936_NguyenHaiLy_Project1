/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> extends AbstractSequentialList<E> implements Serializable {

    private MyNode<E> head;
    private MyNode<E> tail;
    private int size = 0;

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean add(E e) {
        addToEnd(e);
        return true;
    }

    public void addToBegin(E e) {
        MyNode<E> node = new MyNode<>();
        node.data = e;
        if (this.isEmpty()) {
            head = tail = node;
        } else {
            node.next = head;
            head = node;
        }
        size++;
    }

    public void addToEnd(E e) {
        MyNode<E> node = new MyNode<>();
        node.data = e;
        if (this.isEmpty()) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public void addBefore(E e, MyNode<E> succ) {
        final MyNode<E> pred = findNodeBefore(succ.data);
        final MyNode<E> newNode = new MyNode<>(e, succ);

        MyNode<E> prev = findNodeBefore(succ.data);
        prev = newNode;

        if (pred == null) {
            head = newNode;
        } else {
            pred.next = newNode;
        }
        size++;
    }

    public E removeObject(E e) {
        if (this.isEmpty()) {
            return null;
        }
        MyNode<E> returnNode = null;
        if (size == 1) {
            returnNode = head;
            head = null;
            tail = null;
            size--;
            return returnNode.data;
        }
        MyNode<E> nodeBeforeNodeToDelete = findNodeBefore(e);
        if (nodeBeforeNodeToDelete.data != null) {
            if (tail.data == e) {
                returnNode = nodeBeforeNodeToDelete.next;
                nodeBeforeNodeToDelete.next = null;
                tail = nodeBeforeNodeToDelete;
            } else {
                returnNode = nodeBeforeNodeToDelete.next;
                nodeBeforeNodeToDelete.next = nodeBeforeNodeToDelete.next.next;
            }
            size--;
        } else {
            returnNode = head;
            head = head.next;
            size--;
        }
        return returnNode.data;
    }

    @Override
    public E remove(int i) {
        return removeObject(get(i));
    }

    public E removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        E e = head.data;
        head = head.next;
        size--;
        return e;
    }

    public E removeLast() {
        E e = tail.data;
        if (this.isEmpty()) {
            return null;
        }
        if (size == 1) {
            e = head.data;
            head = null;
            tail = null;
            size--;
            return e;
        }
        MyNode<E> node = findNodeBefore(e);
        e = node.next.data;
        node.next = null;
        tail = node;
        size--;
        return e;
    }

    //if this return empty nodeBeforeNodeToDelete
    //the element want to delete is at the head 
    //return null when no match
    //return Node when found
    private MyNode<E> findNodeBefore(E e) {
        if (head.data == e) {
            return new MyNode();
        }
        MyNode<E> node = head;
        while (node.next != null) {
            if (node.next.data == e) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    public void traverse() {
        MyNode<E> node = head;
        if (head != null) {
            System.out.println(node.data.toString());
            while (node.next != null) {
                node = node.next;
                System.out.println(node.data.toString());
            }
        }
    }

    public E get(int i) {
        rangeCheck(i);
        MyNode<E> node = head;
        int index = 0;
        if (head != null) {
            while (index < i && i < size) {
                node = node.next;
                index++;
            }
        }
        return node.data;
    }

    public MyNode node(int i) {
        rangeCheck(i);
        MyNode<E> node = head;
        int index = 0;
        if (head != null) {
            while (index < i && i < size) {
                node = node.next;
                index++;
            }
        }
        return node;
    }

//this method was copied from ArrayList<E>
//this will return exception when index = size
//for example, when you use for loop...
    private void rangeCheck(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    public Object[] toArray() {
        Object[] o = new Object[size];
        MyNode<E> node = head;
        int index = 0;
        if (head != null) {
            o[index] = node.data;
            while (node.next != null && index < size) {
                index++;
                node = node.next;
                o[index] = node.data;
            }
        }
        return o;
//        ArrayList<E> list = new ArrayList<>();
//        MyNode<E> node = head;
//        while (node != null) {
//            list.add(node.data);
//            node = node.next;
//        }
//        Object[] e = new Object[list.size()];
//        list.toArray(e);
//        return e;
    }

    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        // Write out any hidden serialization magic
        s.defaultWriteObject();

        // Write out size
        s.writeInt(size);

        // Write out all elements in the proper order.
        for (MyNode<E> x = head; x != null; x = x.next) {
            s.writeObject(x.data);
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        // Read in any hidden serialization magic
        s.defaultReadObject();

        // Read in size
        int size = s.readInt();

        // Read in all elements in the proper order.
        for (int i = 0; i < size; i++) {
            addToEnd((E) s.readObject());
        }
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        rangeCheck(index);
        return new ListItr(index);
    }

    private class ListItr implements ListIterator<E> {

        private MyNode<E> lastReturned;
        private MyNode<E> next;
        private int nextIndex;

        ListItr(int index) {
            // assert isPositionIndex(index);
            next = (index == size) ? null : node(index);
            nextIndex = index;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.data;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            lastReturned = next = (next == null) ? tail : findNodeBefore(next.data);
            nextIndex--;
            return lastReturned.data;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            MyNode<E> lastNext = lastReturned.next;
            removeObject(lastReturned.data);
            if (next == lastReturned) {
                next = lastNext;
            } else {
                nextIndex--;
            }
            lastReturned = null;
        }

        @Override
        public void set(E e) {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            lastReturned.data = e;
        }

        @Override
        public void add(E e) {
            lastReturned = null;
            if (next == null) {
                addToEnd(e);
            } else {
                addBefore(e, next);
            }
            nextIndex++;
        }

    }

}

class MyNode<E> implements Comparable<E>, Serializable {

    public MyNode() {
    }

    public E getData() {
        return data;
    }

    E data;
    MyNode<E> next;

    //Default Node constructor
    public MyNode(E data) {
        this.data = data;
        next = null;
    }

    public MyNode(E data, MyNode<E> next) {
        this.data = data;
        next = next;
    }

    @Override
    public int compareTo(E data) {
        //To change body of generated methods, choose Tools | Templates.
//      throw new UnsupportedOperationException("Not supported yet.");
        if (data == this.data) {
            return 0;
        } else {
            return 1;
        }
    }

}
