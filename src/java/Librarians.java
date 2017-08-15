/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author Trupesh Patel
 */
//@Named(value = "librarians")
@Dependent
@ManagedBean
@RequestScoped
public class Librarians {

    /**
     * Creates a new instance of Librarians
     */
    String id;
    String fname;
    String lname;
    String email;
    String password;
    ArrayList librariansList;
    private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Object> getSessionMap() {
        return sessionMap;
    }

    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }
    Connection connection;
    
    public String getGenderName(String gender) {
        if (gender == "Mr") {
            return "Mr";
        } else if(gender=="Mrs"){
            return "Mrs";
        }else{
            return "Miss";
        }
    }

    // Used to establish connection  

    public Connection getConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/JavaAssignment", "Trupesh", "1234");
        } catch (Exception e) {
            System.out.println(e);
        }
        return connection;
    }

    
    // Used to save user record  

    public String save() {
        int result = 0;
        try {
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "insert into librarians(id,fname,lname,email,password) values(?,?,?,?,?)");
            stmt.setString(1, gender+fname+lname);
            stmt.setString(2, fname);
            stmt.setString(3, lname);
            stmt.setString(4, email);
            stmt.setString(5, password);
            result = stmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        if (result != 0) {
            return "ListofLibrarian.xhtml?faces-redirect=true";
        } else {
            return "Admin.xhtml?faces-redirect=true";
        }
    }

    // Used to fetch all records  
    public ArrayList librariansList() {
        try {
            librariansList = new ArrayList();
            
            connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from librarians");
            while (rs.next()) {
                Librarians librarians = new Librarians();
                librarians.setId(rs.getString("id"));
                librarians.setFname(rs.getString("fname"));
                librarians.setLname(rs.getString("lname"));
                librarians.setEmail(rs.getString("email"));
                librarians.setPassword(rs.getString("password"));
                librariansList.add(librarians);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println("Database Connectivity Failure");
        }
        return librariansList;
    }
    
    //to delete record
    public String delete(String id){
        Librarians librarians=null;
        try {
            connection = getConnection();
            PreparedStatement stmnt  = connection.prepareStatement("delete from librarians where id=?");
            
            stmnt.setString(1, id);
            stmnt.executeUpdate();     
     
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "ListofLibrarian.xhtml?faces-redirect=true";       
    }
    
    //to login
    public String login(String fname,String password)
    {
        int results = 0;
        String userId="";
        try{
            connection = getConnection();
            PreparedStatement stmnt  =
                    connection.prepareStatement("select id from "
                            + "librarians where fname =? and password = ?");

            stmnt.setString(1, fname);
            stmnt.setString(2, password);
            ResultSet result= stmnt.executeQuery();
            
            while(result!=null & result.next()){
                userId = result.getString("id");

            //GlobalDeclaration.UserId=userId;
            

            }
            connection.close();
        }catch(Exception e){
            
        }
        if(userId == null || userId==""){
                System.out.println("<font color=red> User not found</font>");
                return "LibrarianLogin.xhtml?faces-redirect=true";
            }else{
                System.out.println("<font color=green> User  found     "+  userId+"</font>");           
                return "Librarian.xhtml?faces-redirect=true";
            }
    }
    
    //used for admin login
    public String adminlogin(String name,String password){
        if("admin".equals(name) && "admin".equals(password)){
            return "Admin.xhtml?faces-redirect=true";
        }else{
            return "AdminLogin.xhtml?faces-redirect=true";
        }
    }
}
