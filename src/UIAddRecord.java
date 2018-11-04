import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.LinkedList;

public class UIAddRecord extends JFrame{
	String filename;
	JPanel panel;
	JPanel topErrPanel;
	JLabel errGeneratingFileLabel;
	JLabel[] itemlabels = new JLabel[8];
	JLabel[] errorlabels = new JLabel[8];
	JTextField[] tfs = new JTextField[8];
	JButton clearbtn;
	JButton savebtn;
	JButton sumupbtn;
	JButton snqbtn;
	private LinkedList<RecordRaw> rawRecords;
	
	//------------------------Constructors-------------------------------
	public UIAddRecord(String filename) {
		this.filename = filename;
		this.rawRecords = new LinkedList<>();
		
		createAndShowFrame();
    }
	
	public UIAddRecord(LinkedList<RecordRaw> recordsRaw, String filename) {
		this.filename = filename;
		this.rawRecords = recordsRaw;
		
		createAndShowFrame();
	}
	
	//------------------------Event listeners-------------------------------
	public class ClearEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			clearForm();
		}
	}
	
	public class SaveEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			boolean vld0 = Validator.validateRoom(tfs[0], errorlabels[0]);
			boolean vld1 = Validator.validatePosition(tfs[1], errorlabels[1]);
			boolean vld2 = Validator.validateDirection(tfs[2], errorlabels[2]);
			boolean vld3 = Validator.validateBorX(tfs[3], errorlabels[3]);
			boolean vld4 = Validator.validateName(tfs[4], errorlabels[4]);
			boolean vld5 = Validator.validateType(tfs[5], errorlabels[5], tfs[3]);
			boolean vld6 = Validator.validatePadlen(tfs[6], errorlabels[6], tfs[3]);
			boolean vld7 = Validator.validateSize(tfs[7], errorlabels[7]);
			
			if (vld0 && vld1 && vld2 && vld3 && vld4 && vld5 && vld6 && vld7) {
				inputSave();
				clearForm();
			}
		}
	}
	
	public class SumUpEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				OutToExcel.outCheckList(rawRecords, filename);
				errGeneratingFileLabel.setText("");
				UIAddRecord.super.dispose();  // close AddRecord frame
				new UIAddPrice(rawRecords, filename);
			} catch (Exception ex) {
				errGeneratingFileLabel.setText("计算书生成失败，无法将记录写入xls文件");
				System.out.println("ERR: Failure happens when click on SumUp button on UIAddRecord Page, details below:");
				ex.printStackTrace();
			}
		}
	}
	
	public class SaveAndQuitEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				OutToExcel.outCheckList(rawRecords, filename);
				UIAddRecord.super.dispose();
			} catch (Exception ex) {
				errGeneratingFileLabel.setText("计算书生成失败，无法将记录写入xls文件");
				System.out.println("ERR: Failure happens when click on SaveAndQuit button on UIAddRecord Page, details below:");
				ex.printStackTrace();
			}
		}
	}
	
	//------------------------Functions-------------------------------
	public void createAndShowFrame() {
		// Top Panel which gives error message if any
		topErrPanel = new JPanel();
		topErrPanel.setLayout(new BoxLayout(topErrPanel, BoxLayout.Y_AXIS));
		errGeneratingFileLabel = new JLabel("");
		topErrPanel.add(errGeneratingFileLabel);

		// Main Panel
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		for (int i = 0; i < errorlabels.length; i++) {
            errorlabels[i] = new JLabel("");
            errorlabels[i].setForeground(Color.red);
            errorlabels[i].setFont(new Font("Courier New", Font.ITALIC, 12));
		}
		
		// add items
		itemlabels[0] = new JLabel("房间（东南卧室）");
		panel.add(itemlabels[0]);
		tfs[0] = new JTextField(10);
		panel.add(tfs[0]);
		panel.add(errorlabels[0]);
		
		itemlabels[1] = new JLabel("位置 （顶）");
		panel.add(itemlabels[1]);
		tfs[1] = new JTextField(10);
		panel.add(tfs[1]);
		panel.add(errorlabels[1]);
		
		itemlabels[2] = new JLabel("铺贴方向（东西铺贴）");
		panel.add(itemlabels[2]);
		tfs[2] = new JTextField(10);
		panel.add(tfs[2]);
		panel.add(errorlabels[2]);
		
		itemlabels[3] = new JLabel("板（b）或线（x）");
		panel.add(itemlabels[3]);
		tfs[3] = new JTextField(10);
		panel.add(tfs[3]);
		panel.add(errorlabels[3]);
		
		itemlabels[4] = new JLabel("产品名称（S-802）");
		panel.add(itemlabels[4]);
		tfs[4] = new JTextField(10);
		panel.add(tfs[4]);
		panel.add(errorlabels[4]);
		
		itemlabels[5] = new JLabel("毫米规格（300*3600）");
		panel.add(itemlabels[5]);
		tfs[5] = new JTextField(10);
		panel.add(tfs[5]);
		panel.add(errorlabels[5]);
		
		itemlabels[6] = new JLabel("单片长度（1800）");
		panel.add(itemlabels[6]);
		tfs[6] = new JTextField(10);
		panel.add(tfs[6]);
		panel.add(errorlabels[6]);
		
		itemlabels[7] = new JLabel("毫米尺寸（1000+2000*2-200/50）");
		panel.add(itemlabels[7]);
		tfs[7] = new JTextField(10);
		panel.add(tfs[7]);
		panel.add(errorlabels[7]);
		
		// add buttons
		savebtn = new JButton("保存当前条目");
		panel.add(savebtn);
		
		clearbtn = new JButton("清空当前条目");
		panel.add(clearbtn);
		
		sumupbtn = new JButton("生成计算书并合计");
		panel.add(sumupbtn);
		
		snqbtn = new JButton("生成计算书并退出");
		panel.add(snqbtn);
		
		// add button listener
		ClearEvent eclear = new ClearEvent();
		clearbtn.addActionListener(eclear);
		
		SaveEvent esave = new SaveEvent();
		savebtn.addActionListener(esave);
		
		SumUpEvent esumup = new SumUpEvent();
        sumupbtn.addActionListener(esumup);
        
        SaveAndQuitEvent esnq = new SaveAndQuitEvent();
        snqbtn.addActionListener(esnq);
        
        this.getContentPane().add(BorderLayout.NORTH, topErrPanel);
        this.getContentPane().add(BorderLayout.CENTER, panel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(700, 600);
		this.setTitle("建筑计算助手");
		this.setVisible(true);
	}
	
	public LinkedList<RecordRaw> getRecords() {
		return this.rawRecords;
	}
	
	public void inputSave() {
		RecordRaw record = new RecordRaw();
		record.room = tfs[0].getText();
		record.position = tfs[1].getText();
		record.direction = tfs[2].getText();
		record.borx = tfs[3].getText();
		record.name = tfs[4].getText();
		record.type = tfs[5].getText();
		
		String potentialpadlen = tfs[6].getText();
		if (potentialpadlen == null || potentialpadlen.length() == 0) record.padlen = 0;
		else record.padlen = Integer.parseInt(potentialpadlen);
		
		record.size = tfs[7].getText();
		rawRecords.add(record);
	}
	
	public void clearForm() {
		tfs[0].setText("");
		tfs[1].setText("");
		tfs[2].setText("");
		tfs[3].setText("");
		tfs[4].setText("");
		tfs[5].setText("");
		tfs[6].setText("");
		tfs[7].setText("");
		errorlabels[0].setText("");
		errorlabels[1].setText("");
		errorlabels[2].setText("");
		errorlabels[3].setText("");
		errorlabels[4].setText("");
		errorlabels[5].setText("");
		errorlabels[6].setText("");
		errorlabels[7].setText("");
	}
	
	
}