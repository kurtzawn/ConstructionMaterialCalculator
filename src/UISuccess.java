import java.awt.*;
import javax.swing.*;
import java.awt.event.*;



public class UISuccess extends JFrame{
	JLabel congratlabel;
	JButton exitbtn;
	public UISuccess() {
		congratlabel = new JLabel("恭喜！结果计算完毕！", SwingConstants.CENTER);
		
		exitbtn = new JButton("退出程序");
		
		QuitEvent quitEvent = new QuitEvent();
		exitbtn.addActionListener(quitEvent);
		
		this.getContentPane().add(BorderLayout.CENTER, congratlabel);
		this.getContentPane().add(BorderLayout.SOUTH, exitbtn);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800, 500);
		this.setTitle("建筑计算助手");
		this.setVisible(true);
	}
	
	public class QuitEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				UISuccess.super.dispose();
			} catch(Exception ex) {}
		}
	}
}
