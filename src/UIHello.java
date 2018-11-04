import java.util.LinkedList;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class UIHello extends JFrame{
	JLabel label;
	JPanel boardPanel;
	JLabel newlabel;
	JLabel continuelabel;
	JLabel errnewlabel;
	JLabel errctnlabel;
	JTextField newtf;
	JTextField continuetf;
	JButton newprjbtn;
	JButton continueprjbtn;
	
	public UIHello() {
		
		// title label 
		label = new JLabel("欢迎使用建筑材料计算工具",  SwingConstants.CENTER);
		label.setFont(new Font("Serif", Font.PLAIN, 16));
		this.getContentPane().add(BorderLayout.NORTH, label);
		
		// main panel
		boardPanel = new JPanel();
//		boardPanel.setLayout(new GridLayout(8, 1));  // frame size 800,400
		boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.Y_AXIS));
		

		newlabel = new JLabel("请输入新的工程名（如：XX小区客户XXX）");
		boardPanel.add(newlabel);
		newtf = new JTextField(10);
		boardPanel.add(newtf);
		errnewlabel = new JLabel("");
		errnewlabel.setForeground(Color.red);
        errnewlabel.setFont(new Font("Courier New", Font.ITALIC, 12));
		boardPanel.add(errnewlabel);
		newprjbtn = new JButton("新建工作");
		boardPanel.add(newprjbtn);
		
		continuelabel = new JLabel("请输入您想要继续的工程名（如：XX小区客户XXX）");
		boardPanel.add(continuelabel);
		continuetf = new JTextField(10);
		boardPanel.add(continuetf);
		errctnlabel = new JLabel("");
		errctnlabel.setForeground(Color.red);
        errctnlabel.setFont(new Font("Courier New", Font.ITALIC, 12));
		boardPanel.add(errctnlabel);
		continueprjbtn = new JButton("继续上次工作");
		boardPanel.add(continueprjbtn);
		
		this.getContentPane().add(BorderLayout.CENTER, boardPanel);

		NewEvent newevent = new NewEvent();
		newprjbtn.addActionListener(newevent);
		
		ContinueEvent continueevent = new ContinueEvent();
		continueprjbtn.addActionListener(continueevent);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 200);
		this.setTitle("建筑材料计算工具");
		this.setVisible(true);
	}
	
	/*
	 * need validate file name used to store sheets
	 */
	public class NewEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// validate file name, if file already exists, give overwritten notice 
			String filename = newtf.getText();
			if (filename.isEmpty() || filename.length() == 0 || filename.replace(" ", "").length()==0) {
				errnewlabel.setText("文件名不能为空，或纯空格");
				return;
			}
			File f = new File(filename + "_计算书.xls");
			if (f.exists()) {
				errnewlabel.setText("文件名已存在，请在继续工作输入框中输入文件名以继续上次工作，或使用新的文件名");
				return;
			}
			// close window and open AddRecordUI
			errnewlabel.setText("");
			UIHello.super.dispose();
			new UIAddRecord(filename);
		}
	}
	
	/*
	 * need validate file name used to continue work
	 */
	public class ContinueEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				String filename = continuetf.getText();
				LinkedList<RecordRaw> recordsRaw = ReadExcel.getRecordFromExcel(filename + "_计算书.xls");				
				//if success in loading
				errctnlabel.setText("");
				UIHello.super.dispose();
				new UIAddRecord(recordsRaw, filename);
			} catch (Exception ex) {
				errctnlabel.setText("读取文件失败：文件名不存在（文件名不应包含后缀“_计算书.xls”）或xls文件遭不当修改");
				System.out.println("ERR: Fail to load excel file, could be either file name not exists, or xls has been changed unpropperly, details below:");
				ex.printStackTrace();
			}
		}
	}
}
