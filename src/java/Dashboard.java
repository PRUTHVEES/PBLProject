/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import methods.ImpMethod;

/**
 *
 * @author pruth
 */
public class Dashboard extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            
            HttpSession session=request.getSession(false);  
            if(request.getQueryString().contains("name") && session != null) {
                
                    String welcomeString = "<p><strong>Welcome " + request.getParameter("name") + "!</strong></p>"; 
                    response.setContentType("text/html");
                    response.getOutputStream().write(welcomeString.getBytes());

                    String filePath = getServletContext().getRealPath("dashboard.html");
                    ImpMethod impMethod = new ImpMethod();
                    impMethod.renderFile(filePath,response.getOutputStream());
            } else {
                response.getWriter().write("Please Login First!");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
