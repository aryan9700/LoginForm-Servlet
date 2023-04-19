package com.user;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.sql.DriverManager;

@MultipartConfig
public class Register extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            //getting all the incoming details from the request..
            String name = request.getParameter("user_name");
            String password = request.getParameter("user_password");
            String email = request.getParameter("user_email");
            Part part = request.getPart("image");
            String filename = part.getSubmittedFileName();
//            out.println(filename);
            //connection.............
            try {
                Thread.sleep(3000);

                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/youtube", "root", "348846");
                //query...
                String q = "insert into user(name,password,email,imageName) value(?,?,?,?)";
                PreparedStatement pstmt = con.prepareStatement(q);
                pstmt.setString(1, name);
                pstmt.setString(2, password);
                pstmt.setString(3, email);
                pstmt.setString(4, filename);
                pstmt.executeUpdate();
                //upload file to server.....
                
                InputStream is=part.getInputStream();
                byte []data= new byte[is.available()];
                
                is.read(data);//transfer file to data variable
                String path= request.getServletContext().getRealPath("/")+"img"+File.separator+filename;
//                out.println(path);
                
                FileOutputStream fos=new FileOutputStream(path);
                fos.write(data);
                fos.close();
                

                out.println("done");

            } catch (Exception e) {
                e.printStackTrace();
                out.println("error");

            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
