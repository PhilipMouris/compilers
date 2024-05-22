import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Stack;

public class LL1 {
	
	public static String Parse(String grammar, String input) {
		String rules = grammar.split("#")[0];
		String firsts = grammar.split("#")[1];
		String follows = grammar.split("#")[2];
		ArrayList<String> alphabet = new ArrayList<String>();
		for (int i = 0;i<rules.length();i++) {
			if(rules.charAt(i) >= 97 && rules.charAt(i) <= 122)
				alphabet.add(rules.charAt(i)+"");
		}
		Set<String> alphabetSet = new HashSet<>(alphabet);
		alphabet.clear();
		alphabet.addAll(alphabetSet);
		alphabet.sort(null);
		alphabet.remove("e");
		LinkedHashMap<String, LinkedHashMap<String, String>> parsingTable = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		for(String rule : rules.split(";")) {
			parsingTable.put(rule.substring(0,1), new LinkedHashMap<String, String>());
			for (String letter:alphabet) {
				parsingTable.get(rule.substring(0,1)).put(letter, null);
			}
			parsingTable.get(rule.substring(0,1)).put("$", null);
		}
		for(int i = 0;i<firsts.split(";").length;i++) {
			for (int j = 1;j<firsts.split(";")[i].split(",").length;j++) {
				if(!firsts.split(";")[i].split(",")[j].equals("e")) {
					for (int k = 0;k<firsts.split(";")[i].split(",")[j].length();k++)
						parsingTable.get(firsts.split(";")[i].split(",")[0]).put(firsts.split(";")[i].split(",")[j].charAt(k)+"", rules.split(";")[i].split(",")[j]);
				}
				else {
					String follow = follows.split(";")[i].split(",")[1];
					for (char singleFollow:follow.toCharArray()) {
						parsingTable.get(firsts.split(";")[i].split(",")[0]).put(singleFollow+"", "e");
					}
				}
			}
		}
//		System.out.println(parsingTable);
		Stack<String> pda = new Stack<>();
		pda.push("$");
		pda.push("S");
		ArrayList<String> result = new ArrayList<String>();
		result.add("S");
		input += "$";
		int inputCounter = 0;
		while(!pda.empty()) {
//			System.out.println(result.toString());
			String top = pda.pop();
			if(top.equals(input.charAt(inputCounter)+"")) {
				inputCounter++;
				continue;
			}
			if (parsingTable.get(top) == null) {
				result.add("ERROR");
				break;
			}
			String rule = parsingTable.get(top).get(input.charAt(inputCounter)+"");
			if (rule == null) {
				result.add("ERROR");
				break;
			}
			if (rule.equals("e")) {
				result.add(result.get(result.size()-1).replaceFirst("[A-Z]", ""));
				continue;
			}
			for (int i = rule.length()-1;i>=0;i--) {
				pda.push(rule.charAt(i)+"");
			}
			result.add(result.get(result.size()-1).replaceFirst("[A-Z]", rule));
		}
		String output = result.toString().replaceAll(" ", "");
		return output.substring(1,output.length()-1);
	}
	
	public static void main(String[] args) {
		System.out.println(Parse("S,ipD,oSmDc,e;D,VmS,LxS;V,n,e;L,oSc,e#S,i,o,e;D,mn,ox;V,n,e;L,o,e#S,cm$;D,cm$;V,m;L,x", "oo"));
	}

}
