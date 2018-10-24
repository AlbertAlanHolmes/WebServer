package com.tedu.core;

import java.io.File;

import com.tedu.common.HttpContext;
import com.tedu.http.HttpResponse;

public class HttpServlet {
	public void forward(String uri, HttpResponse response) throws Exception {
		File file = new File("webapp" + uri);
		response.setStatus(HttpContext.STATUS_CODE_OK);
		response.setContentType("text/html,charset=utf-8");
		response.setContentLength((int) file.length());
		response.setEntity(file);
		response.flush();
	}

}
