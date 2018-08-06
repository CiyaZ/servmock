package com.gaoshuhang.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class MockHandler implements HttpHandler
{
	private String response;
	private String contentType;

	MockHandler(String response, String contentType)
	{
		super();
		this.response = response;
		this.contentType = contentType + ";charset=utf8";
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException
	{
		httpExchange.getResponseHeaders().set("Content-Type", contentType);
		httpExchange.sendResponseHeaders(200, 0);
		OutputStream outputStream = httpExchange.getResponseBody();
		outputStream.write(response.getBytes());
		outputStream.close();
	}
}
