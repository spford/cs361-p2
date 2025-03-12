package fa.nfa;

import java.util.*;

public class NFA implements NFAInterface {

    private LinkedHashSet<Character> sigma;
    private HashMap<String, NFAState> states;
    private TreeSet<String> finalStates;
    private String startState;

    public NFA() {
        this.sigma = new LinkedHashSet<>();
        sigma.add('e'); //Fill with epsilon(e) for NFA purposes
        this.states = new HashMap<>();
        this.finalStates = new TreeSet<>();
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
            NFAState state = states.get(s.getName());
            for (Character c : state.transitions.keySet()) {
                if (c.equals('e')) {
                    closure.add(state);
                }
            }
        }
        return closure;
    }

    @Override
    public int maxCopies(String s) {
        int copies = 0;
        NFAState currentState = states.get(startState);
        for (Character c : s.toCharArray()) {
            if (sigma.contains(c)) {
                for (Character c2 : currentState.transitions.keySet()) {
                    if (c.equals(c2) || c2.equals('e')) {
                        copies++;
                    }
                }
            }
        }
        return copies;
    }

    @Override
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb) {
        if( states.containsKey(fromState) && sigma.contains(onSymb) ) {
            NFAState fromThisState = states.get(fromState);
            for(String from : toStates) {
                if(states.containsKey(from)) {
                    fromThisState.transition(onSymb, states.get(from));
                }
                else { return false; }
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
            newState.transition('e', newState);
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
        NFAState currentState = states.get(startState);
        Stack<NFAState> stack = new Stack<>();
        for (Character c : s.toCharArray()) {
            if (!sigma.contains(c)) { return false; }
            for (Character c2 : currentState.transitions.keySet()) {
                if (c.equals(c2) || c2.equals('e')) {
                    List transitionList = (List) currentState.transitions.get(c2);
                    for ( int i = 0; i < transitionList.size(); i++ ) {
                        stack.add((NFAState) transitionList.get(i));
                    }
                }
            }
        }
        for (NFAState nfaState : stack) {
            if (finalStates.contains(nfaState.getName())) {
                return true;
            }
        }
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
