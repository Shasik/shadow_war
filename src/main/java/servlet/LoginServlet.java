package servlet;

import database.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("ACCESS DENIED");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");

        switch (User.checkLogin(nickname, password)) {
            case 1:
                request.getSession().setAttribute("nickname", nickname);
                this.getServletContext().getRequestDispatcher("/homePage.jsp").forward(request, response);
                break;
            case 0:
                User.registrationUser(nickname, password);
                request.getSession().setAttribute("nickname", nickname);
                this.getServletContext().getRequestDispatcher("/homePage.jsp").forward(request, response);
                break;
            case -1:
                response.sendRedirect(this.getServletContext().getContextPath() + "/error401.html");
                break;
            default:
                out.print("INTERNAL SERVER ERROR. PLEASE TRY IT LATER");
        }
    }
}
