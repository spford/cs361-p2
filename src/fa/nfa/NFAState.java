package fa.nfa;
import fa.State;
import java.util.HashMap;

public class NFAState extends State {

    protected HashMap<Character, NFAState> transitions;
    
    NFAState(String stateName) {
        super(stateName);
        this.transitions = new HashMap<>();
    }

    public void transition(char alphaChar, NFAState state) {
        transitions.putIfAbsent(alphaChar, state);
    }
}
