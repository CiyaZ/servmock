package com.gaoshuhang;

import java.awt.*;

import com.gaoshuhang.form.MainForm;
import com.gaoshuhang.util.GoodLookUtil;

import javax.swing.plaf.FontUIResource;

public class App
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(() -> {
			try
			{
				//设置全局观感和字体
				GoodLookUtil.setNimbus();
				GoodLookUtil.setUIFont(new FontUIResource("Serif",Font.PLAIN,15));

				MainForm mainForm = new MainForm();
				mainForm.launchGUIForm();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
	}
}
