import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class NFA {
	public Set<String> states;
	public Set<String> alphabet;
	public HashMap<String, String> transitionFunction;
	public String startState;
	public Set<String> acceptStates;
	
	public NFA(String nfaDescription) {
		this.transitionFunction = new HashMap<String, String>();
		ArrayList<String> combinedStates = new ArrayList<String>();
		this.startState = orderAndRemoveDuplicates(createStartState(nfaDescription));
		System.out.println(this.startState);
		combinedStates.add(this.startState);
		int index = 0;
		while(index < combinedStates.size()) {
			String combinedState = combinedStates.get(index);
			String zeroTransition = "";
			for (String singleState: combinedState.split(",")) {
				zeroTransition += nextState(nfaDescription, singleState, "0");
			}
			String oneTransition = "";
			for (String singleState: combinedState.split(",")) {
				oneTransition += nextState(nfaDescription, singleState, "1");
			}
			zeroTransition = orderAndRemoveDuplicates(zeroTransition);
			oneTransition = orderAndRemoveDuplicates(oneTransition);
			if (zeroTransition.equals(""))
				zeroTransition = "dead";
			if (oneTransition.equals(""))
				oneTransition = "dead";
			this.transitionFunction.put(combinedState, zeroTransition + ";" + oneTransition);
			if (!this.transitionFunction.containsKey(zeroTransition))
				combinedStates.add(zeroTransition);
			if (!this.transitionFunction.containsKey(oneTransition))
				combinedStates.add(oneTransition);
			index++;
		}
		this.transitionFunction.entrySet().forEach(entry -> {
		    System.out.println(entry.getKey() + " " + entry.getValue());
		});
		this.alphabet = new HashSet<String>();
		alphabet.add("0");
		alphabet.add("1");
		this.states = this.transitionFunction.keySet();
		System.out.println(this.states);
		this.acceptStates = new HashSet<String>();
		for (String state: this.states)
			for (String acceptState: nfaDescription.split("#")[3].split(","))
				if(state.contains(acceptState)) {
					this.acceptStates.add(state);
					break;
				}
		System.out.println(this.acceptStates);
	}
	
	public String orderAndRemoveDuplicates(String state) {
		HashSet<String> transformedState = new HashSet<String>();
		for (String singleState: state.split(","))
			transformedState.add(singleState);
		return String.join(",", transformedState);
	}
	
	public String nextState(String nfaDescription, String currentState, String operator) {
		String zeroTransitions = nfaDescription.split("#")[0];
		String oneTransitions = nfaDescription.split("#")[1];
		String epsilonTransitions = nfaDescription.split("#")[2];
		String nextState = "";
		if (operator.equals("0"))
			for (String transition: zeroTransitions.split(";"))
				if (transition.split(",")[0].equals(currentState)) {
					nextState += transition.split(",")[1] + ",";
					nextState += nextState(nfaDescription, transition.split(",")[1], "epsilon");
				}
		if (operator.equals("1"))
			for (String transition: oneTransitions.split(";"))
				if (transition.split(",")[0].equals(currentState)) {
					nextState += transition.split(",")[1] + ",";
					nextState += nextState(nfaDescription, transition.split(",")[1], "epsilon");
				}
		if (operator.equals("epsilon"))
			for (String transition: epsilonTransitions.split(";"))
				if (transition.split(",")[0].equals(currentState)) {
					nextState += transition.split(",")[1] + ",";
					nextState += nextState(nfaDescription, transition.split(",")[1], "epsilon");
				}
		return nextState;
	}
	
	public String createStartState(String nfaDescription) {
//		String startState = "0";
//		for (String epsilonTransition: nfaDescription.split("#")[2].split(";"))
//			if (epsilonTransition.split(",")[0].equals("0"))
//				startState += "," + epsilonTransition.split(",")[1];
		return "0," + nextState(nfaDescription, "0", "epsilon");
	}
	
	public boolean run(String input) {
		if (input.equals(""))
			return this.acceptStates.contains(this.startState);
		String[] symbols = input.split("");
		String currentState = this.startState;
		for (String symbol: symbols) {
			currentState = this.transitionFunction.get(currentState).split(";")[Integer.parseInt(symbol)];
		}
		return this.acceptStates.contains(currentState);
	}
	
	public static void main(String args[]) {
		NFA test = new NFA("2,2;3,4#0,1;2,2#1,2;2,3#2");
		System.out.println(test.run("011"));
		System.out.println(test.run("000"));
		System.out.println(test.run("0011"));
		System.out.println(test.run("111"));
		System.out.println(test.run("101"));
		System.out.println("-------");
		NFA test2 = new NFA("0,0;0,1;1,2;3,3#0,0;2,3#0,1#2");
		System.out.println(test2.run("0011"));
		System.out.println(test2.run("100"));
		System.out.println(test2.run("10"));
		System.out.println(test2.run("11"));
		System.out.println(test2.run("010"));
		
		
		
	}
}
