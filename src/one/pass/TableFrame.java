/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package one.pass;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.crypto.SecretKey;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;

/**
 *
 * @author sammy
 */
public class TableFrame extends JFrame implements ActionListener {

    static JFrame frame = new JFrame("One-Pass");
    MyUtils mu = new MyUtils();
    String sel = "";
    SecretKey k;

    public TableFrame(SecretKey key) {
        super("One-Pass");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String col[] = {"Scope", "Username", "Pwd"};
        k=key;

        JTable table = new JTable(new ModelData(k));
        JTableHeader header = table.getTableHeader();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel selectionModel = table.getSelectionModel();

        selectionModel.addListSelectionListener(this::handleSelectionEvent);

        JPanel p1 = new JPanel();
        BoxLayout boxlayout = new BoxLayout(p1, BoxLayout.Y_AXIS);
        p1.setLayout(boxlayout);
        p1.add(header);
        p1.add(table);

        JButton bakbtn = new JButton("Back");
        bakbtn.addActionListener(this);
        bakbtn.setActionCommand("back");
        JButton clozbtn = new JButton("Close");
        clozbtn.addActionListener(this);
        clozbtn.setActionCommand("close");
        JPanel p = new JPanel(new GridLayout());
        p.setPreferredSize(new Dimension(5, 0));
        p.add(bakbtn);
        p.add(clozbtn);
        p1.add(p);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 4);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 3);
        add(p1, BorderLayout.CENTER);
        setLocation(x, y);
        pack();
        setMinimumSize(new Dimension(700, 0));
        setVisible(true);

    }

    protected void handleSelectionEvent(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        // e.getSource() returns an object like this
        // javax.swing.DefaultListSelectionModel 1052752867 ={11}
        // where 11 is the index of selected element when mouse button is released
        String strSource = e.getSource().toString();
        System.out.println("selected " + strSource);
        int start = strSource.indexOf("{") + 1,
                stop = strSource.length() - 1;
        sel = strSource.substring(start, stop);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("back")) {
            dispose();
            new AnotherJFrame("", k);
        }
        
        if (cmd.equals("close")) {
            System.exit(0);
        }
    }

}
