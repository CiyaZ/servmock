package com.gaoshuhang.server;

import com.gaoshuhang.util.LogUtil;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class MockServer
{
	private static Map<String, String> serverURLConfig = new HashMap<>();
	private static Map<String, String> serverContentTypeConfig = new HashMap<>();
	private static HttpServer server = null;

	/**
	 * 启动JDK内置HTTP服务器
	 * @param port 端口号
	 */
	public static void launchMockServer(String contextPath, int port)
	{
		try
		{
			if(contextPath.endsWith("/"))
			{
				contextPath = contextPath.substring(0, contextPath.length() - 1);
			}

			server = HttpServer.create(new InetSocketAddress(port), 0);
			for(Map.Entry<String, String> entry : serverURLConfig.entrySet())
			{
				server.createContext(contextPath + entry.getKey(), new MockHandler(entry.getValue(), serverContentTypeConfig.get(entry.getKey())));
			}
			server.start();
			LogUtil.getLogger().info("服务器已启动，位于 localhost:" + port);
		}
		catch (IOException e)
		{
			LogUtil.getLogger().info("服务器启动失败");
			e.printStackTrace();
		}
	}

	/**
	 * 停止服务器
	 */
	public static void stopMockServer()
	{
		server.stop(0);
		LogUtil.getLogger().info("服务器已停止");
	}

	/**
	 * 重启服务器
	 * @param port 新的端口号
	 */
	public static void restartMockServer(String contextPath, int port)
	{
		stopMockServer();
		launchMockServer(contextPath, port);
	}

	public static void addServerURLConfig(String apiURL, String response)
	{
		serverURLConfig.put(apiURL, response);
	}

	public static void deleteServerURLConfig(String apiURL)
	{
		serverURLConfig.remove(apiURL);
	}

	public static void addServerContentTypeConfig(String apiURL, String contentTypeStr)
	{
		serverContentTypeConfig.put(apiURL, contentTypeStr);
	}

	public static void deleteServerContentTypeConfig(String apiURL)
	{
		serverContentTypeConfig.remove(apiURL);
	}
}
