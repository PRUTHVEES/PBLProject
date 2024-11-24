/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import methods.ImpMethod;

/**
 *
 * @author pruth
 */
public class FeedbackServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if(request.getQueryString().contains("courseid")) {
        if(session!=null) {
            response.setContentType("text/html");
            OutputStream out = response.getOutputStream();
            
            String filePath = getServletContext().getRealPath("feedbackform.html");
            ImpMethod mt = new ImpMethod();
            mt.renderFile(filePath,out);
            
            String toBePrinted = "\n" +
"            <th>Course ID</th>\n" +
"            <td><input type=\"text\" id=\"courseId\" name=\"courseId\" placeholder=\"Enter Course ID\" value=\""+request.getParameter("courseid")+"\"*</td>\n" +
"        </tr>\n" +
"        <tr>\n" +
"            <th>Course Name</th>\n" +
"            <td><input type=\"text\" id=\"courseName\" name=\"courseName\" placeholder=\"Enter Course Name\" value=\""+request.getParameter("coursename")+"\"></td>\n" +
"        </tr>\n" +
"        <tr>\n" +
"            <th>Enter Feedback</th>\n" +
"            <td><textarea id=\"feedback\" name=\"feedback\" placeholder=\"Enter your feedback here\"></textarea></td>\n" +
"        </tr>\n" +
"        <tr class=\"submit-row\">\n" +
"            <td colspan=\"2\">\n" +
"                <button type=\"submit\" onclick=\"http://localhost:8080/PBLProject/FeedbackServlet?courseid\">Submit</button>\n" +
"            </td>\n" +
"        </tr>\n" +
"    </table>\n" +
"</body>\n" +
"</html>";
            
            out.write(toBePrinted.getBytes());
        }
        } 
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getSession()!=null) {
            try {
                int courseid = Integer.parseInt(request.getParameter("courseId"));
                String coursename = String.valueOf(request.getParameter("courseName"));
                String feedback = String.valueOf(request.getParameter("feedback"));
                
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost/pblproject","root","");
                
                String query = "INSERT INTO course_feedback (courseid,coursename,feedback) VALUES (?,?,?)";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1,courseid);
                stmt.setString(2,coursename);
                stmt.setString(3,feedback);
                
                int rowcount = stmt.executeUpdate();
                
                response.setContentType("text/html");
                
                if (rowcount > 0) { 
                    response.getOutputStream().write("Form Data submitted successfully?<h2>".getBytes());
                } else {
                    response.getOutputStream().write("Form Data not submitted successfully :-< . Please write your response again".getBytes());
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                
            }
    }
    }
}
