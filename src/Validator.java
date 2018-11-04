import javax.swing.*;


public class Validator {
	
	public Validator() {};

	/* 
	 * no need to worry about white space in type field
	 */
	
	public static boolean validateRoom(JTextField textField, JLabel errorLabel) {
		if (textField.getText().isEmpty() || textField.getText().replace(" ","").length() == 0) {
			errorLabel.setText("请输入房间名称");
			return false;
		}
		errorLabel.setText("");
		return true;
	}
	
	public static boolean validatePosition(JTextField textField, JLabel errorLabel) {
		if (textField.getText().isEmpty() || textField.getText().replace(" ","").length() == 0) {
			errorLabel.setText("请输入位置（顶或墙）");
			return false;
		}
		errorLabel.setText("");
		return true;
	}
	
	public static boolean validateDirection(JTextField textField, JLabel errorLabel) {
		errorLabel.setText("");
		return true;
	}
	
	public static boolean validateBorX(JTextField textField, JLabel errorLabel) {
		if (textField.getText().isEmpty() || textField.getText().replace(" ","").length() == 0) {
			errorLabel.setText("请输入b(板)或x(线)");
			return false;
		} else if (!textField.getText().equals("b") && !textField.getText().equals("x")) {
			// cannot be changed to add !euals "B" && !equals"X", because function validatePadlen depends on validateBorX that this field must be either b or x, cannot be B or X
			errorLabel.setText("输入只能为b或x (请确保输入无空格，字母小写)");
			return false;
		}
		errorLabel.setText("");
		return true;
	}
	
	public static boolean validateName(JTextField textField, JLabel errorLabel) {
		if (textField.getText().isEmpty() || textField.getText().replace(" ","").length() == 0) {
			errorLabel.setText("请输入产品名称");
			return false;
		} else if (!textField.getText().matches("[A-Z]+\\-\\d+")) {
			errorLabel.setText("请确保输入格式正确（无空格，字母全部大写，格式为 字母-数字）");
			return false;
		}
		errorLabel.setText("");
		return true;
	}
	
	public static boolean validateType(JTextField tfType, JLabel errorLabel, JTextField tfBorX) {
		if (tfType.getText().isEmpty() || tfType.getText().replace(" ","").length() == 0) {
			errorLabel.setText("请输入产品规格");
			return false;
		} else if (!tfType.getText().matches("\\d+\\*\\d+")) {
			errorLabel.setText("请确保输入格式正确（无空格，格式为 整数数字*整数数字）");
			return false;
		}
		if (tfBorX.getText().equals("b")) { // one more check if it's b
			int firstMul = Integer.parseInt(tfType.getText().split("\\*")[0]);
			if (firstMul < 100) {
				errorLabel.setText("第一个乘数不应小于100，毫米单位");
				return false;
			}
		}
		
		errorLabel.setText("");
		return true;
	}
	
	public static boolean validatePadlen(JTextField tfPadlen, JLabel errorLabel, JTextField tfBorX) {
		if (tfBorX.getText().equals("b") && (tfPadlen.getText().isEmpty() || tfPadlen.getText().replace(" ","").length() == 0)) { 
			errorLabel.setText("请输入单片长度");
			return false;
		} else if (tfBorX.getText().equals("x") && !tfPadlen.getText().isEmpty()) {
			errorLabel.setText("线不需要单片长度，请删除（确保无空格）");
			return false;
		} else if (tfBorX.getText().equals("b") && !tfPadlen.getText().matches("\\d+")) {
			errorLabel.setText("请确保单片长度格式正确，格式为 纯数字，无空格，若单片长度与原长度一致，也请填写单片长度");
			return false;
		} else if (tfBorX.getText().equals("b") && tfPadlen.getText().equals("0")) {
			errorLabel.setText("单片长度不能为零");
			return false;
		}
		errorLabel.setText("");
		return true;
	}
	
	public static boolean validateSize(JTextField textField, JLabel errorLabel) {
		if (textField.getText().isEmpty() || textField.getText().replace(" ","").length() == 0) {
			errorLabel.setText("请输入尺寸");
			return false;
		} else if (!textField.getText().matches("[0-9\\+\\-\\*\\/]+")) {
			// cannot detect double *, and result negative
			errorLabel.setText("请确保算式合法，不含括号，小数点，空格");
			return false;
		}
		errorLabel.setText("");
		return true;
	}
	
	public static boolean validatePrice(JTextField buytf, JTextField selltf, JLabel errorLabel) {
		if (buytf.getText().isEmpty() || buytf.getText().replaceAll(" ", "").length() == 0 || selltf.getText().isEmpty() || selltf.getText().replaceAll(" ", "").length() == 0) {
			errorLabel.setText("本行输入不能为空");
			return false;
		} else if ((!buytf.getText().matches("\\d+\\.\\d+") && !buytf.getText().matches("\\d+"))
				|| (!selltf.getText().matches("\\d+\\.\\d+") && !selltf.getText().matches("\\d+"))) {
			errorLabel.setText("本行输入格式有误，价格应为正数，无空格");
			return false;
		}
		
		errorLabel.setText("");
		return true;
	}
	
}
