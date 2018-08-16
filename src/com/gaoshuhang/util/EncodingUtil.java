package com.gaoshuhang.util;

import java.io.UnsupportedEncodingException;

public class EncodingUtil
{
	public static String GBKToUtf8(String str)
	{
		try
		{
			byte[] strBytes = str.getBytes("GBK");
			return new String(strBytes, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return str;
		}
	}

	public static String UTF8ToGBK(String str)
	{
		try
		{
			byte[] strBytes = str.getBytes("UTF-8");
			return new String(strBytes, "GBK");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return str;
		}
	}
}
