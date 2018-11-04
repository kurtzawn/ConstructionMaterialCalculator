import java.util.Map;
import java.util.Stack;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



public class CalculatorA {

	
	public CalculatorA() {}
	
	/*
	 * Assign quantity field to each raw record
	 * And sum up records with same name, borx, and type fields to generate recordsSum 
	 */
	public static LinkedList<RecordSum> calRecordsSum(LinkedList<RecordRaw> recordsRaw) {
		LinkedList<RecordSum> recordsSum = new LinkedList<>();
		Map<String, List<Integer>> map = new HashMap<>();
		int n = recordsRaw.size();
		
		// calculate quantity for each raw record
		for (int i = 0; i < n; i++) {
			RecordRaw rawrecord = recordsRaw.get(i);
			
			// calculate total len
			double nom = calSize(rawrecord.size);
			
			if (rawrecord.borx.equals("b")) { // if negative, report error that might be because input "type" field not in mm
				int denom = Integer.parseInt(rawrecord.type.split("\\*")[0]) - 10;
				if (denom < 0) {
					System.out.println("ERR: illegal record type field input before, make sure metric should be in mm");
					continue;
				}
				int fixer = (int) Math.floor(Integer.parseInt(rawrecord.type.split("\\*")[1])*1.0/rawrecord.padlen);
				rawrecord.quantity = (int) Math.ceil(nom*1.0/denom/fixer);
				String key = rawrecord.name + "$" + rawrecord.borx + "$" + rawrecord.type;
				if (!map.containsKey(key)) map.put(key, new LinkedList<>());
				map.get(key).add(i);
				
			} else if (rawrecord.borx.equals("x")) {
				int denom = Integer.parseInt(rawrecord.type.split("\\*")[1]);
				rawrecord.quantity = (int) Math.ceil(nom*1.0/denom);
				String key = rawrecord.name + "$" + rawrecord.borx + "$" + rawrecord.type;
				if (!map.containsKey(key)) map.put(key, new LinkedList<>());
				map.get(key).add(i);
			} else {
				System.out.println("ERR: illegal record borx field input before");
			}
		}
		
		// sum up all same name and type raw record, put other fields together in demo field, store in RecordsSum
		for (Map.Entry<String, List<Integer>> entry: map.entrySet()) {
			RecordSum recordSum = new RecordSum();
			String key = entry.getKey();
			String[] strs = key.split("\\$");
			recordSum.name = strs[0];
			recordSum.borx = strs[1];
			recordSum.type = strs[2];
			double subarea = 0;
			if (strs[1].equals("b")) {
				subarea = Integer.parseInt(strs[2].split("\\*")[0]) * Integer.parseInt(strs[2].split("\\*")[1]) / 1000000.0;
			} else if (strs[1].equals("x")) {
				subarea = Integer.parseInt(strs[2].split("\\*")[1]) / 1000.0;
			} else {
				System.out.println("ERR: illegal borx field parsed from key value in sum-up-record-on-same-name-borx-and-type map");
			}
			int qty = 0; 
			String demoo = "";
			for (int i : entry.getValue()) {
				RecordRaw curr = recordsRaw.get(i);
				qty += curr.quantity;
				demoo += "+" + curr.room + curr.position + curr.direction + curr.quantity;
			}
			recordSum.quantity = qty;
			recordSum.demo = demoo.length() == 0 ? "" : demoo.substring(1);
			recordSum.area = subarea * qty;
			recordsSum.add(recordSum);
		}
		return recordsSum;
	}
	
	/*
	 * Calculate number of lockers needed
	 * MUST call CalculatorA.calRecordsSum() first to get recordsSum in order to use this function!!!!!
	 * locker.quantity stores floored quantity
	 */
	public static RecordSum calLocker(LinkedList<RecordSum> recordsSum) {
		RecordSum locker = new RecordSum();
		locker.name = "卡扣";
		double cnt = 0;
		for (RecordSum record: recordsSum) {
			if (record.borx.equals("b")) {
				int h = Integer.parseInt(record.type.split("\\*")[1]);
				cnt += h * record.quantity;
			}
		}
		locker.quantity = (int) Math.floor(cnt/600.0);
		
		return locker;
	}
	
	/*
	 * Given expression, calculate result value
	 */
	public static double calSize(String exp) {
		if (exp.charAt(0) != '-' && exp.charAt(0) != '+')
			exp = "+" + exp;
		Stack<Double> stack = new Stack<>();
		int len = exp.length();
		char sign = exp.charAt(0);
		int st = 1, end = 1;
		while (true) {
			while (end < len && 0 <= (exp.charAt(end) - '0') && (exp.charAt(end) - '0' <= 9)) end++;
			int num = Integer.parseInt(exp.substring(st, end));
			if (sign == '+') {
				stack.push(1.0 * num);
			} else if (sign == '-') {
				stack.push(-1.0 * num);
			} else if (sign == '*') {
				stack.push(1.0 * stack.pop() * num);
			} else if (sign == '/') {
				stack.push(1.0 * stack.pop() / num);
			}
			if (end >= len) break;
			sign = exp.charAt(end);
			st = end = end + 1;
		}
		double res = 0;
		while (!stack.isEmpty())
			res += stack.pop();
		
		return res;
		
	}
	
	/*
	 * Sort recordsSum according to type
	 */
	public static void sortRecordsSum(LinkedList<RecordSum> recordsSum) {
		Collections.sort(recordsSum, new Comparator<RecordSum>() {
			@Override
			public int compare(RecordSum r1, RecordSum r2) {
				int r1l = Integer.parseInt(r1.type.split("\\*")[0]);
				int r1r = Integer.parseInt(r1.type.split("\\*")[1]);
				int r2l = Integer.parseInt(r2.type.split("\\*")[0]);
				int r2r = Integer.parseInt(r2.type.split("\\*")[1]);
				return r1l-r2l == 0 ? r2r - r1r : r1l - r2l;
			}
		});
	}
}