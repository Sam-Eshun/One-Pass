/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package one.pass;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.table.AbstractTableModel;
import one.pass.DataBase.Id;

/**
 *
 * @author sammy
 */
class ModelData extends AbstractTableModel {

    List<Data> data = new ArrayList<>();
    String colNames[] = {"Type(ID)", "Username", "Password"};
    Class<?> colClasses[] = {String.class, String.class, String.class};

    MyUtils mu = new MyUtils();

    ModelData(SecretKey key) {
        try {
            Path path = Paths.get("encoded.ser");
            DataBase db = DataBase.load(path);
            for(Id i_d: db.database.values())
                data.add(new Data(i_d.id, i_d.username, MyUtils.decode(i_d.pwd, key)));
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | BadPaddingException |
                IllegalBlockSizeException | NoSuchPaddingException ex) {
            Logger.getLogger(ModelData.class.getName()).log(Level.SEVERE, null, ex);
        } 
            
        }
    

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return data.get(rowIndex).getName();
        }
        if (columnIndex == 1) {
            return data.get(rowIndex).getType();
        }
        if (columnIndex == 2) {
            return data.get(rowIndex).getPwd();
        }
        return null;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return colClasses[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public void setValueAt(String aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            data.get(rowIndex).setName((String) aValue);
        }
        if (columnIndex == 1) {
            data.get(rowIndex).setType((String) aValue);
        }
        if (columnIndex == 2) {
            data.get(rowIndex).setPwd(aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
