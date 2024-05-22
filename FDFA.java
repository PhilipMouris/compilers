import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class FDFA {
	public HashSet<Integer> states;
	public HashSet<Integer> alphabet;
	public String transitionFunction;
	public int startState = 0;
	public HashSet<Integer> acceptStates;
	public ArrayList<String> actions ;
	
	public FDFA(String description) {
		this.actions = new ArrayList<String>();
		this.states = new HashSet<Integer>();
		this.alphabet = new HashSet<Integer>();
		this.acceptStates = new HashSet<Integer>();
		alphabet.add(0);
		alphabet.add(1);
		this.transitionFunction = description.split("#")[0];
		String[] stringStates = this.transitionFunction.split("[,;]");
		for (int i = 0; i<stringStates.length;i ++) {
			if ((i+1)%4 != 0 )
				this.states.add(Integer.parseInt(stringStates[i]));
			else
				actions.add(stringStates[i]);
		}
		String[] stringAcceptStates = description.split("#")[1].split(",");
		for (String state: stringAcceptStates) {
			this.acceptStates.add(Integer.parseInt(state));
		}
	}
	
	public void run(String input) {
		int r = 0;
		int l = 0;
		Stack<Integer> statesStack= new Stack<>();
		String output = "";
		while(r < input.length() && l <input.length()) {
			statesStack.push(this.startState);
			while (l<input.length()) {
				int transition = Integer.parseInt(input.charAt(l) + "");
				statesStack.push(Integer.parseInt(this.transitionFunction.split(";")[statesStack.peek()].split(",")[transition+1]));
				l++;
			}
			int stackTop = statesStack.peek();
			while(!statesStack.empty() && !this.acceptStates.contains(statesStack.peek())) {
				statesStack.pop();
				l--;
			}
			if(!statesStack.empty() && this.acceptStates.contains(statesStack.peek())) {
				output += actions.get(statesStack.peek());
				statesStack.clear();
				r = l;
			}
			else {
				output += actions.get(stackTop);
				break;
			}
		}
		System.out.println(output);
	}
	
	public static void main(String[] args) {
		FDFA test = new FDFA("0,1,2,A;1,2,4,B;2,4,3,C;3,4,4,D;4,5,4,E;5,5,4,F#3,5");
		test.run("1010");
		test.run("00101");
		test.run("1");
		test.run("001101");
		test.run("0010");
		System.out.println("------------------------");
		FDFA test2 = new FDFA("0,1,3,A;1,0,2,B;2,3,4,C;3,4,1,D;4,4,2,E#2,4");
		test2.run("0010");
		test2.run("00010");
		test2.run("101");
		test2.run("00");
		test2.run("10101");
	}
}
