package com.gaoshuhang.form;

import com.gaoshuhang.server.MockServer;
import com.gaoshuhang.util.JTextAreaOutputStream;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainForm
{
	private JFrame frame;

	private JTextField mContextPathTextField;
	private JTextField mPortTextField;
	private JButton mStartServerButton;
	private JButton mStopServerButton;
	private JButton mRestartServerButton;
	private JTextArea mServerLogOutputTextArea;
	private JScrollPane mServiceConfigScrollPane;
	private JButton mAddServiceConfigButton;
	private JPanel mMainPanel;

	private JPanel mScrollPaneInnerPanel = new JPanel();

	public MainForm()
	{
		//UI子组件初始化
		initUI();
	}

	public void launchGUIForm()
	{
		frame = new JFrame("MainForm");
		frame.setContentPane(new MainForm().mMainPanel);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setTitle("Web API 服务模拟器");
		frame.pack();

		//设置窗口大小
		frame.setSize(640, 480);

		//设置窗口出现在屏幕中间
		int windowWidth = frame.getWidth();
		int windowHeight = frame.getHeight();
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		frame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);

		frame.setVisible(true);
	}

	private void initUI()
	{
		mStartServerButton.addActionListener((e) -> {

			redirectSysout();

			String serverPortStr = mPortTextField.getText();
			String contextPath = mContextPathTextField.getText();

			if (serverPortTextFieldCheck(serverPortStr) && contextPathTextFieldCheck(contextPath))
			{
				int serverPort = Integer.parseInt(serverPortStr);
				MockServer.launchMockServer(contextPath, serverPort);
			}

			mStartServerButton.setEnabled(false);
			mStopServerButton.setEnabled(true);
			mRestartServerButton.setEnabled(true);
		});

		mStopServerButton.addActionListener((e) -> {
			redirectSysout();
			MockServer.stopMockServer();

			mStartServerButton.setEnabled(true);
			mStopServerButton.setEnabled(false);
			mRestartServerButton.setEnabled(false);
		});

		mRestartServerButton.addActionListener((e) -> {

			redirectSysout();

			String serverPortStr = mPortTextField.getText();
			String contextPath = mContextPathTextField.getText();

			if (serverPortTextFieldCheck(serverPortStr) && contextPathTextFieldCheck(contextPath))
			{
				int serverPort = Integer.parseInt(serverPortStr);
				MockServer.restartMockServer(contextPath, serverPort);
			}
		});

		mServiceConfigScrollPane.setViewportView(mScrollPaneInnerPanel);
		mScrollPaneInnerPanel.setLayout(new BoxLayout(mScrollPaneInnerPanel, BoxLayout.Y_AXIS));



		mAddServiceConfigButton.addActionListener((e) -> {
			AddConfigDialog addConfigDialog = new AddConfigDialog(frame);
			addConfigDialog.setVisible(true);
		});
	}

	private void redirectSysout()
	{
		JTextAreaOutputStream jtaos = new JTextAreaOutputStream(mServerLogOutputTextArea);
		PrintStream ps = new PrintStream(jtaos);
		System.setOut(ps);
		System.setErr(ps);
	}

	/**
	 * 端口号输入框校验
	 *
	 * @param serverPortStr 输入框字符串
	 * @return 校验成功返回true, 否则返回false
	 */
	private boolean serverPortTextFieldCheck(String serverPortStr)
	{
		//错误处理 端口未填写
		if ("".equals(serverPortStr))
		{
			JOptionPane.showMessageDialog(mMainPanel, "端口号不能为空", "警告", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		//错误处理 输入不合法
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher matcher = pattern.matcher(serverPortStr);
		if (!matcher.matches())
		{
			JOptionPane.showMessageDialog(mMainPanel, "端口号必须为数字", "警告", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		int serverPort = Integer.parseInt(serverPortStr);
		//错误处理 端口号范围错误
		if (serverPort < 1 || serverPort > 65535)
		{
			JOptionPane.showMessageDialog(mMainPanel, "端口号必须指定为1~65535", "警告", JOptionPane.WARNING_MESSAGE);
			return false;
		}

		return true;
	}

	private boolean contextPathTextFieldCheck(String contextPathStr)
	{
		//错误处理 应用路径不能为空
		if ("".equals(contextPathStr))
		{
			JOptionPane.showMessageDialog(mMainPanel, "ContextPath不能为空", "警告", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		//错误处理 应用路径不合法
		if(!contextPathStr.startsWith("/"))
		{
			JOptionPane.showMessageDialog(mMainPanel, "ContextPath格式错误", "警告", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	class ServiceConfigPanel extends JPanel
	{
		private JTextField urlTextField;
		private JTextField contentTypeTextField;
		private JTextArea respTextArea;

		private JButton deleteButton = new JButton("删除");

		ServiceConfigPanel()
		{
			super();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			urlTextField = new JTextField();
			urlTextField.setEditable(false);
			urlTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE,urlTextField.getHeight()));
			contentTypeTextField = new JTextField();
			contentTypeTextField.setEditable(false);
			contentTypeTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE,contentTypeTextField.getHeight()));
			respTextArea = new JTextArea();
			respTextArea.setRows(5);
			respTextArea.setEditable(false);
			add(urlTextField);
			add(contentTypeTextField);
			add(respTextArea);
			JPanel btnPanel = new JPanel();
			btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			btnPanel.add(deleteButton);
			add(btnPanel);

			deleteButton.addActionListener((e) -> {
				//删除服务器配置
				MockServer.deleteServerURLConfig(urlTextField.getText());
				MockServer.deleteServerContentTypeConfig(urlTextField.getText());
				//销毁ServiceConfigPanel
				ServiceConfigPanel.this.getParent().remove(ServiceConfigPanel.this);
				//重绘
				SwingUtilities.updateComponentTreeUI(MainForm.this.mScrollPaneInnerPanel);
			});
		}

		JTextField getUrlTextField()
		{
			return urlTextField;
		}

		JTextArea getRespTextArea()
		{
			return respTextArea;
		}

		JTextField getContentTypeTextField()
		{
			return contentTypeTextField;
		}
	}

	class AddConfigDialog extends JDialog
	{
		private JTextField urlTextField = new JTextField();
		private JTextArea respTextArea = new JTextArea();
		private JComboBox<String> contentTypeComboBox = new JComboBox<>();

		private JButton confirmButton = new JButton("确认");
		private JButton cancelButton = new JButton("取消");

		AddConfigDialog(JFrame parent)
		{
			super(parent, "添加配置", true);
			setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

			JPanel urlTextFieldPanel = new JPanel();
			urlTextFieldPanel.setLayout(new BorderLayout());
			urlTextFieldPanel.add(new JLabel("URL"), BorderLayout.WEST);
			urlTextFieldPanel.add(urlTextField, BorderLayout.CENTER);
			add(urlTextFieldPanel);

			JPanel respTextAreaPanel = new JPanel();
			respTextAreaPanel.setLayout(new BorderLayout());
			respTextAreaPanel.add(new JLabel("响应信息"), BorderLayout.NORTH);
			JScrollPane respTextAreaHolder = new JScrollPane();
			respTextAreaPanel.add(respTextAreaHolder, BorderLayout.CENTER);
			respTextAreaHolder.setViewportView(respTextArea);
			respTextArea.setRows(5);
			add(respTextAreaPanel);

			contentTypeComboBox.addItem("text/plain");
			contentTypeComboBox.addItem("application/json");
			contentTypeComboBox.addItem("application/xml");

			JPanel contentTypeComboPanel = new JPanel();
			contentTypeComboPanel.setLayout(new BorderLayout());
			contentTypeComboPanel.add(new JLabel("数据类型"), BorderLayout.WEST);
			contentTypeComboPanel.add(contentTypeComboBox, BorderLayout.CENTER);
			add(contentTypeComboPanel);

			JPanel btnPanel = new JPanel();
			btnPanel.add(confirmButton);
			btnPanel.add(cancelButton);
			add(btnPanel);

			confirmButton.addActionListener((e) -> {
				String urlStr = urlTextField.getText();
				String respStr = respTextArea.getText();
				String contentTypeStr = "text/plain";
				if(contentTypeComboBox.getSelectedItem() != null)
				{
					contentTypeStr = contentTypeComboBox.getSelectedItem().toString();
				}

				if(urlTextFieldCheck(urlStr))
				{
					//创建ServiceConfigPanel
					ServiceConfigPanel serviceConfigPanel = new ServiceConfigPanel();
					serviceConfigPanel.getUrlTextField().setText(urlStr);
					serviceConfigPanel.getContentTypeTextField().setText(contentTypeStr);
					serviceConfigPanel.getRespTextArea().setText(respStr);
					MainForm.this.mScrollPaneInnerPanel.add(serviceConfigPanel);
					//更新UI
					SwingUtilities.updateComponentTreeUI(MainForm.this.mScrollPaneInnerPanel);
					JScrollBar jScrollBar = MainForm.this.mServiceConfigScrollPane.getVerticalScrollBar();
					jScrollBar.setValue(jScrollBar.getMaximum());
					//加载新配置
					MockServer.addServerURLConfig(urlStr, respStr);
					MockServer.addServerContentTypeConfig(urlStr, contentTypeStr);
					//关闭自己
					AddConfigDialog.this.dispose();
				}
			});

			cancelButton.addActionListener((e) -> dispose());

			pack();
			setBounds(0, 0, 320, 220);

			//设置窗口出现在屏幕中间
			int windowWidth = getWidth();
			int windowHeight = getHeight();
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			int screenWidth = screenSize.width;
			int screenHeight = screenSize.height;
			setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);
		}

		private boolean urlTextFieldCheck(String urlStr)
		{
			if("".equals(urlStr))
			{
				JOptionPane.showMessageDialog(mMainPanel, "URL不能为空", "警告", JOptionPane.WARNING_MESSAGE);
				return false;
			}
			if(!urlStr.startsWith("/"))
			{
				JOptionPane.showMessageDialog(mMainPanel, "URL格式错误", "警告", JOptionPane.WARNING_MESSAGE);
				return false;
			}
			return true;
		}
	}
}
