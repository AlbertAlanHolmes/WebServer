package com.tedu.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.tedu.common.HttpContext;
import com.tedu.core.HttpServlet;
import com.tedu.http.HttpRequest;
import com.tedu.http.HttpResponse;

/**
 * ��������û�ע�Ṧ��
 * @author adminitartor
 *
 */
public class LoginServlet  extends HttpServlet{
	
	
	public void service(HttpRequest request,HttpResponse response){
		System.out.println("��ʼ��½");
		String name=request.getParameter("username");
		String pwd=request.getParameter("password");
		System.out.println(name+","+pwd);
		
		BufferedReader br=null;
		try {
			br=new BufferedReader(new InputStreamReader(new FileInputStream("webapp"+File.separator+"userinfo.txt")));
			
			
			String line=null;
			boolean flag=false;
			while((line=br.readLine())!=null){
				System.out.println(line);
				
				String[] userinfos=line.split(",");
			
				if(name.equals(userinfos[0])&&pwd.equals(userinfos[1])){
					flag=true;
					break;
				}
				
			}
			
			if(flag){
				System.out.println("��½�ɹ�");
				forward("/login_ok.html", response);
			}else{
				System.out.println("��½ʧ��");
				forward("/login_error.html", response);
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
				try {
					if(br!=null){
					br.close();
					}
				} catch (IOException e) {
					
					e.printStackTrace();
				
			}
		}
		
		
		
	}
}




