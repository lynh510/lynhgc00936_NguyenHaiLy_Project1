/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

public class Node<E> {

    long key;
    public E data;
    public Node<E> left;
    public Node<E> right;

    public Node(long key, E data) {
        this.key = key;
        this.data = data;
    }

    @Override
    public String toString() {
        return String.valueOf(key);
    }
}
