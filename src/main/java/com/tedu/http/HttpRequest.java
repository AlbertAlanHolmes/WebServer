package com.tedu.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.tedu.common.HttpContext;

/**
 * http������󣬷�װһ��http���������Ϣ
 * 
 * @author shaozg
 *
 */
public class HttpRequest {
	// ���󷽷�
	private String method;
	// ������Դ
	private String uri;
	// ����Э��汾
	private String protocol;

	// ��Ϣ��ͷ��Ϣ
	private Map<String, String> header;
	
	private Map<String, String> parameters;
	
	
	public static void main(String[] args) {
		HttpRequest r=new HttpRequest();
		r.parseUri("/reg.html?username=szg&password=123456&nickname=shaozg&mailbox=szg%40tedu.cn");
		System.out.println("URI:"+r.uri);
		System.out.println("kv������"+r.parameters);
	
	
	}
//	private void parseUri(String uri){
//		/*
//		 * 
//		 * 	private Map<String, String> parameters;
//		 * /reg.html?username=szg&password=123456&nickname=shaozg&mailbox=szg%40tedu.cn
//		 * 1ʵ����HashMap���ڳ�ʼ������parameters
//		 * 
//		 * 
//		 * 
//		 * 
//		 */
//		this.uri=uri;
//		parameters=new HashMap<String,String>();
//		//1�Ȱ���?���
//				String[] data = uri.split("\\?");
//				System.out.println("loc:"+data[0]);
//				
//				//2����&������в���
//				data = data[1].split("&");
//				for(int i=0;i<data.length;i++){
//					//��ÿһ����������=���
//					String[] arr = data[i].split("=");
////					System.out.println(arr[0]+":"+arr[1]);
//					parameters.put(arr[0], arr[1]);
//				}
//				
//
//	}
	private void parseUri(String uri){
		/*
		 * /index.html
		 * /reg?username=fancq&password=123456&nickname=fanfan
		 * ��GET�����У�URI���ܻ����������������
		 * HTTPЭ���й涨��GET�����е�URI���Դ���
		 * ���������������������Դ������"?"�ָ
		 * ֮����Ϊ����Ҫ���ݵĲ�����ÿ��������:
		 * name=value�ĸ�ʽ���棬ÿ������֮��ʹ��
		 * "&"�ָ
		 * ����Ĵ���Ҫ��:
		 * ��"?"֮ǰ�����ݱ��浽����uri�ϡ�
		 * ֮���ÿ����������������parameters��
		 * ����keyΪ��������valueΪ������ֵ��
		 * 
		 * 1:ʵ����HashMap���ڳ�ʼ������parameters
		 * 
		 */
		//1
		this.parameters = new HashMap<String,String>();
		
		//�ȷ���uri���Ƿ���"?"
		int index = -1;
		if((index = uri.indexOf("?"))>=0){
			//�Ȱ���"?"���
			this.uri = uri.substring(0,index);
			
			/*
			 * ��ȡ�����в���
			 * paras:username=fancq&password=123456&nickname=fanfan
			 */
			String paras = uri.substring(index+1);
			/*
			 * ���ÿһ������
			 * [username=fancq,password=123456,nickname=fanfan]
			 */
			String[] paraArray = paras.split("&");
			//����ÿһ������
			for(String para : paraArray){
				//����"="���
				String[] paraDate = para.split("=");
				this.parameters.put(paraDate[0], paraDate[1]);
			}
		}else{
			this.uri = uri;
		}
		
		
	}
	
	public String getParameter(String name){
		
		return parameters.get(name);
	}

	public HttpRequest(InputStream in) throws Exception {

		try {
			System.out.println("httpRequest������");
			// 1����������
			
			parseRequestLine(in);
			// 2������Ϣͷ
			parseRequestHeader(in);
			// 3��������
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public HttpRequest() {
		
	}
	/*
	 * ������Ϣͷ
	 */
	private void parseRequestHeader(InputStream in) {
		System.out.println("��������ͷ");
		try {
			header = new HashMap<String, String>();
			String line = null;
			while (true) {
				line = readLine(in);
				if (line.length() == 0)
					break;
				int index = line.indexOf(":");

				// System.out.println(Arrays.toString(data));
				// System.out.println(data[0]+data[1]);
				String key = line.substring(0, index);
				String value = line.substring(index + 1).trim();
				// System.out.println(key);
				// System.out.println(value);
				header.put(key, value);
				// header.put(data[0], data[1]);
			}

			System.out.println(header);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private void parseRequestLine(InputStream in) throws Exception {
		System.out.println("����������");
		/*
		 * ʵ�ֲ��裺 1���ȶ�ȡһ���ַ��� 2�����ݿո��� 3�����������е������������õ�HR��Ӧ������
		 */
		try {
			String line = readLine(in);

			System.out.println("ss:" + line);
			if (line.length() == 0)
				throw new RuntimeException("�յ�������");
			String[] data = line.split("\\s");
			method = data[0];
//			uri = data[1];
			parseUri(data[1]);
			protocol = data[2];
//			System.out.println("�ҵĽ��������󷽷�=" + method + ",��Դ��ʶ=" + uri + ",Э��=" + protocol);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * ������������ȡһ���ַ��� ����HTTPЭ���ȡ�����е�һ������, ��CRLF��β��һ���ַ���
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private String readLine(InputStream in) throws IOException {
		// �����е�һ���ַ���(����������)
		StringBuilder builder = new StringBuilder();
		/*
		 * ������ȡ�����ַ���ֱ��������ȡ����CRLFΪֹ
		 */
		// c1�Ǳ��ζ������ַ���c2���ϴζ������ַ�
		int c1 = -1, c2 = -1;
		while ((c1 = in.read()) != -1) {
			if (c1 == HttpContext.LF && c2 == HttpContext.CR) {
				break;
			}
			builder.append((char) c1);
			c2 = c1;
		}
		return builder.toString().trim();
	}

	public String getMethod() {
		return method;
	}

	public String getUri() {
		return uri;
	}

	public String getProtocol() {
		return protocol;
	}

	public Map<String, String> getHeader() {
		return header;
	}

}
