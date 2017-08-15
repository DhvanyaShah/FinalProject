/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Trupesh Patel
 */

@Dependent
@ManagedBean
@RequestScoped
public class Members {

    /**
     * Creates a new instance of Members
     */
    String id,fname,lname,membership_start,membership_end,payment,phone;
    Librarians librarians=new Librarians();
    ArrayList membersList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMembership_start() {
        return membership_start;
    }

    public void setMembership_start(String membership_start) {
        this.membership_start = membership_start;
    }

    public String getMembership_end() {
        return membership_end;
    }

    public void setMembership_end(String membership_end) {
        this.membership_end = membership_end;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Members() {
    }
    // Used to save user record  
    Connection connection;
    public String save() {
        int result = 0;
        try {
            connection = librarians.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "insert into members(id,fname,lname,membership_start,membership_end,payment,phone) values(?,?,?,?,?,?,?)");
            stmt.setString(1, fname+lname);
            stmt.setString(2, fname);
            stmt.setString(3, lname);
            stmt.setString(4, membership_start);
            stmt.setString(5, membership_end);
            stmt.setString(6, payment);
            stmt.setString(7, phone);
            
            result = stmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        if (result != 0) {
            return "ListofMembers.xhtml?faces-redirect=true";
        } else {
            return "Librarian.xhtml?faces-redirect=true";
        }
    }

    // Used to fetch all records  
    public ArrayList membersList() {
        try {
            membersList = new ArrayList();
            
            connection = librarians.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from members");
            while (rs.next()) {
                Members members = new Members();
                members.setId(rs.getString("id"));
                members.setFname(rs.getString("fname"));
                members.setLname(rs.getString("lname"));
                members.setMembership_start(rs.getString("membership_start"));
                members.setMembership_end(rs.getString("membership_end"));
                members.setPayment(rs.getString("payment"));
                members.setPhone(rs.getString("phone"));
                membersList.add(members);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println("Database Connectivity Failure");
        }
        return membersList;
    }
    
    //to delete record
    public String delete(String id){
        
        Members members=null;
        try {
            connection = librarians.getConnection();
            PreparedStatement stmnt  = connection.prepareStatement("delete from members where id=?");
            
            stmnt.setString(1, id);
            stmnt.executeUpdate();     
     
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "ListofMembers.xhtml?faces-redirect=true";       
    }

}
