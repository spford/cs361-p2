package fa.nfa;
import fa.State;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class NFAState extends State {

    protected HashMap<Character, List> transitions;
    
    NFAState(String stateName) {
        super(stateName);
        this.transitions = new HashMap<>();
    }

    public void transition(char alphaChar, NFAState state) {
        if (transitions.containsKey(alphaChar)) {
            List list = transitions.get(alphaChar);
            list.add(state.getName());
            transitions.put(alphaChar, list);
        } else {
            List list = new List();
            list.add(state.getName());
            transitions.put(alphaChar, list);
        }
    }
}
