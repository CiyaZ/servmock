package com.gaoshuhang.util;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JTextAreaOutputStream extends OutputStream
{
	private JTextArea jTextArea;
	//注：使用ByteArrayOutputStream而不是StringBuilder作为缓冲区，是因为按byte写会导致乱码
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();

	public JTextAreaOutputStream(JTextArea jTextArea)
	{
		super();
		this.jTextArea = jTextArea;
	}

	@Override
	public void write(int b) throws IOException
	{
		char c = (char) b;
		if(c == '\n')
		{
			baos.write('\n');
			byte[] bytes = baos.toByteArray();
			String str = new String(bytes, "UTF-8");
			jTextArea.append(str);
			baos.reset();
		}
		else
		{
			baos.write(b);
		}
	}
}
