package fa.nfa;

import fa.State;
import java.util.*;

public class NFA implements NFAInterface {

    private LinkedHashSet<Character> sigma;
    private HashMap<String, NFAState> states;
    private TreeSet<String> finalStates;
    private String startState;

    public NFA() {
        this.sigma = new LinkedHashSet<>();
        sigma.add('e');
        this.states = new HashMap<>();
        this.finalStates = new TreeSet();
        this.startState = null;
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return Set.of();
    }

    @Override
    public Set<NFAState> eClosure(NFAState s) {
        Set<NFAState> closure = new HashSet<>();
        if (states.containsKey(s.getName())) {
            for (Character c : s.transitions.keySet()) {
                if (c.equals('e')) {
                    NFAState nfaState = states.get(s.transitions.get(c).toString());
                    closure.add(nfaState);
                }
            }
            closure.add(s);
        }
        return closure;
    }

    @Override
    public int maxCopies(String s) {
        return 0;
    }

    @Override
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb) {
        if( states.containsKey(fromState) && sigma.contains(onSymb) ) {
            NFAState fromThisState = states.get(fromState);
            for(String from : toStates) {
                if(states.containsKey(from)) {
                    fromThisState.transitions.putIfAbsent(onSymb, states.get(from));
                }
                else { return false;}
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isDFA() {
        return false;
    }

    @Override
    public boolean addState(String name) {
        if ( !states.containsKey(name) ) {
            NFAState newState = new NFAState(name);
            states.put(name, newState);
            return true;
        }
        return false;
    }

    @Override
    public boolean setFinal(String name) {
        if( states.containsKey(name) ) {
            finalStates.add(name);
            return true;
        }
        return false;
    }

    @Override
    public boolean setStart(String name) {
        if( states.containsKey(name) ) {
            startState = name;
            return true;
        }
        return false;
    }

    @Override
    public void addSigma(char symbol) {
        if( !sigma.contains(symbol) ) {
            sigma.add(symbol);
        }
    }

    @Override
    public boolean accepts(String s) {
        return false;
    }

    @Override
    public Set<Character> getSigma() {
        return sigma;
    }

    @Override
    public NFAState getState(String name) {
        if (states.containsKey(name)) {
            return states.get(name);
        }
        return null;
    }

    @Override
    public boolean isFinal(String name) {
        return finalStates.contains(name);
    }

    @Override
    public boolean isStart(String name) {
        return startState.contentEquals(name);
    }
}
