package com.gaoshuhang.util;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.util.Enumeration;

/**
 * 注意：在窗口初始化前调用
 */
public class GoodLookUtil
{

	public static void antiAliasing()
	{
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");
	}

	public static void setNimbus()
	{
		try
		{
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("set look and feel error");
		}
	}

	public static void setCDE()
	{
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("set look and feel error");
		}
	}

	public static void setGTKPlus()
	{
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("set look and feel error");
		}
	}

	//调用示例：GoodLookUtil.setUIFont(new FontUIResource("微软雅黑",Font.PLAIN,15));
	public static void setUIFont(FontUIResource f)
	{
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements())
		{
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof FontUIResource)
				UIManager.put(key, f);
		}
	}
}
