package com.javafun.timetracking.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WelcomePageServlet extends HttpServlet {

	private static final long serialVersionUID = 2635258112565672725L;

	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect(resp.encodeRedirectURL("welcomepage"));
	}
}