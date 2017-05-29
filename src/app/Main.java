/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import entity.Customer;
import entity.Order;
import entity.Product;
import model.MyLinkedList;
import model.MyStack;
import model.OrderBinaryTree;
import model.TableHeaderRenderer;
import parse.FileProcess;
import parse.JsonParser;
import sample.EntitySample;
import util.QuickSort;
import util.TableBuilder;

public final class Main extends JFrame {

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        this.setLocationRelativeTo(null);
        backDataBtn.setVisible(false);
        initSampleData();
        initProductTable();
        initCustomerTable();
        initOrderTable();
    }

    private final EntitySample es = new EntitySample();
    private final JsonParser jp = new JsonParser();

    public void initSampleData() {
        listProduct = listProducttemp = es.productListSample();
        listCustomer = listCustomertemp = es.customerListSample();
        listOrder = es.orderListSample();
        /*obt = new OrderBinaryTree();
        for (int i = 0; i < listOrder.size(); i++) {
            obt.insert(listOrder.get(i));
        }
        orders = obt.getPcodeArray();*/
    }
    
    public void openTableData() {
        String fileName;
        String[] extensions = {"json"};
        fileName = FileProcess.getFileName(".", "Json Data Files", extensions);
        if (fileName != null) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileName));
                if (tableState == 0) {
                    listProduct = jp.json2P(br);
                    initProductTable();
                } else if (tableState == 1) {
                    listCustomer = jp.json2C(br);
                    initCustomerTable();
                } else if (tableState == 2) {

                }
            } catch (FileNotFoundException | NullPointerException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void saveData() {
    	String filepath = System.getProperty("user.dir");
    	FileWriter fw = null;
        String contents = "";
        try {
            
            if (tableState == 0) {
            	fw = new FileWriter(filepath + "/src/temp/Product.json");
                contents = jp.class2Json(listProduct);
            } else if (tableState == 1) {
            	fw = new FileWriter(filepath + "/src/temp/Customer.json");
                contents = jp.class2Json(listCustomer);
            } else if (tableState == 2) {
            	fw = new FileWriter(filepath + "/src/temp/Order.json");
            	contents = jp.class2Json(listOrder);
            }
            fw.write(contents);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void saveTableData() {
        String fileName;
        String[] extensions = {"json"};
        fileName = FileProcess.getFileName(".", "Json Data Files", extensions, 1);
        if (fileName != null) {
            if (!fileName.endsWith(".json")) {
                fileName = fileName + ".json";
            }
            FileWriter fw = null;
            String contents = "";
            try {
                fw = new FileWriter(fileName);
                if (tableState == 0) {
                    contents = jp.class2Json(listProduct);
                } else if (tableState == 1) {
                    contents = jp.class2Json(listCustomer);
                } else if (tableState == 2) {

                }
                fw.write(contents);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fw.close();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

  //INIT TABLE PRODUCT
    @SuppressWarnings({ "serial", "unchecked" })
	public void initProductTable() {
        String columns[] = { "Select", "No.", "Product code", "Product name", "Quantity", "Saled", "Price"};
        //define the type of row header
        DefaultTableModel dmp = new DefaultTableModel(columns, 0) {
            @Override
            public Class getColumnClass(int col) {
                switch (col) {
                    case 0:
                    	return Boolean.class;
                	case 1:
                		return Integer.class;
                    case 2:
                        return String.class;
                    case 3:
                        return String.class;
                    case 4:
                        return Integer.class;
                    case 5:
                        return Integer.class;
                    case 6:
                        return Double.class;
                    default:
                        return Object.class;
                }
            }
        };
        
        //convert MyLinkedList<Product> to Array to sort by QuickSort
        Object[] arrProduct = listProduct.toArray();
        //sort Array Product by QuickSort
        qs.sort(arrProduct);
        
        //set up data to model
        int j = 1;
        for (Object obj : arrProduct) {
            Product pro = (Product) obj;
            int id = j++;
            String pcode = pro.getPcode();
            String pname = pro.getPro_name();
            int quantity = pro.getQuantity();
            int saled = pro.getSaled();
            double price = pro.getPrice();
            Object[] data = { false, id, pcode, pname, quantity, saled, price };
            dmp.addRow(data);
        }
        final int rows = 10;
        //set model for table
        pTable.setModel(dmp);
        
        TableColumnModel model = pTable.getColumnModel();
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(SwingConstants.LEFT);
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        
        int[] columnWidth = {50, 50, 100, 150, 100, 100, 100}; //default size for header
        for (int i = 0; i < columns.length; i++) {
            model.getColumn(i).setHeaderRenderer(new TableHeaderRenderer(pTable));
            model.getColumn(i).setPreferredWidth(columnWidth[i]);
            //SET ALIGNMENT FOR EACH COLUMN
            if(i == 1 || i == 2) {
            	model.getColumn(i).setCellRenderer(center);
            }
            if(i == 3) {
            	model.getColumn(i).setCellRenderer(left);
            }
            if(i == 6) {
            	model.getColumn(i).setCellRenderer(right);
            }
        }
        //SET ROW HEIGHT FOR TABLE
        pTable.setRowHeight(25);
        //AUTO SORT WHEN CLICK ON TABLE HEADER
        pTable.setAutoCreateRowSorter(true);
        //DISABLE SELECT ON TABLE
        pTable.setRowSelectionAllowed(false);
        //DISABLE DRAG AND DROP COLUMNS TABLE
        pTable.getTableHeader().setReorderingAllowed(false);
    }

    public void initCustomerTable() {
        String col[] = {"Select", "No.", "Customer Code" , "Customer Name", "Phone Number"};
        DefaultTableModel dmc = new DefaultTableModel(col, 0) {
            @Override
            public Class getColumnClass(int col) {
                switch (col) {
                    case 0:
                        return Boolean.class;
                    case 1:
                        return Integer.class;
                    case 2:
                        return String.class;
                    case 3:
                        return String.class;
                    case 4:
                        return String.class;
                    default:
                        return Object.class;
                }
            }
        };
        
        //convert MyStack<Customer> to Array to sort by QuickSort
        Object[] cArray = listCustomer.toArray();
        //sort Array Customer by QuickSort
        qs.sort(cArray);
        int j = 1;
        for (Object cArray1 : cArray) {
            Customer temp = (Customer) cArray1;
            int id = j++;
            String ccode = temp.getCcode();
            String cname = temp.getCus_name();
            String cnumber = temp.getPhone();
            Object[] data = { false, id, ccode, cname, cnumber };
            dmc.addRow(data);
        }
        cTable.setModel(dmc);
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        
        TableColumnModel model = cTable.getColumnModel();
        int[] columnWidth = {50, 50, 100, 200, 150};
        for (int i = 0; i < col.length; i++) {
            model.getColumn(i).setHeaderRenderer(new TableHeaderRenderer(cTable));
            model.getColumn(i).setPreferredWidth(columnWidth[i]);
            if(i == 1 || i == 2) {
            	model.getColumn(i).setCellRenderer(center);
            }
        }
        cTable.setRowHeight(25);
        cTable.setAutoCreateRowSorter(true);
        cTable.setRowSelectionAllowed(false);
        cTable.getTableHeader().setReorderingAllowed(false);
    }

    public void initOrderTable() {
        String col[] = { "Product Code", "Customer Code", "Quantity"};
        DefaultTableModel dmo = new DefaultTableModel(col, 0) {
            @Override
            public Class getColumnClass(int col) {
                switch (col) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    case 2:
                        return Integer.class;
                    default:
                        return Object.class;
                }
            }
        };
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        
        TableColumnModel model = oTable.getColumnModel();
        for (int i = 0; i < col.length; i++) {
            model.getColumn(i).setHeaderRenderer(new TableHeaderRenderer(oTable));
            model.getColumn(i).setCellRenderer(center);
        }

        Object[] ord = listOrder.toArray();
        
        for (int i = 0; i < ord.length; i++) {
            Order temp = (Order) ord[i];
            String pcode = temp.getPcode();
            String ccode = temp.getCcode();
            int quantity = temp.getQuantity();
            Object[] data = {pcode, ccode, quantity};
            dmo.addRow(data);
        }
        oTable.setModel(dmo);
        oTable.setRowHeight(25);
        oTable.getTableHeader().setReorderingAllowed(false);
    }

    

    private void pDelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pDelBtnActionPerformed
    	boolean cantDelete = false;
        MyStack<String> _pCodeListToDelete = new MyStack<>();
        if (pTable.getColumnCount() < 7) {
            System.err.println("Product Data Not Found!");
            return;
        }
        System.out.println(pTable.getRowCount());
        for (int i = 0; i < pTable.getRowCount(); i++) {
            Boolean checked = Boolean.valueOf(pTable.getValueAt(i, 0).toString());
            if (checked) {
                _pCodeListToDelete.push(pTable.getValueAt(i, 2).toString());
            }
        }

        for (int i = 0; i < _pCodeListToDelete.size(); i++) {
            cantDelete = obt.checkOrderPcodeExist(_pCodeListToDelete.get(i).hashCode());
            System.out.println(cantDelete);
        }

        if (!cantDelete) {
            while (!_pCodeListToDelete.isEmpty()) {
                String temp = _pCodeListToDelete.pop();
                for (int i = 0; i < listProduct.size(); i++) {
                    System.out.println(listProduct.get(i).getPcode());
                    if (temp.equals(listProduct.get(i).getPcode())) {
                    	listProduct.remove(listProduct.get(i));
                        System.out.println("Deleted: " + temp);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "An error occured! \nThere are a few products present in the ordering list!");
        }
        System.out.println("Remain product(s): " + listProduct.size());
        initProductTable();
    }//GEN-LAST:event_pDelBtnActionPerformed

    private void txtSearchStringMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchStringMouseEntered
        txtSearchString.setFocusable(true);
    }//GEN-LAST:event_txtSearchStringMouseEntered

    private void oSortBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oSortBtnActionPerformed
        JButton button = (JButton) evt.getSource();
        productSortPopup.show(button, 0, button.getHeight());
    }//GEN-LAST:event_oSortBtnActionPerformed

    private void menuSortPcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSortPcodeActionPerformed
        resetOrderTree();
        orders = obt.getPcodeArray();
        initOrderTable();
    }//GEN-LAST:event_menuSortPcodeActionPerformed

    private void menuSortCCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSortCCodeActionPerformed
        resetOrderTree();
        orders = obt.getCcodeArray();
        initOrderTable();
    }//GEN-LAST:event_menuSortCCodeActionPerformed

    private void oTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_oTableMouseClicked
        int amount = 0;
        String orderedProduct = "";
        double payment = 0.0;

        int row = oTable.rowAtPoint(evt.getPoint());
        int col = oTable.columnAtPoint(evt.getPoint());

        TableBuilder tb = new TableBuilder();
        tb.addRow("------------", "------------", "--------", "--------");
        tb.addRow("Product Code", "Product Name", "Quantity", "Price($)");
        tb.addRow("------------", "------------", "--------", "--------");
        try {
            String cusCode = oTable.getValueAt(row, 1).toString();
            String cusName = "ORDER LIST \n\nCustomer Name: " + getCustomerName(cusCode) + " - " + cusCode;
            for (int i = 0; i < oTable.getRowCount(); i++) {
                if (oTable.getValueAt(i, 1).equals(cusCode)) {
                    int quantity = Integer.parseInt(oTable.getValueAt(i, 2).toString());
                    String product = oTable.getValueAt(i, 0).toString();
                    double price = calculatePaymentForEachItem(product, quantity);
                    tb.addRow(product, getProductName(product), String.valueOf(quantity), String.valueOf(price));
                    amount = amount + quantity;
                    payment = payment + price;
                }
            }
            tb.addRow("------------", "------------", "--------", "--------");
            tb.addRow("Total:", " ", String.valueOf(amount), String.valueOf(payment));
            txtDisplayOrderTotal.setText(cusName + "\n\n" + tb.toString());
            txtDisplayOrderTotal.setCaretPosition(0);
        } catch (NullPointerException e) {
            System.err.println("Can't retrieve data! Make sure Products and Customers data are opened.");
        }
    }//GEN-LAST:event_oTableMouseClicked

    public void resetOrderTree() {
        obt = new OrderBinaryTree();
        /*for (int i = 0; i < listOrder.size(); i++) {
            obt.insert(listOrder.get(i));
        }*/
        for (Order order : listOrder) {
        	obt.insert(order);
		}
    }

    public double calculatePaymentForEachItem(String pcode, int quantity) {
        double price = 0.0;
        for (int i = 0; i < listProduct.size(); i++) {
            if (listProduct.get(i).getPcode().equals(pcode)) {
                price = quantity * listProduct.get(i).getPrice();
            }
        }
        return price;
    }

    public String getCustomerName(String ccode) {
        for (int i = 0; i < listCustomer.size(); i++) {
            if (listCustomer.get(i).getCcode().equals(ccode)) {
                return listCustomer.get(i).getCus_name();
            }
        }
        return null;
    }

    public String getProductName(String pcode) {
        for (int i = 0; i < listProduct.size(); i++) {
            if (listProduct.get(i).getPcode().equals(pcode)) {
                return listProduct.get(i).getPro_name();
            }
        }
        return null;
    }

    public void changeProductQuantityInStock(String pcode, int sale) {
        for (int i = 0; i < listProduct.size(); i++) {
            if (listProduct.get(i).getPcode().equals(pcode)) {
                Product temp = listProduct.get(i);
                temp.setQuantity(temp.getQuantity() - sale);
                temp.setSaled(temp.getSaled() + sale);
                break;
            }
        }
        initProductTable();
    }

    private void saveBtnToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnToolActionPerformed
    	saveData();
        System.out.println("Save!");
    }//GEN-LAST:event_saveBtnToolActionPerformed

    private void mainTabPaneStateChanged(ChangeEvent evt) {//GEN-FIRST:event_mainTabPaneStateChanged
        JTabbedPane sourceTabbedPane = (JTabbedPane) evt.getSource();
        int index = sourceTabbedPane.getSelectedIndex();
        System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
        if (index == 0) {
            tableState = index;
            saveBtnTool.setEnabled(true);
            saveAsBtnTool.setEnabled(true);
            txtSearchString.setEnabled(true);
            searchBtnTool.setEnabled(true);
            backDataBtn.setEnabled(true);
        } else if (index == 1) {
            tableState = index;
            saveBtnTool.setEnabled(true);
            saveAsBtnTool.setEnabled(true);
            txtSearchString.setEnabled(true);
            searchBtnTool.setEnabled(true);
            backDataBtn.setEnabled(true);
        } else if (index == 2) {
            tableState = index;
            saveBtnTool.setEnabled(false);
            saveAsBtnTool.setEnabled(false);
            txtSearchString.setEnabled(false);
            searchBtnTool.setEnabled(false);
            backDataBtn.setEnabled(false);
        }
    }//GEN-LAST:event_mainTabPaneStateChanged

    private void cDelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cDelBtnActionPerformed
        boolean cantDelete = false;
        if (cTable.getColumnCount() < 5) {
            System.err.println("Customer Data Not Found!");
            return;
        }
        ArrayList<String> _cCodeListToDelete = new ArrayList<>();
        for (int i = 0; i < cTable.getRowCount(); i++) {
            Boolean checked = Boolean.valueOf(cTable.getValueAt(i, 0).toString());
            if (checked) {
                _cCodeListToDelete.add(cTable.getValueAt(i, 2).toString());
            }
        }
        for (int i = 0; i < _cCodeListToDelete.size(); i++) {
            cantDelete = obt.checkOrderCcodeExist(_cCodeListToDelete.get(i).hashCode());
            System.out.println(cantDelete);
        }
        if (!cantDelete) {
            for (int i = 0; i < _cCodeListToDelete.size(); i++) {
                for (int j = 0; j < listCustomer.size(); j++) {
                    if (_cCodeListToDelete.get(i).equals(listCustomer.get(j).getCcode())) {
                        System.out.println("Deleted: " + _cCodeListToDelete.get(i));
                        listCustomer.remove(j);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "An error occured! \nThere are a few customers present in the ordering list!");
        }
        System.out.println("Remain customer(s): " + listCustomer.size());
        initCustomerTable();
    }//GEN-LAST:event_cDelBtnActionPerformed
    
    private void openBtnToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openBtnToolActionPerformed
        openTableData();
    }//GEN-LAST:event_openBtnToolActionPerformed

    private void pAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pAddBtnActionPerformed
        if (listProduct != null) {
            txtProductCode.setText("");
            txtProductName.setText("");
            txtProductQuantity.setValue(0);
            txtProductPrice.setText("");
            addProductFrame.show();
        }
    }//GEN-LAST:event_pAddBtnActionPerformed

    private void addProductCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductCancelBtnActionPerformed
        addProductFrame.dispose();
    }//GEN-LAST:event_addProductCancelBtnActionPerformed

    private void addProductConfirmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductConfirmBtnActionPerformed
        String pcode = txtProductCode.getText();
        String pname = txtProductName.getText();
        int qty = (Integer) txtProductQuantity.getValue();
        float pprice = 0f;
        try {
            pprice = Float.valueOf(txtProductPrice.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Price must be a number!");
            return;
        }
        if (pcode != null && pname != null && qty > 0 && pprice > 0) {
            listProduct.add(new Product(pcode, pname, qty, 0, pprice));
            initProductTable();
            addProductFrame.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "You have to fill all information!");
        }
    }//GEN-LAST:event_addProductConfirmBtnActionPerformed

    private void saveAsBtnToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsBtnToolActionPerformed
        saveTableData();
    }//GEN-LAST:event_saveAsBtnToolActionPerformed

    private void cAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cAddBtnActionPerformed
        if (listCustomer != null) {
            addCustomerFrame.show();
        }
    }//GEN-LAST:event_cAddBtnActionPerformed

    private void addCustomerConfirmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerConfirmBtnActionPerformed
        // TODO add your handling code here:
        String ccode = txtCCode.getText();
        String cname = txtCName.getText();
        String cnum = txtCNumber.getText();
        if (ccode != null && cname != null && cnum != null) {
            listCustomer.push(new Customer(ccode, cname, cnum));
            initCustomerTable();
            addCustomerFrame.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "You have to fill all information!");
        }
    }//GEN-LAST:event_addCustomerConfirmBtnActionPerformed

    private void oAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oAddBtnActionPerformed
        // TODO add your handling code here:
        ArrayList<String> cname = new ArrayList<>();
        ArrayList<String> pname = new ArrayList<>();
        if (orders != null && listProduct != null && listCustomer != null) {
            for (int i = 0; i < listProduct.size(); i++) {
                pname.add(listProduct.get(i).getPro_name());
            }
            for (int i = 0; i < listCustomer.size(); i++) {
                cname.add(listCustomer.get(i).getCus_name());
            }
            comboBoxCusName.setModel(new DefaultComboBoxModel(cname.toArray()));
            comboBoxProName.setModel(new DefaultComboBoxModel(pname.toArray()));

            for (int i = 0; i < listProduct.size(); i++) {
                if (comboBoxProName.getSelectedItem().toString().equals(listProduct.get(i).getPro_name())) {
                    int max = listProduct.get(i).getQuantity() - listProduct.get(i).getSaled();
                    maxProLabel.setText(String.valueOf(max));
                    SpinnerModel sm = new SpinnerNumberModel(0, 0, max, 1);
                    txtOrderProNumber.setModel(sm);
                }
            }
            addOrderFrame.show();
        }
    }//GEN-LAST:event_oAddBtnActionPerformed

    private void comboBoxProNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxProNameItemStateChanged
        // TODO add your handling code here:
        for (int i = 0; i < listProduct.size(); i++) {
            if (comboBoxProName.getSelectedItem().toString().equals(listProduct.get(i).getPro_name())) {
                int max = listProduct.get(i).getQuantity() - listProduct.get(i).getSaled();
                maxProLabel.setText(String.valueOf(max));
                SpinnerModel sm = new SpinnerNumberModel(0, 0, max, 1);
                txtOrderProNumber.setModel(sm);
            }
        }
    }//GEN-LAST:event_comboBoxProNameItemStateChanged

    private void oAddCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oAddCancelActionPerformed
        // TODO add your handling code here:
        addOrderFrame.dispose();
    }//GEN-LAST:event_oAddCancelActionPerformed

    private void oAddConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oAddConfirmActionPerformed
        // TODO add your handling code here:
        String pcode = "";
        String ccode = "";
        int num = 0;
        for (int i = 0; i < listProduct.size(); i++) {
            if (comboBoxProName.getSelectedItem().toString().equals(listProduct.get(i).getPro_name())) {
                pcode = listProduct.get(i).getPcode();
            }
        }
        for (int i = 0; i < listCustomer.size(); i++) {
            if (comboBoxCusName.getSelectedItem().toString().equals(listCustomer.get(i).getCus_name())) {
                ccode = listCustomer.get(i).getCcode();
            }
        }
        num = (Integer) txtOrderProNumber.getValue();

        listOrder.add(new Order(pcode, ccode, num));
        resetOrderTree();
        changeProductQuantityInStock(pcode, num);
        orders = obt.getCcodeArray();

        initOrderTable();
        addOrderFrame.dispose();
    }//GEN-LAST:event_oAddConfirmActionPerformed

    private void searchBtnToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnToolActionPerformed
        // TODO add your handling code here:
        MyLinkedList<Product> psearch = new MyLinkedList<>();
        MyStack<Customer> csearch = new MyStack<>();
        String searchString = txtSearchString.getText();
        if (tableState == 0) {
            for (int i = 0; i < listProduct.size(); i++) {
                if (listProduct.get(i).getPcode().contains(searchString)) {
                    psearch.add(listProduct.get(i));
                }
            }
            listProduct = psearch;
            initProductTable();
            backDataBtn.setVisible(true);
        } else if (tableState == 1) {
            for (int i = 0; i < listCustomer.size(); i++) {
                if (listCustomer.get(i).getCcode().contains(searchString)) {
                    csearch.push(listCustomer.get(i));
                }
            }
            listCustomer = csearch;
            initCustomerTable();
            backDataBtn.setVisible(true);
        }
    }//GEN-LAST:event_searchBtnToolActionPerformed

    private void backDataBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backDataBtnActionPerformed
        // TODO add your handling code here:
        if (tableState == 0) {
            listProduct = listProducttemp;
            initProductTable();
        } else if (tableState == 1) {
            listCustomer = listCustomertemp;
            initCustomerTable();
        }
        backDataBtn.setVisible(false);
    }//GEN-LAST:event_backDataBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
//            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        productSortPopup = new javax.swing.JPopupMenu();
        menuSortPcode = new javax.swing.JMenuItem();
        menuSortCCode = new javax.swing.JMenuItem();
        addProductFrame = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtProductName = new javax.swing.JTextField();
        txtProductCode = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtProductQuantity = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        txtProductPrice = new javax.swing.JTextField();
        addProductConfirmBtn = new javax.swing.JButton();
        addProductCancelBtn = new javax.swing.JButton();
        addCustomerFrame = new javax.swing.JFrame();
        jLabel5 = new javax.swing.JLabel();
        txtCCode = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCName = new javax.swing.JTextField();
        addCustomerConfirmBtn = new javax.swing.JButton();
        addCustomerCancelBtn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtCNumber = new javax.swing.JTextField();
        addOrderFrame = new javax.swing.JFrame();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        comboBoxCusName = new javax.swing.JComboBox();
        comboBoxProName = new javax.swing.JComboBox();
        txtOrderProNumber = new javax.swing.JSpinner();
        oAddConfirm = new javax.swing.JButton();
        oAddCancel = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        maxProLabel = new javax.swing.JLabel();
        mainTabPane = new javax.swing.JTabbedPane();
        productTablePane = new javax.swing.JPanel();
        pAddBtn = new javax.swing.JButton();
        pDelBtn = new javax.swing.JButton();
        pTableScrollPane = new javax.swing.JScrollPane();
        pTable = new javax.swing.JTable();
        customerTablePane = new javax.swing.JPanel();
        cTableScollPane = new javax.swing.JScrollPane();
        cTable = new javax.swing.JTable();
        cAddBtn = new javax.swing.JButton();
        cDelBtn = new javax.swing.JButton();
        orderTablePane = new javax.swing.JPanel();
        oSortBtn = new javax.swing.JButton();
        oAddBtn = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        displayOrderScrollPane = new javax.swing.JScrollPane();
        txtDisplayOrderTotal = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        oTable = new javax.swing.JTable();
        mainToolBar = new javax.swing.JToolBar();
        openBtnTool = new javax.swing.JButton();
        saveBtnTool = new javax.swing.JButton();
        saveAsBtnTool = new javax.swing.JButton();
        searchPanel = new javax.swing.JPanel();
        txtSearchString = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        searchBtnTool = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        backDataBtn = new javax.swing.JButton();

        menuSortPcode.setText("By Product Code");
        menuSortPcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSortPcodeActionPerformed(evt);
            }
        });
        productSortPopup.add(menuSortPcode);

        menuSortCCode.setText("By Customer Code");
        menuSortCCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSortCCodeActionPerformed(evt);
            }
        });
        productSortPopup.add(menuSortCCode);

        addProductFrame.setTitle("Add New Product");
        addProductFrame.setMinimumSize(new java.awt.Dimension(500, 220));
        addProductFrame.setLocationRelativeTo(null);

        jLabel1.setText("Product Name:");

        jLabel2.setText("Product Code:");

        jLabel3.setText("Quantity:");

        jLabel4.setText("Price: ");

        addProductConfirmBtn.setText("OK");
        addProductConfirmBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductConfirmBtnActionPerformed(evt);
            }
        });

        addProductCancelBtn.setText("Cancel");
        addProductCancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductCancelBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addProductFrameLayout = new javax.swing.GroupLayout(addProductFrame.getContentPane());
        addProductFrame.getContentPane().setLayout(addProductFrameLayout);
        addProductFrameLayout.setHorizontalGroup(
            addProductFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addProductFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addProductFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addProductFrameLayout.createSequentialGroup()
                        .addGroup(addProductFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(addProductFrameLayout.createSequentialGroup()
                                .addGroup(addProductFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3))
                                .addGap(8, 8, 8)
                                .addGroup(addProductFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtProductPrice, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtProductQuantity, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                    .addComponent(txtProductName, javax.swing.GroupLayout.Alignment.LEADING)))
                            .addGroup(addProductFrameLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtProductCode)))
                        .addGap(55, 55, 55)
                        .addGroup(addProductFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addProductConfirmBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addProductCancelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))
                    .addComponent(jLabel4))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        addProductFrameLayout.setVerticalGroup(
            addProductFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addProductFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addProductFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addProductConfirmBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addProductFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addProductCancelBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addProductFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtProductQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addProductFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtProductPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        addCustomerFrame.setMinimumSize(new java.awt.Dimension(500, 200));
        addCustomerFrame.setLocationRelativeTo(null);

        jLabel5.setText("Customer Code:");

        jLabel6.setText("Customer Name:");

        addCustomerConfirmBtn.setText("Add");
        addCustomerConfirmBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerConfirmBtnActionPerformed(evt);
            }
        });

        addCustomerCancelBtn.setText("Cancel");

        jLabel7.setText("Phone Number:");

        javax.swing.GroupLayout addCustomerFrameLayout = new javax.swing.GroupLayout(addCustomerFrame.getContentPane());
        addCustomerFrame.getContentPane().setLayout(addCustomerFrameLayout);
        addCustomerFrameLayout.setHorizontalGroup(
            addCustomerFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addCustomerFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addCustomerFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addCustomerFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCName, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(txtCCode)
                    .addComponent(txtCNumber))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(addCustomerFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addCustomerCancelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(addCustomerConfirmBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        addCustomerFrameLayout.setVerticalGroup(
            addCustomerFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addCustomerFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addCustomerFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addCustomerConfirmBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addCustomerFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addCustomerCancelBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addCustomerFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        addOrderFrame.setMinimumSize(new java.awt.Dimension(500, 200));
        addOrderFrame.setLocationRelativeTo(null);

        jLabel8.setText("Customer Name:");

        jLabel9.setText("Product Name:");

        jLabel10.setText("Quantity:");

        comboBoxCusName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        comboBoxProName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxProName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxProNameItemStateChanged(evt);
            }
        });

        oAddConfirm.setText("Add");
        oAddConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oAddConfirmActionPerformed(evt);
            }
        });

        oAddCancel.setText("Cancel");
        oAddCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oAddCancelActionPerformed(evt);
            }
        });

        jLabel11.setText("Maximum:");

        maxProLabel.setText("10");

        javax.swing.GroupLayout addOrderFrameLayout = new javax.swing.GroupLayout(addOrderFrame.getContentPane());
        addOrderFrame.getContentPane().setLayout(addOrderFrameLayout);
        addOrderFrameLayout.setHorizontalGroup(
            addOrderFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addOrderFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addOrderFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35)
                .addGroup(addOrderFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addOrderFrameLayout.createSequentialGroup()
                        .addGroup(addOrderFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboBoxProName, javax.swing.GroupLayout.Alignment.TRAILING, 0, 200, Short.MAX_VALUE)
                            .addComponent(comboBoxCusName, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(addOrderFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(oAddConfirm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(oAddCancel, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)))
                    .addGroup(addOrderFrameLayout.createSequentialGroup()
                        .addComponent(txtOrderProNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(maxProLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        addOrderFrameLayout.setVerticalGroup(
            addOrderFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addOrderFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addOrderFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(comboBoxCusName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(oAddConfirm))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addOrderFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(comboBoxProName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(oAddCancel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addOrderFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtOrderProNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(maxProLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sale Management System");
        setResizable(false);

        mainTabPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mainTabPaneStateChanged(evt);
            }
        });

        pAddBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/_menu_addkw.png"))); // NOI18N
        pAddBtn.setText("Add");
        pAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pAddBtnActionPerformed(evt);
            }
        });

        pDelBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/_menu_dltkw.png"))); // NOI18N
        pDelBtn.setText("Delete");
        pDelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pDelBtnActionPerformed(evt);
            }
        });

        pTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "Product Code", "Product Name", "Quantity", "Saled", "Price"
            }
        ));
        pTableScrollPane.setViewportView(pTable);

        javax.swing.GroupLayout productTablePaneLayout = new javax.swing.GroupLayout(productTablePane);
        productTablePane.setLayout(productTablePaneLayout);
        productTablePaneLayout.setHorizontalGroup(
            productTablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productTablePaneLayout.createSequentialGroup()
                .addComponent(pTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(productTablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pDelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        productTablePaneLayout.setVerticalGroup(
            productTablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, productTablePaneLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(productTablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(productTablePaneLayout.createSequentialGroup()
                        .addComponent(pAddBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pDelBtn)))
                .addContainerGap())
        );

        mainTabPane.addTab("Product", productTablePane);

        cTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Customer Code", "Customer Name", "Phone Number"
            }
        ));
        cTableScollPane.setViewportView(cTable);

        cAddBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/_menu_addkw.png"))); // NOI18N
        cAddBtn.setText("Add");
        cAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cAddBtnActionPerformed(evt);
            }
        });

        cDelBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/_menu_dltkw.png"))); // NOI18N
        cDelBtn.setText("Delete");
        cDelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cDelBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout customerTablePaneLayout = new javax.swing.GroupLayout(customerTablePane);
        customerTablePane.setLayout(customerTablePaneLayout);
        customerTablePaneLayout.setHorizontalGroup(
            customerTablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerTablePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cTableScollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(customerTablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cDelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        customerTablePaneLayout.setVerticalGroup(
            customerTablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerTablePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(customerTablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(customerTablePaneLayout.createSequentialGroup()
                        .addComponent(cAddBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cDelBtn)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cTableScollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
                .addContainerGap())
        );

        mainTabPane.addTab("Customer", customerTablePane);

        oSortBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/sort_az_ascending.png"))); // NOI18N
        oSortBtn.setText("Sort");
        oSortBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oSortBtnActionPerformed(evt);
            }
        });

        oAddBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/_menu_addkw.png"))); // NOI18N
        oAddBtn.setText("Add");
        oAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oAddBtnActionPerformed(evt);
            }
        });

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        txtDisplayOrderTotal.setColumns(20);
        txtDisplayOrderTotal.setRows(5);
        displayOrderScrollPane.setViewportView(txtDisplayOrderTotal);

        jSplitPane1.setBottomComponent(displayOrderScrollPane);

        oTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        oTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                oTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(oTable);

        jSplitPane1.setLeftComponent(jScrollPane1);

        javax.swing.GroupLayout orderTablePaneLayout = new javax.swing.GroupLayout(orderTablePane);
        orderTablePane.setLayout(orderTablePaneLayout);
        orderTablePaneLayout.setHorizontalGroup(
            orderTablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderTablePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(orderTablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oSortBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(oAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        orderTablePaneLayout.setVerticalGroup(
            orderTablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderTablePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(orderTablePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(orderTablePaneLayout.createSequentialGroup()
                        .addComponent(oAddBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(oSortBtn)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)))
        );

        mainTabPane.addTab("Order", orderTablePane);

        mainToolBar.setFloatable(false);
        mainToolBar.setRollover(true);

        openBtnTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/_active__open.png"))); // NOI18N
        openBtnTool.setText("Open");
        openBtnTool.setFocusable(false);
        openBtnTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openBtnTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        openBtnTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openBtnToolActionPerformed(evt);
            }
        });
        mainToolBar.add(openBtnTool);

        saveBtnTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/_active__save.png"))); // NOI18N
        saveBtnTool.setText("Save");
        saveBtnTool.setFocusable(false);
        saveBtnTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveBtnTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveBtnTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnToolActionPerformed(evt);
            }
        });
        mainToolBar.add(saveBtnTool);

        saveAsBtnTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/_active__saveas.png"))); // NOI18N
        saveAsBtnTool.setText("Save As");
        saveAsBtnTool.setFocusable(false);
        saveAsBtnTool.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveAsBtnTool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveAsBtnTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsBtnToolActionPerformed(evt);
            }
        });
        mainToolBar.add(saveAsBtnTool);

        txtSearchString.setFocusable(false);
        txtSearchString.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtSearchStringMouseEntered(evt);
            }
        });

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        searchBtnTool.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/_active__find.png"))); // NOI18N
        searchBtnTool.setFocusable(false);
        searchBtnTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnToolActionPerformed(evt);
            }
        });
        jToolBar1.add(searchBtnTool);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        backDataBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/ic_arrow_back_48px-128.png"))); // NOI18N
        backDataBtn.setFocusable(false);
        backDataBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backDataBtnActionPerformed(evt);
            }
        });
        jToolBar2.add(backDataBtn);

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearchString, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSearchString, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainTabPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mainToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(mainToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainTabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //plist 
    private MyLinkedList<Product> listProduct, listProducttemp;
    private MyStack<Customer> listCustomer, listCustomertemp;
    private Object[] orders;
    private MyLinkedList<Order> listOrder;
    private OrderBinaryTree obt;
    private final QuickSort qs = new QuickSort();
    //these variables used to storage size of list<data> item
    //when add or deleted, compare to its 
    //then enable the save button
    private int psize_original, csize_original, osize_original;
    private int tableState = 0;
    private String pFileName, cFileName, oFileName = "";
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCustomerCancelBtn;
    private javax.swing.JButton addCustomerConfirmBtn;
    private javax.swing.JFrame addCustomerFrame;
    private javax.swing.JFrame addOrderFrame;
    private javax.swing.JButton addProductCancelBtn;
    private javax.swing.JButton addProductConfirmBtn;
    private javax.swing.JFrame addProductFrame;
    private javax.swing.JButton backDataBtn;
    private javax.swing.JButton cAddBtn;
    private javax.swing.JButton cDelBtn;
    private javax.swing.JTable cTable;
    private javax.swing.JScrollPane cTableScollPane;
    private javax.swing.JComboBox comboBoxCusName;
    private javax.swing.JComboBox comboBoxProName;
    private javax.swing.JPanel customerTablePane;
    private javax.swing.JScrollPane displayOrderScrollPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTabbedPane mainTabPane;
    private javax.swing.JToolBar mainToolBar;
    private javax.swing.JLabel maxProLabel;
    private javax.swing.JMenuItem menuSortCCode;
    private javax.swing.JMenuItem menuSortPcode;
    private javax.swing.JButton oAddBtn;
    private javax.swing.JButton oAddCancel;
    private javax.swing.JButton oAddConfirm;
    private javax.swing.JButton oSortBtn;
    private javax.swing.JTable oTable;
    private javax.swing.JButton openBtnTool;
    private javax.swing.JPanel orderTablePane;
    private javax.swing.JButton pAddBtn;
    private javax.swing.JButton pDelBtn;
    private javax.swing.JTable pTable;
    private javax.swing.JScrollPane pTableScrollPane;
    private javax.swing.JPopupMenu productSortPopup;
    private javax.swing.JPanel productTablePane;
    private javax.swing.JButton saveAsBtnTool;
    private javax.swing.JButton saveBtnTool;
    private javax.swing.JButton searchBtnTool;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTextField txtCCode;
    private javax.swing.JTextField txtCName;
    private javax.swing.JTextField txtCNumber;
    private javax.swing.JTextArea txtDisplayOrderTotal;
    private javax.swing.JSpinner txtOrderProNumber;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtProductPrice;
    private javax.swing.JSpinner txtProductQuantity;
    private javax.swing.JTextField txtSearchString;
    // End of variables declaration//GEN-END:variables
}
