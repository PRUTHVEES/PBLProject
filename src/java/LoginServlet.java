import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import methods.ImpMethod;

public class LoginServlet extends HttpServlet {

    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Specify the path of the file to render
        response.setContentType("text/html");
        String filePath = getServletContext().getRealPath("index.html");
        ImpMethod impMethod = new ImpMethod();
        impMethod.renderFile(filePath,response.getOutputStream());
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        ImpMethod impMethod = new ImpMethod();
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost/pblproject","root","");

                String query = "SELECT * from credentials WHERE username=? AND password=?";
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setString(1, request.getParameter("username"));
                pstmt.setString(2, request.getParameter("password"));
                ResultSet resultSet = pstmt.executeQuery();

                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                
                if (!resultSet.isBeforeFirst()) { 
                    out.println("<h2>No data found!</h2>");
                } else {
                    resultSet.next();
                        String name = resultSet.getString("name");
                        Boolean is_admin = (resultSet.getInt("is_admin") > 0)? true : false;
                        if(is_admin==true) {
                            HttpSession session=request.getSession();  
                            Cookie c = new Cookie("is_admin",String.valueOf(is_admin));
                            response.addCookie(c);
                            session.setAttribute("name",name);
                            response.sendRedirect("http://localhost:8080/PBLProject/AdminDashboard");
                        } else {
                            if(name != null) {
                                response.sendRedirect("http://localhost:8080/PBLProject/Dashboard?name=" + resultSet.getString("name"));
                            } else {
                                response.sendRedirect("http://localhost:8080/PBLProject/Dashboard?name=" + resultSet.getString("username"));
                            }
                        }
                    }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
