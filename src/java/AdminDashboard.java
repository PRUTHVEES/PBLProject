/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import methods.ImpMethod;

/**
 *
 * @author pruth
 */


public class AdminDashboard extends HttpServlet {
protected String getFormattedFeedback(int cid) {
    ArrayList<String> feedback = new ArrayList<>();
    String driver = "com.mysql.jdbc.Driver";
    Connection con = null;
    StringBuilder feedbacks = new StringBuilder();

    try {
        // Step 1: Load the JDBC driver and establish a connection to the database
        Class.forName(driver);
        con = DriverManager.getConnection("jdbc:mysql://localhost/pblproject", "root", "");

        // Step 2: Query the database to get feedback based on the course ID
        String query = "SELECT feedback FROM course_feedback WHERE courseid=?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, cid);  // Set the course ID as a parameter
        ResultSet rs = stmt.executeQuery();

        // Step 3: Collect feedback from the result set
        while (rs.next()) {
            feedback.add(rs.getString("feedback"));
        }

        // Step 4: If feedback exists, format it as JSON-like string
        if (!feedback.isEmpty()) {
            for (int i = 0; i < feedback.size(); i++) {
                feedbacks.append("{feedback: \"")
                         .append(feedback.get(i).replace("\"", "\\\"")) // Escape double quotes
                         .append("\"}")
                         .append(i == feedback.size() - 1 ? "" : ","); // Append comma except for the last item
            }
        } else {
            // If no feedback is available, return an empty array or a default message
            feedbacks.append("[]");
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (con != null) con.close(); // Close the database connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Step 5: Return the formatted feedback string
    System.out.println(feedbacks.toString());
    return feedbacks.toString();
}



    protected ArrayList<String> getFeedback(int cid) {
            ArrayList<String> feedback=new ArrayList<>();
            String driver="com.mysql.jdbc.Driver";
            Connection con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection("jdbc:mysql://localhost/pblproject","root","");
            
            String query = "SELECT * FROM course_feedback WHERE courseid=(?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1,cid);
            
            ResultSet rs = stmt.executeQuery();
            
            if(rs.isBeforeFirst()) {
                while(rs.next()) {
                    feedback.add(rs.getString("feedback"));
                }
                System.out.println(feedback.toString());
                return feedback;
            } else {
                feedback.add("");
                return feedback;
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try{
                con.close();
            }catch(Exception e) {
                e.printStackTrace();
           };
        }
        return feedback;
    }
    
    protected String printFeedback(ArrayList<String> courseFeedback) {
    StringBuilder feedbacks = new StringBuilder(); 
    for (int i = 0; i < courseFeedback.size(); i++) {
        feedbacks.append("{feedback: \"")
                .append(courseFeedback.get(i).replace("\"", "\\\"")) // Escape double quotes
                .append("\"}")
                .append(i == courseFeedback.size() - 1 ? "" : ",");   
    }
    return feedbacks.toString();
}

    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            Cookie[] cks = request.getCookies();
                boolean is_admin=false;
                for(Cookie i:cks) {
                    if(i.getName().equals("is_admin") && i.getValue().equals("true")) {
                        is_admin=true;
                    }
                }
                
                if((is_admin==true)&&(request.getSession(false)!=null)) {
                    try {
                    response.setContentType("text/html");
                    OutputStream out = response.getOutputStream();
                    String data = "<!DOCTYPE html>\n" +
"<html lang=\"en\">\n" +
"<head>\n" +
"    <meta charset=\"UTF-8\">\n" +
"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
"    <title>Feedback Dashboard</title>\n" +
"	<script>\n" +
"        // Sample feedback data\n" +
"        const feedbackData = {\n" +
"            \"101\": [\n" + getFormattedFeedback(101) +
"            ],\n" +
"            \"102\": [\n" + printFeedback(getFeedback(102)) + 
"            ],\n" +
"            \"103\": [\n" + printFeedback(getFeedback(103)) +
"            ],\n" +
"        };";
                    out.write(data.getBytes());
                    response.setContentType("text/html");
                    String filePath = getServletContext().getRealPath("adminDashboard.html");

                    ImpMethod impMethod = new ImpMethod();
                    impMethod.renderFile(filePath,out);
                } catch(Exception e)  {
                    e.printStackTrace();
                }
                }
    }

}
