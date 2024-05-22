import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class FirstAndFollow {
	public static String First(String input) {
		LinkedHashMap<String, String> rules = new LinkedHashMap<String, String>();
		for (String rule:input.split(";")) {
			rules.put(rule.split(",")[0], rule.substring(2));
		}
		String output = "";
		for (String variable:rules.keySet()) {
			output += variable + "," + computeFirst(rules, variable).toString() + ";";
		}
		return output.substring(0, output.length()-1);
	}
	
	public static HashSet<String> computeFirst(LinkedHashMap<String, String> rules, String symbol) {
		HashSet<String> first = new HashSet<String>();
		if (symbol.charAt(0) >= 97) {
			first.add(symbol);
			return first;
		}
		boolean change = true;
		while(change) {
			change = false;
			for (String rule:rules.get(symbol).split(",")) {
				if (intersectionOfFirsts(rules, rule).contains("e")) {
					if (!first.contains("e")) {
						first.add("e");
						change = true;
					}
				}
				else {
					for (int i = 1; i<=rule.length(); i++) {
						if ((i == 1) || intersectionOfFirsts(rules, rule.substring(0, i-1)).contains("e")) {
							HashSet<String> currentFirst = computeFirst(rules, rule.charAt(i-1)+"");
							currentFirst.remove("e");
							if (!first.containsAll(currentFirst)) {
								first.addAll(currentFirst);
								change = true;
							}
						}
					}
				}
			}
		}
		return first;
	}
	
	public static HashSet<String> intersectionOfFirsts(LinkedHashMap<String, String> rules, String rule) {
		HashSet<String> intersection = computeFirst(rules, rule.charAt(0)+"");
		for(int i = 1;i<rule.length();i++) {
			intersection.retainAll(computeFirst(rules,rule.charAt(i)+""));
		}
		return intersection;
	}
	
	public static void main(String[] args) {
		System.out.println(First("S,ACB,CbB,Ba;A,da,BC;B,g,e;C,h,e"));
	}
}
