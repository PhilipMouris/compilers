import java.util.ArrayList;

public class LRE {
	public static String LRE(String grammar) {
		String[] rules = grammar.split(";");
		String newRules = "";
		for (String rule : rules) {
			for (String previousRule : newRules.split(";")) {
				if (previousRule.equals("") || previousRule.charAt(0) == rule.charAt(0) || previousRule.charAt(1) == '\'')
					continue;
				else
					rule = replaceProductions(rule, previousRule.split(","));
			}
			newRules += eliminateImmediateLeftRecursion(rule);
		}
		return newRules.substring(0, newRules.length()-1);
	}
	
	public static String replaceProductions(String rule, String[] productions) {
		ArrayList<String> modifiedRule = new ArrayList<String>();
		modifiedRule.add(rule.split(",")[0]);
		for (int i = 1; i<rule.split(",").length; i++) {
			if (rule.split(",")[i].charAt(0) != productions[0].charAt(0))
				modifiedRule.add(rule.split(",")[i]);
			else
				for(int j = 1; j< productions.length; j++)
					modifiedRule.add(productions[j]+rule.split(",")[i].substring(1));
		}
		return String.join(",", modifiedRule);
	}
	
	public static String eliminateImmediateLeftRecursion(String rule) {
		String newRule = "";
		String modifiedRule = "";
		String[] splitRule = rule.split(",");
		String variable = splitRule[0];
		String newVariable = variable + "'";
		newRule += newVariable;
		modifiedRule += variable;
		for (int i = 1; i < splitRule.length; i++) {
			if(splitRule[i].charAt(0) == variable.charAt(0))
				newRule += "," + splitRule[i].substring(1) + newVariable;
			else
				modifiedRule += "," + splitRule[i] + newVariable;
		}
		if (newRule.equals(newVariable)) {
			return rule + ";";
		}
		newRule += ",e";
		return modifiedRule + ";" + newRule + ";";
	}
	
	public static void main(String[] args) {
//		System.out.println(LRE("S,cSa,iS,m"));
//		System.out.println(LRE("S,SdS,SzS,x"));
//		System.out.println(LRE("S,SdK,K;K,KzW,W;W,uv"));
//		System.out.println(LRE("S,LW,Wd;L,SW,LS,m;W,SL,m"));
//		System.out.println(LRE("S,SmT,Sv,T,u;T,vSu,ivKu,i;K,SdK,S"));
		System.out.println(LRE("S,EJ,Jd;E,SJ,ES,c;J,SE,c"));
	}
}
