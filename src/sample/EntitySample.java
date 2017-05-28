/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import entity.Customer;
import entity.Order;
import entity.Product;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import app.Main;
import model.BinaryTree;
import model.MyLinkedList;
import model.MyStack;
import model.Node;
import model.OrderBinaryTree;
import parse.JsonParser;

public class EntitySample {
	public String filePath = new File("").getAbsolutePath();
    public MyLinkedList<Product> productListSample() {
        MyLinkedList<Product> ml = new MyLinkedList();
        
        try {
        	JsonParser jp = new JsonParser();
            BufferedReader br = new BufferedReader(new FileReader(filePath + "/src/temp/Product.json"));
        	ml = jp.json2P(br);
               
        } catch (FileNotFoundException | NullPointerException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //ML ML ML
        return ml;

    }

    public MyStack<Customer> customerListSample() {
        MyStack<Customer> cl = new MyStack<>();
        try {
        	JsonParser jp = new JsonParser();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath + "/src/temp/Customer.json"), "UTF8"));
        	cl = jp.json2C(br);
               
        } catch (FileNotFoundException | NullPointerException | UnsupportedEncodingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //CL CL CL
        return cl;
    }

    public MyLinkedList<Order> orderListSample() {
        OrderBinaryTree bt = new OrderBinaryTree();
        MyLinkedList<Order> mo = new MyLinkedList<>();
        try {
        	JsonParser jp = new JsonParser();
            BufferedReader br = new BufferedReader(new FileReader(filePath + "/src/temp/Order.json"));
        	mo = jp.json2O(br);
               
        } catch (FileNotFoundException | NullPointerException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mo;
    }

}
