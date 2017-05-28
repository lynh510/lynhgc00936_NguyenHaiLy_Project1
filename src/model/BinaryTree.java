/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

public class BinaryTree<E> {

    public Node<E> root;

//    MyLinkedList<E> _sortedList = new MyLinkedList<>();
    public boolean isEmpty() {
        return root == null;
    }

    private void insert(Node<E> node) {
        if (isEmpty()) {
            root = node;
        } else {
            Node focusNode = root;
            Node parent;
            while (true) {
                parent = focusNode;
                if (node.key < focusNode.key) {
                    focusNode = focusNode.left;
                    if (focusNode == null) {
                        parent.left = node;
                        return;
                    }
                } else {
                    focusNode = focusNode.right;
                    if (focusNode == null) {
                        parent.right = node;
                        return;
                    }
                }
            }
        }
    }

    //insert by key
    public void insert(long key, E data) {
        Node<E> node = new Node<>(key, data);
        insert(node);
    }

    // left, root, right
    public void inOrderTraversal(Node<E> node) {
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.println(node.data.toString());
//            _sortedList.add(focusNode.data);
            inOrderTraversal(node.right);
        }
    }

    //get sorted array by in-order traverse
    public Object[] getSortedArray() {
        MyLinkedList<E> list = new MyLinkedList<>();
        inOrderTraversal(list, root);
        Object[] temp = new Object[list.size()];
        temp = list.toArray();
        return temp;
    }

    public void inOrderTraversal(MyLinkedList<E> list, Node<E> node) {
        if (node != null) {
            inOrderTraversal(list, node.left);
            list.add((E) node.data);
            inOrderTraversal(list, node.right);
        }
    }
    
    public E findNode(long key) {
        Node focusNode = root;
        while (focusNode.key != key) {
            if (key < focusNode.key) {
                focusNode = focusNode.left;
            } else if (key > focusNode.key) {
                focusNode = focusNode.right;
            }
            if (focusNode == null) {
                return null;
            }
        }
        return (E) focusNode.data;
    }

}
