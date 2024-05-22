import java.util.HashSet;

public class DFA {
	public HashSet<Integer> states;
	public HashSet<Integer> alphabet;
	public String transitionFunction;
	public int startState = 0;
	public HashSet<Integer> acceptStates;
	
	public DFA(String description) {
		this.states = new HashSet<Integer>();
		this.alphabet = new HashSet<Integer>();
		this.acceptStates = new HashSet<Integer>();
		alphabet.add(0);
		alphabet.add(1);
		this.transitionFunction = description.split("#")[0];
		String[] stringStates = this.transitionFunction.split("[,;]");
		for (String state: stringStates) {
			this.states.add(Integer.parseInt(state));
		}
		String[] stringAcceptStates = description.split("#")[1].split(",");
		for (String state: stringAcceptStates) {
			this.acceptStates.add(Integer.parseInt(state));
		}
	}
	
	public boolean run(String input) {
		String[] symbols = input.split("");
		int currentState = this.startState;
		for(String symbol: symbols) {
			currentState = Integer.parseInt(this.transitionFunction.split(";")[currentState].split(",")[Integer.parseInt(symbol)+1]);
		}
		return this.acceptStates.contains(currentState);		
	}
	
	public static void main(String[] args) {
		DFA test = new DFA("0,0,1;1,2,1;2,0,3;3,3,3#1,3");
		System.out.println(test.run("01010100"));
	}
}
