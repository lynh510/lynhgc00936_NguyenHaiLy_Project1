/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Order;

public class OrderBinaryTree {

    BinaryTree<Order> _ccode;
    BinaryTree<Order> _pcode;

    public OrderBinaryTree() {
        _ccode = new BinaryTree<>();
        _pcode = new BinaryTree<>();
    }

    public void insert(Order order) {
        _ccode.insert(order.getCcode().hashCode(), order);
        _pcode.insert(order.getPcode().hashCode(), order);

    }

    public Object[] getCcodeArray() {
        return _ccode.getSortedArray();
    }

    public Object[] getPcodeArray() {
        return _pcode.getSortedArray();
    }

    public boolean checkOrderPcodeExist(long key) {
        return _pcode.findNode(key) != null;
    }

    public boolean checkOrderCcodeExist(long key) {
        return _ccode.findNode(key) != null;
    }

}
