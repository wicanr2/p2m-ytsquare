package com.ytsquare.push2me;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Push2Me  extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4334152941321906193L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                        throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Hello Push2ME!!</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1> Hello! Push2ME!</h1>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
