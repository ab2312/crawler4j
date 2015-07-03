/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package edu.uci.ics.crawler4j.tests;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * A example that demonstrates how HttpClient APIs can be used to perform
 * form-based logon.
 */
public class ClientFormLogin {

	public static void main(String[] args) throws Exception {
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore)
				.build();
		try {
			HttpGet httpget = new HttpGet("http://www.pixiv.net/");
			CloseableHttpResponse response1 = httpclient.execute(httpget);
			try {
				HttpEntity entity = response1.getEntity();

				System.out.println("Login form get: " + response1.getStatusLine());
				EntityUtils.consume(entity);

				System.out.println("Initial set of cookies:");
				List<Cookie> cookies = cookieStore.getCookies();
				if (cookies.isEmpty()) {
					System.out.println("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						System.out.println("- " + cookies.get(i).toString());
					}
				}
			} finally {
				response1.close();
			}
			
			
			HttpPost login = new HttpPost("https://www.secure.pixiv.net/login.php");
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			//提交两个参数及值
			nvps.add(new BasicNameValuePair("pixiv_id", "hsjab2312@hotmail.com"));
			nvps.add(new BasicNameValuePair("pass", "XXXX"));
			nvps.add(new BasicNameValuePair("mode", "login"));
			nvps.add(new BasicNameValuePair("return_to", "http://www.pixiv.net/"));
			nvps.add(new BasicNameValuePair("skip", "1"));

//			HttpUriRequest login = RequestBuilder.post().setUri(new URI("https://www.secure.pixiv.net/login.php"))
//					.addParameter("pixiv_id", "hsjab2312@hotmail.com")
//					.addParameter("pass", "XXXX")
//					.addParameter("skip", "1").build();
			CloseableHttpResponse response2 = httpclient.execute(login);
			try {
//				HttpEntity entity = response2.getEntity();
//
//				System.out.println("Login form get: " + response2.getStatusLine());
////				System.out.println(EntityUtils.toString(entity));
//				EntityUtils.consume(entity);
//
//				System.out.println("Post logon cookies:");
//				List<Cookie> cookies = cookieStore.getCookies();
//				if (cookies.isEmpty()) {
//					System.out.println("None");
//				} else {
//					for (int i = 0; i < cookies.size(); i++) {
//						System.out.println("- " + cookies.get(i).toString());
//					}
//				}
				
				//设置表单提交编码为UTF-8
				login.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
				HttpResponse response = httpclient.execute(login);
				HttpEntity entity = response.getEntity();
				System.out.println("Login form get: " + response.getStatusLine());
				System.out.println(EntityUtils.toString(entity));
				EntityUtils.consume(entity);
				
				System.out.println("Post logon cookies:");
				List<Cookie> cookies = cookieStore.getCookies();
				if (cookies.isEmpty()) {
					System.out.println("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						System.out.println("- " + cookies.get(i).toString());
					}
				}
				
				
				
			} finally {
				response2.close();
			}

			HttpGet httpgetafter = new HttpGet("http://www.pixiv.net/mypage.php#id=1459295");
			CloseableHttpResponse response3 = httpclient.execute(httpgetafter);
			try {
				HttpEntity entity = response3.getEntity();

				System.out.println("Login form get: " + response3.getStatusLine());

				System.out.println("Initial set of cookies:");
				List<Cookie> cookies = cookieStore.getCookies();
				if (cookies.isEmpty()) {
					System.out.println("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						System.out.println("- " + cookies.get(i).toString());
					}
				}
				
				System.out.println(EntityUtils.toString(entity));
				EntityUtils.consume(entity);
			} finally {
				response3.close();
			}
		} finally {
			httpclient.close();
		}
	}
}
