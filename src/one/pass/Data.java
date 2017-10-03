/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package one.pass;

import java.util.Date;

/**
 *
 * @author sammy
 */
class Data {
    String name;
    String scop;
    String pwd;

    public Data(String name, String scop, String pwd) {
        super();
        this.name = name;
        this.scop = scop;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return scop;
    }

    public void setType(String scop) {
        this.scop = scop;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
