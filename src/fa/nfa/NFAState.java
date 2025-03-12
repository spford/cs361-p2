package fa.nfa;
import fa.State;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class NFAState extends State {

    protected HashMap<Character, HashSet<NFAState>> transitions;
    
    NFAState(String stateName) {
        super(stateName);
        this.transitions = new HashMap<>();
    }

    public void transition(char alphaChar, NFAState state) {
        if (transitions.containsKey(alphaChar)) {
            HashSet<NFAState> set = transitions.get(alphaChar);
            set.add(state);
            transitions.put(alphaChar, set);
        } else {
            HashSet<NFAState> set = new HashSet<>();
            set.add(state);
            transitions.put(alphaChar, set);
        }
    }
}
