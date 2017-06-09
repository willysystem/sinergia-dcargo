package com.sinergia.dcargo.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = -5826816056346734478L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		
		 HttpSession session = req.getSession(false);
	      if (session != null) {
	            session.invalidate();
	      }
	      res.sendRedirect("DCargo.html");
		
	}
}