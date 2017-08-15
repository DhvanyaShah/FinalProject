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
//@Named(value = "books")
@Dependent
@ManagedBean
@RequestScoped

public class Books {

    /**
     * Creates a new instance of Books
     */
    String id,title,isbn,author,category,publisher,issued,price,quantity;
    ArrayList booksList;

    Librarians librarians=new Librarians();
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    
    public Books() {
    }
    // Used to save user record  
    Connection connection;
    public String save() {
        int result = 0;
        try {
            connection = librarians.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "insert into books(id,title,isbn,author,category,publisher,issued,price,quantity) values(?,?,?,?,?,?,?,?,?)");
            stmt.setString(1, title+isbn);
            stmt.setString(2, title);
            stmt.setString(3, isbn);
            stmt.setString(4, author);
            stmt.setString(5, category);
            stmt.setString(6, publisher);
            stmt.setString(7, issued);
            stmt.setString(8, price);
            stmt.setString(9, quantity);
            
            result = stmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        if (result != 0) {
            return "ListofBooks.xhtml?faces-redirect=true";
        } else {
            return "Librarian.xhtml?faces-redirect=true";
        }
    }
    
    // Used to fetch all records  
    public ArrayList booksList() {
        try {
            booksList = new ArrayList();
            
            connection = librarians.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from books");
            while (rs.next()) {
                Books books = new Books();
                books.setId(rs.getString("id"));
                books.setTitle(rs.getString("title"));
                books.setIsbn(rs.getString("isbn"));
                books.setAuthor(rs.getString("author"));
                books.setCategory(rs.getString("category"));
                books.setPublisher(rs.getString("publisher"));
                books.setIssued(rs.getString("issued"));
                books.setPrice(rs.getString("price"));
                books.setQuantity(rs.getString("quantity"));
                booksList.add(books);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println("Database Connectivity Failure");
        }
        return booksList;
    }

    //to delete record
    public String delete(String id){
        
        Books books=null;
        try {
            connection = librarians.getConnection();
            PreparedStatement stmnt  = connection.prepareStatement("delete from books where id=?");
            
            stmnt.setString(1, id);
            stmnt.executeUpdate();     
     
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "ListofBooks.xhtml?faces-redirect=true";       
    }
    
}
