import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.LinkedList;

public class UIAddPrice extends JFrame {
	String filename;
	JPanel boardPanel;
	JLabel[] nameslabel;
	JLabel[] typeslabel;
	JLabel[] errorslabel;
	JTextField[] buypricestf;
	JTextField[] sellpricestf; 
	JButton outputResbtn;
	JScrollPane qScroller;
	
	LinkedList<RecordRaw> recordsRaw;
	LinkedList<RecordSum> recordsSum;
	
	JLabel lockernamelabel;
	JLabel lockertypelabel;
	JLabel lockererrorlabel;
	JTextField lockerpricebuytf;
	JTextField lockerpriceselltf;
	RecordSum locker; //卡扣数

	JLabel errGeneratingFileLabel;
	
	public UIAddPrice(LinkedList<RecordRaw> rawrecords, String filename) {
		this.filename = filename;
		this.recordsRaw = rawrecords;
		this.recordsSum = CalculatorA.calRecordsSum(rawrecords);
		CalculatorA.sortRecordsSum(recordsSum);
		this.locker = CalculatorA.calLocker(this.recordsSum);
		
		int qty = recordsSum.size();
		
		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(qty+2, 5));
		
		nameslabel = new JLabel[qty];
		typeslabel = new JLabel[qty];
		buypricestf = new JTextField[qty];
		sellpricestf = new JTextField[qty];
		errorslabel = new JLabel[qty];
		
		boardPanel.add(new JLabel("型号"));
		boardPanel.add(new JLabel("规格"));
		boardPanel.add(new JLabel("进货价"));
		boardPanel.add(new JLabel("报价"));
		boardPanel.add(new JLabel(""));
		
		for (int i = 0; i < qty; i++) {
			nameslabel[i] = new JLabel(recordsSum.get(i).name);
			boardPanel.add(nameslabel[i]);
			typeslabel[i] = new JLabel(recordsSum.get(i).type);
			boardPanel.add(typeslabel[i]);
			buypricestf[i] = new JTextField(5);
			boardPanel.add(buypricestf[i]);
			sellpricestf[i] = new JTextField(5);
			boardPanel.add(sellpricestf[i]);
			errorslabel[i] = new JLabel("");
			errorslabel[i].setForeground(Color.red);
//	        errorslabel[i].setFont(new Font("Courier New", Font.ITALIC, 12));
			boardPanel.add(errorslabel[i]);
		}
		
		
		lockernamelabel = new JLabel("卡扣");
		boardPanel.add(lockernamelabel);
		lockertypelabel = new JLabel("");
		boardPanel.add(lockertypelabel);
		lockerpricebuytf = new JTextField(5);
		boardPanel.add(lockerpricebuytf);
		lockerpriceselltf = new JTextField(5);
		boardPanel.add(lockerpriceselltf);
		lockererrorlabel = new JLabel("");
		lockererrorlabel.setForeground(Color.red);
//        lockererrorlabel.setFont(new Font("Courier New", Font.ITALIC, 12));
		boardPanel.add(lockererrorlabel);

		errGeneratingFileLabel = new JLabel("");
		errGeneratingFileLabel.setForeground(Color.red);
//		errGeneratingFileLabel.setFont(new Font("Courier New", Font.ITALIC, 12));
		
		
		
		
		qScroller = new JScrollPane(boardPanel);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		outputResbtn = new JButton("输出结果");
		
		GetFormEvent eGetForm = new GetFormEvent();
		outputResbtn.addActionListener(eGetForm);
		
		this.getContentPane().add(BorderLayout.CENTER, qScroller);
		this.getContentPane().add(BorderLayout.SOUTH, outputResbtn);
		this.getContentPane().add(BorderLayout.NORTH, errGeneratingFileLabel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1500, 500);
		this.setTitle("建筑计算助手");
		this.setVisible(true);
	}
	
	/*
	 * check if price is valid, should be double with no spaces, and no alphabets
	 */
	public class GetFormEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int n = recordsSum.size();
			// validate
			boolean valid = true;
			for (int i = 0; i < n; i++) {
				valid = valid & Validator.validatePrice(buypricestf[i], sellpricestf[i], errorslabel[i]);
			}
			valid = valid & Validator.validatePrice(lockerpricebuytf, lockerpriceselltf, lockererrorlabel);
			if (!valid) return;
			// input buying prices to recordsSum
			double allPricesBuy = 0;
			double allPricesSell = 0;
			for (int i = 0; i < n; i++) {
				RecordSum curr = recordsSum.get(i);
				double spricebuy = Double.parseDouble(buypricestf[i].getText());
				double spricesell = Double.parseDouble(sellpricestf[i].getText());
				curr.pricebuy = spricebuy;
				curr.totalpricebuy = spricebuy * curr.area;
				curr.pricesell = spricesell;
				curr.totalpricesell = spricesell * curr.area;
				allPricesBuy += curr.totalpricebuy;
				allPricesSell += curr.totalpricesell;
			}
			double spricebuy = Double.parseDouble(lockerpricebuytf.getText());
			locker.pricebuy = spricebuy;
			locker.totalpricebuy = spricebuy * locker.quantity;
			allPricesBuy += locker.totalpricebuy; 
			double spricesell = Double.parseDouble(lockerpriceselltf.getText());
			locker.pricesell = spricesell;
			locker.totalpricesell = spricesell * locker.quantity;
			allPricesSell += locker.totalpricesell; 
			
			try {
				// call outToExcel to output three forms
				OutToExcel.outDetailList(recordsRaw, filename);
				OutToExcel.outSumListBuying(recordsSum, locker, allPricesBuy, filename);
				OutToExcel.outSumListSelling(recordsSum, locker, allPricesSell, filename);
				// close current window
				UIAddPrice.super.dispose();
				// show success window
				new UISuccess();
			} catch(Exception ex) {
				errGeneratingFileLabel.setText("生成报价表，进货表，施工详单失败，无法将相应数据写入三张表");
				System.out.println("ERR: Failure happens when clicking on outputResbtn in UIAddPrice Page, details below:");
				ex.printStackTrace();
			}
		}
	}
	
}
