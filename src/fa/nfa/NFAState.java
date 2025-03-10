package fa.nfa;
import fa.State;
import java.util.HashMap;

public class NFAState extends State {

    protected HashMap<Character, State> transitions;
    
    NFAState(String stateName) {
        super(stateName);
        this.transitions = new HashMap<>();
    }

    public void transition(char alphaChar, State state) {
        transitions.putIfAbsent(alphaChar, state);
    }
}
