/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package one.pass;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.crypto.SecretKey;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
/**
 *
 * @author sammy
 */



public class OnePass extends JFrame implements ActionListener {

    static JFrame frame = new JFrame("One-Pass");
    private final JLabel nemLabel;
    private final JLabel pwdLabel;
    private final JLabel pwd2Label;
    //Fields for data entry
    private JFormattedTextField nemField;
    private final JPasswordField pwdField;
    private final JPasswordField confField;

    private final JButton sevBtn;
    private final JButton canBtn;
    private final JButton logBtn;

    DataBase db=new DataBase();
    File f = new File("encoded.ser");
    MyUtils utl = new MyUtils();
    
    private SecretKey MasterKey; 
    
    public OnePass() {
        super("One-Pass");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create the labels.
        nemLabel = new JLabel("userName ");
        pwdLabel = new JLabel("Password ");
        pwd2Label = new JLabel("Confirm Pwd ");

        nemField = new JFormattedTextField(nemField);
        nemField.setColumns(15);
        pwdField = new JPasswordField();
        pwdField.setColumns(15);

        confField = new JPasswordField();
        confField.setColumns(15);

        //Lay out the labels in a panel.
        JPanel labelPane = new JPanel(new GridLayout(0, 1));
        
        labelPane.add(nemLabel);
        labelPane.add(pwdLabel);

        if (!f.exists() && !f.isDirectory()) {
            labelPane.add(pwd2Label);
        }

        //Tell accessibility tools about label/textfield pairs.
        nemLabel.setLabelFor(nemField);
        pwdLabel.setLabelFor(pwdField);

        if (!f.exists() && !f.isDirectory()) {
            pwd2Label.setLabelFor(confField);
        }

        //Layout the text fields in a panel.
        JPanel fieldPane = new JPanel(new GridLayout(0, 1));
        fieldPane.add(nemField);
        fieldPane.add(pwdField);
        if (!f.exists() && !f.isDirectory()) {
            fieldPane.add(confField);
        }
        JPanel p = new JPanel(new GridLayout());
        sevBtn = new JButton("Save");
        sevBtn.addActionListener(this);
        sevBtn.setActionCommand("saveMaster");
        logBtn = new JButton("login");
        logBtn.addActionListener(this);
        logBtn.setActionCommand("login");
        canBtn = new JButton("Cancel");
        canBtn.addActionListener(this);
        canBtn.setActionCommand("cancel");

        if (!f.exists() && !f.isDirectory()) {
            p.add(sevBtn);
        } else {
            p.add(logBtn);
        }
        p.add(canBtn);

        getRootPane().setBorder(BorderFactory.createEmptyBorder(30, 30, 50, 50));
        JPanel pp = new JPanel();
        JLabel tyto = new JLabel("Create Master User!!");
        tyto.setFont(new Font("Serif", Font.BOLD, 15));
        if (!f.exists() && !f.isDirectory())
        pp.add(tyto);
        add(pp,BorderLayout.NORTH);
        add(labelPane, BorderLayout.CENTER);
        add(fieldPane, BorderLayout.LINE_END);
        add(p, BorderLayout.SOUTH);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 3);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 3);
        setLocation(x, y);
        pack();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        char[] inputPwd = pwdField.getPassword();
        char[] inputCon = confField.getPassword();
        String nem = nemField.getText();
        String b = new String(inputPwd);
        String c = new String(inputCon);
                
        String Mpwd = nem.trim()+b.trim();
        if (cmd.equals("saveMaster")) { //Process the password.
        if(nem.length()<2||c.length()<2||b.length()<2)
                utl.denial(frame, "Information too short or missing!!");
else
            if (b.equals(c)) {
                    try {
                        db.settings=new Settings("encoded", utl.ret_Salt(8), 100);
                        MasterKey=MyUtils.KDF(Mpwd, db.settings.d.salt, db.settings.d.it_counter, "PBKDF2WithHmacSHA256");
                        db.settings.set_v("encoded", MasterKey);
                        Path path = Paths.get("encoded.ser");
                        db.save(path);
                        //Zero out the possible password, for security.
                Arrays.fill(inputPwd, '0');
                Arrays.fill(inputCon, '0');
                utl.success(frame, "Root User successfully created");
                dispose();
                new AnotherJFrame("", MasterKey);
                    } catch (Exception ex) {
                        utl.denial(frame, "Error occured!!");
                    } 
                }
            else {
                utl.denial(frame, "Password mismatch!!");
            }
                
            } 
        

        if (cmd.equals("cancel")) {
            System.exit(0);
        }

        if (cmd.equals("login")) {
            try {
                Path path = Paths.get("encoded.ser");
               db = DataBase.load(path);
               
        if(nem.length()<2||b.length()<2)
                utl.denial(frame, "Missing info!!");
        else
          Mpwd = nem.trim()+b.trim();
          MasterKey=MyUtils.KDF(Mpwd, db.settings.d.salt, db.settings.d.it_counter, "PBKDF2WithHmacSHA256");
             if (db.settings.ret_V("encoded", MasterKey)) {
                    Arrays.fill(inputPwd, '0');
                    Arrays.fill(inputCon, '0');
                    dispose();
                    AnotherJFrame anotherJFrame = new AnotherJFrame("", MasterKey);
                } else {
                    utl.denial(frame, "Access Denied!!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
             utl.denial(frame, "Access Denied!!");
            } 
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new OnePass().setVisible(true);
            }

        });
    }

}