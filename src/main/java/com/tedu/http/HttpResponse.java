package com.tedu.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.tedu.common.HttpContext;

public class HttpResponse {
	private OutputStream out;
	private int status;
	private String contentType;
	// ��Ӧͷ-ContentLength
	private int contentLength = -1;

	// ��Ӧʵ��-�ļ�
	private File entity;
	/*
	 * ��¼����HTTP״̬���е�״̬������״̬����
	 */


	public HttpResponse(OutputStream out) {
		this.out = out;
	
	}

	public void setStatus(int i) {
		this.status = i;

	}

	public int getStatus() {
		return status;
	}

	public void setContentType(String string) {
		this.contentType = string;

	}

	public String getContentType() {
		return contentType;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public void setEntity(File file) {
		this.entity = file;

	}

	public File getEntity() {
		return entity;
	}

	public void flush() throws Exception {
		try {
			/*
			 * ��Ӧҳ��Ҫ���û����͵�����: HTTP/1.1 200 OKCRLF Content-Type:text/htmlCRLF
			 * Content-Length:273CRLF CRLF
			 * 10101010101101010101001010100(index.html����)
			 */
			System.out.println("������Ӧ��Ϣ");
			// 1����״̬��
			responseStatusLine();
			// 2������Ӧͷ
			responseHeader();
			// 3��Ӧ����
			responseEntity();

		} catch (Exception e) {
			System.out.println("��Ӧ�ͻ���ʧ��!");
			throw e;
		}
	}

	/**
	 * ��Ӧ״̬��
	 * 
	 * @throws Exception
	 */
	private void responseStatusLine() throws Exception {
		try {
			String line = "HTTP/1.1" + " " + status + " " +HttpContext.statusMap.get(status);
			println(line);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * ��Ӧͷ
	 * 
	 * @throws Exception
	 */
	private void responseHeader() throws Exception {
		try {
			String line = null;
			if (contentType != null) {
				line = "Content-Type:" + contentType;
				println(line);
			}
			if (contentLength >= 0) {
				line = "Content-Length:" + contentLength;
				println(line);
			}

			// ��������CRLF��ʾ��Ӧͷ��Ϣ���
			println("");

		} catch (Exception e) {
			throw e;
		}
	}

	private void println(String line) throws Exception {
		try {
			out.write(line.getBytes("ISO8859-1"));
			out.write(HttpContext.CR);// CR
			out.write(HttpContext.LF);// LF
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * ��Ӧ����
	 * 
	 * @throws Exception
	 */
	private void responseEntity() throws Exception {
		BufferedInputStream bis = null;
		try {
			/*
			 * ��entity�ļ��������ֽڷ��͸� �ͻ���
			 */
			bis = new BufferedInputStream(new FileInputStream(entity));
			BufferedOutputStream bos = new BufferedOutputStream(out);
			int d = -1;
			while ((d = bis.read()) != -1) {
				bos.write(d);
			}
			bos.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}

			}
		}
	}

}
