/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parse;

import entity.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entity.Customer;
import entity.Order;
import java.io.BufferedReader;
import model.MyLinkedList;
import model.MyStack;

public class JsonParser {

    private final Gson parser = new GsonBuilder().setPrettyPrinting().create();

    public String class2Json(Object o) {
        return parser.toJson(o, o.getClass());
    }

    public MyLinkedList<Product> json2P(BufferedReader br) {
        MyLinkedList<Product> mp = parser.fromJson(br, new TypeToken<MyLinkedList<Product>>() {}.getType());
        //this cast method of Gson having error, you can't add more data to this list
        //i created new method to get elements again and fix that error
        //it will take more time...
        MyLinkedList<Product> _mp = new MyLinkedList<>();
        for (int i = 0; i < mp.size(); i++) {
            _mp.add(mp.get(i));
        }
        return _mp;
    }

    public MyStack<Customer> json2C(BufferedReader br) {
        MyStack<Customer> mc = parser.fromJson(br, new TypeToken<MyStack<Customer>>() {}.getType());
        MyStack<Customer> _mc = new MyStack<>();
        for (int i = 0; i < mc.size(); i++) {
            _mc.push(mc.get(i));
        }
        return _mc;
    }

    public MyLinkedList<Order> json2O(BufferedReader br) {
        return parser.fromJson(br, new TypeToken<MyLinkedList<Order>>() {}.getType());
    }
}
