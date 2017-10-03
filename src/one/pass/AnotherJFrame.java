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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class AnotherJFrame extends JFrame implements ActionListener {

    static JFrame frame = new JFrame("One-Pass");
    MyUtils mu = new MyUtils();
    private SecretKey k;
    List<String> quarks = null;
    //Labels to identify the fields
    private JLabel scopeLabel;
    private JLabel nemLabel;
    private JLabel pwdLabel;
    private JLabel pwd2Label;

    //Fields for data entry
    private JFormattedTextField scopeField;
    private JFormattedTextField nemField;
    private JPasswordField pwdField;
    private JPasswordField confField;
    private int str1;

    public AnotherJFrame(String str, SecretKey MasterKey) {
        super("One-Pass");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        k=MasterKey;

//Create the labels.
        scopeLabel = new JLabel("Type(ID) ");
        nemLabel = new JLabel("userName ");
        pwdLabel = new JLabel("Password ");
        pwd2Label = new JLabel("Confirm Pwd ");

        //Create the text fields and set them up.
        scopeField = new JFormattedTextField(scopeField);
        scopeField.setColumns(15);

        nemField = new JFormattedTextField(nemField);
        nemField.setColumns(15);

        pwdField = new JPasswordField();
        pwdField.setColumns(15);

        confField = new JPasswordField();
//        confField.setValue(new Double(payment));
        confField.setColumns(15);

        //Tell accessibility tools about label/textfield pairs.
        scopeLabel.setLabelFor(scopeField);
        nemLabel.setLabelFor(nemField);
        pwdLabel.setLabelFor(pwdField);
        pwd2Label.setLabelFor(confField);

        //Lay out the labels in a panel.
        JPanel labelPane = new JPanel(new GridLayout(0, 1));
        labelPane.add(scopeLabel);
        labelPane.add(nemLabel);
        labelPane.add(pwdLabel);
        labelPane.add(pwd2Label);

        //Layout the text fields in a panel.
        JPanel fieldPane = new JPanel(new GridLayout(0, 1));
        fieldPane.add(scopeField);
        fieldPane.add(nemField);
        fieldPane.add(pwdField);
        fieldPane.add(confField);

        //Fill fields if update
        if (!str.equals("")) {
            str1 = Integer.parseInt(str);
            String[] s1 = quarks.get(str1).split("~~");
            scopeField.setText(s1[0]);
            nemField.setText(s1[1]);
        }

        JPanel p = new JPanel(new GridLayout());
        JButton addbtn = new JButton("Add");
        addbtn.addActionListener(this);
        addbtn.setActionCommand("add");
        JButton viewbtn = new JButton("View");
        viewbtn.addActionListener(this);
        viewbtn.setActionCommand("view");
        JButton clozbtn = new JButton("Close");
        clozbtn.addActionListener(this);
        clozbtn.setActionCommand("close");

        if (str.equals("")) {
            p.add(addbtn);
        }
        p.add(viewbtn);
        p.add(clozbtn);

        //Put the panels in this panel, labels on left,
        //text fields on right.
        getRootPane().setBorder(BorderFactory.createEmptyBorder(30, 30, 70, 70));
        add(labelPane, BorderLayout.CENTER);
        add(fieldPane, BorderLayout.LINE_END);
        add(p, BorderLayout.SOUTH);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 3);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 3);
        setLocation(x, y);
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        char[] inputPwd = pwdField.getPassword();
        char[] inputCon = confField.getPassword();
        String nem = nemField.getText();
        String scop = scopeField.getText();
        String b = new String(inputPwd);
        String c = new String(inputCon);
                DataBase db = new DataBase();
        try {
        } catch (Exception ex) {
            Logger.getLogger(AnotherJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } 
            Path path = Paths.get("encoded.ser");

        if (cmd.equals("add")) {
            if (nem.length() < 1 || scop.length() < 1 || b.length() < 1 || c.length() < 1) {
                mu.denial(frame, "Missing info!!");
            } else {
                if (b.equals(c)) {
            db=DataBase.load(path);
                    try {      
                        db.postId(scop,nem , b, k);
                    } catch (Exception ex) {
                        Logger.getLogger(AnotherJFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } 
            db.save(path);
                    //Zero out the possible password, for security.
                    Arrays.fill(inputPwd, '0');
                    Arrays.fill(inputCon, '0');
                    mu.success(frame, "Successfully saved");
                    scopeField.setText("");
                    nemField.setText("");
                    pwdField.setText("");
                    confField.setText("");
                } else {
                    mu.denial(frame, "Password mismatch!!");
                }
            }
        }


        if (cmd.equals("view")) {
            dispose();
            TableFrame tableFrame = new TableFrame(k);
        }

        if (cmd.equals("close")) {
            System.exit(0);
        }
    }
}
