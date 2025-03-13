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
        Stack<NFAState> stack = new Stack<>();

        stack.push(s);
        closure.add(s);

        while (!stack.isEmpty()) {
            NFAState nfaState = stack.pop();

            if (nfaState.transitions.containsKey('e')) {
                for (NFAState nextState : nfaState.transitions.get('e')) {
                    if(!closure.contains(nextState)) {
                        stack.push(nextState);
                        closure.add(nextState);
                    }
                }
            }
        }
        return closure;
    }

    @Override
    public int maxCopies(String s) {
        Set<NFAState> currentStates = eClosure(states.get(startState));
        int maxCopies = currentStates.size();

        for ( Character c : s.toCharArray() ) {
            if (!sigma.contains(c)) {
                continue;
            }

            Set<NFAState> nextStates = new HashSet<>();
            for (NFAState state : currentStates) {
                if (state.transitions.containsKey(c)) {
                    nextStates.addAll(state.transitions.get(c));
                }
            }

            currentStates.clear();
            for (NFAState nextState : nextStates) {
                currentStates.addAll(eClosure(nextState));
            }
            maxCopies = Math.max(maxCopies, currentStates.size());
        }
        return maxCopies;
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
        Set<NFAState> currentStates = eClosure(states.get(startState));

        for (Character c : s.toCharArray()) {
            if ( !sigma.contains(c) ) {
                return false;
            }

            Set<NFAState> nextStates = new HashSet<>();
            for (NFAState state : currentStates) {
                if (state.transitions.containsKey(c)) {
                    nextStates.addAll(state.transitions.get(c));
                }
            }

            currentStates.clear();
            for (NFAState nextState : nextStates) {
                currentStates.addAll(eClosure(nextState));
            }
        }

        for (NFAState state : currentStates) {
            if (finalStates.contains(state.getName())) {
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

