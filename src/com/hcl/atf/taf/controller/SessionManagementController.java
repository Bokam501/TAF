package com.hcl.atf.taf.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public class SessionManagementController {
	@ExceptionHandler(Exception.class)
	public void handleException(Throwable ex, HttpServletResponse response)
			throws Exception {
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				ex.getMessage());
	}
}