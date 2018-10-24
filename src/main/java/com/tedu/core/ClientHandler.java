package com.tedu.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpRetryException;
import java.net.Socket;

import com.tedu.common.HttpContext;
import com.tedu.http.HttpRequest;
import com.tedu.http.HttpResponse;
import com.tedu.service.LoginServlet;
import com.tedu.service.RegServlet;

/**
 * ���߳��������ڴ���ÿ���ͻ��˵�����
 * 
 * @author adminitartor
 *
 */
public class ClientHandler implements Runnable {
	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			InputStream in = socket.getInputStream();
			HttpRequest request = new HttpRequest(in);

			OutputStream out = socket.getOutputStream();
			HttpResponse response = new HttpResponse(out);

			String uri = request.getUri();
			System.out.println(uri);
			File file = new File("webapp" + uri);
			if("/".equals(uri)){
				file = new File("webapp/index.html");
				responseFile(HttpContext.STATUS_CODE_OK, file, response);
			}else if (file.exists()) {
				System.out.println("�ҵ�����Դ" + file.length());
				responseFile(HttpContext.STATUS_CODE_OK, file, response);

				//�鿴�Ƿ�����һ������
			} else if("/reg".equals(uri)){
				RegServlet servlet = new RegServlet();
				servlet.service(request,response);
			}else if("/login".equals(uri)){
				LoginServlet servlet = new LoginServlet();
				servlet.service(request,response);
			}else {
				System.out.println("û����Դ��404");
				file = new File("webapp/404.html");
				responseFile(HttpContext.STATUS_CODE_NOTFOUND, file, response);
			}
			// //������
			// String line = readLine(in);
			// System.out.println(line);
			// //��������(ֱ��������ȡ��CRLF)����Ϣͷ
			//
			// //��ȡ��Ϣ����
			//
			//
			// //�����������е�������Դ

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("�ر�socketʧ��");
				e.printStackTrace();
			}
		}
	}

	private String getContentTypeByFile(File file) {
		String name = file.getName();
		System.out.println("�ļ���" + name);

		String ext = name.substring(name.lastIndexOf(".") + 1);
		System.out.println("��׺��" + ext);

		String contentType = HttpContext.contentTypeMapping.get(ext);
		return contentType;
	}

	private void responseFile(int status, File file, HttpResponse response) throws Exception {
		response.setStatus(status);
		response.setContentType(getContentTypeByFile(file));
		response.setEntity(file);
		response.flush();
	}

}
