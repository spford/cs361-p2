package fa.nfa;

import java.util.*;

/**
 * DFA is an implementation that provides all necessary operations to construct
 * a Determinate Finite Automata.
 * @author Luis Acosta
 * @author Spencer Ford
 */

public class NFA implements NFAInterface {

    private LinkedHashSet<Character> sigma;
    private HashMap<String, NFAState> states;
    private TreeSet<String> finalStates;
    private String startState;

    public NFA() {
        this.sigma = new LinkedHashSet<>();
        this.states = new HashMap<>();
        this.finalStates = new TreeSet<>();
        this.startState = null;
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return Set.of();
    }

    /** Returns Set of valid 'e' transitions from a state 's'
     * This method creates a stack and set of valid transitions. After when the stack is empty,
     * meaning the number of valid transitions is done the set is returned.
     * @param s - NFAState - state to find eClosure
     * @return - Set<NFAState> - set of valid 'e' transitions from input 's'.
     */
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

    /** The max number of copies of this object were created when traversing the inputs string
     * This method will calculate the eClosure of the startState. It will then loop through
     * every character in the string then it will add every valid transition (including e)to a Set  of
     * state objects and comparing the size of this Set to the previously calculated max copies for the
     * largest.
     * @param s - the string to be consumed and checked for acceptance by this NFA
     * @return int - an int holding the max number of copies of this object created when traversing
     * the input string.
     **/
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

    /** Returns boolean value if specified transition is valid
     * This method will check to make sure fromState, toState are states already in the NFA and the character
     * is in the language. Then it will add the transition to the fromState transition set.
     * @param fromState - state transiton is associated with
     *          toState - state transition is going to
     *           onSymb - character in the language the transition occurs on
     * @return boolean - true - transition successfully added
     *                  false - transition failed to be added
     **/
    @Override
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb) {
        if( states.containsKey(fromState) && (sigma.contains(onSymb) || onSymb == 'e')) {
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

    /** Returns boolean value if this object is a DFA
     * This method will look through every states' transition table looking for epsilon
     * transitions and if a specific state has more than one transition on a single
     * character.
     * @return boolean - true - this object is a DFA
     *                  false - this object is an NFA
     */
    @Override
    public boolean isDFA() {

        for (NFAState state : states.values()) {
            Set<NFAState> totalStates = new HashSet<>();
            totalStates.addAll(state.transitions.get('e'));
            if (totalStates.size() > 1) {
                return false;
            }

            for (Character c : sigma) {
                totalStates.clear();
                if (state.transitions.containsKey(c)) {
                    totalStates.addAll(state.transitions.get(c));
                }
                if (totalStates.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Adds States to a NFA Instance
     * @param name - the requested name of the state to be added.
     * @return boolean - true - state does not already exist and was successfully added
     *                 - false - state was not added. Probably do to the state of 'name' already exists.
     **/
    @Override
    public boolean addState(String name) {
        if ( !states.containsKey(name) ) {
            NFAState newState = new NFAState(name);
            states.put(name, newState);
            addTransition(name, Set.of(name), 'e');
            return true;
        }
        return false;
    }

    /** Adds existing state to a final state set
     * @param name - the name of the state to be added to final state set
     * @return boolean - true - specified state was successfully added to final state set
     *                 - false - specified state not added to final state set
     **/
    @Override
    public boolean setFinal(String name) {
        if( states.containsKey(name) ) {
            finalStates.add(name);
            return true;
        }
        return false;
    }

    /** Sets existing state as the start state
     * @param name - the name of the state to be made the start state
     * @return boolean - true - specified state was successfully made the start state
     *                 - false - specified state not made the start state
     **/
    @Override
    public boolean setStart(String name) {
        if( states.containsKey(name) ) {
            startState = name;
            return true;
        }
        return false;
    }

    /** Add a Character to the language
     * @param symbol - Character to be added to the language
     **/
    @Override
    public void addSigma(char symbol) {
        if(!sigma.contains(symbol) && symbol != 'e') {
            sigma.add(symbol);
        }
    }

    /** Travels the NFA object and return if the passed string is part of the language.
     * First, this method will determine the eClosure of the start state.
     * Next, method will break the string up in to an array of Characters to be compared
     * to the Characters that make up the names of the states of this NFA object. For every
     * character of the array this method will see if the state has a valid transition available.
     * Then method will then add it to the stack of available paths per character consumption.
     * The method will consume the string as long as valid transitions exist until the end when it
     * will make sure the state that it ends on is a final state.
     * @param s - the string that is being tested if it is accepted by the NFA object
     * @return boolean - true - specified string successfully traveled the NFA and landed on a final state
     *                 - false - specified string failed to find valid transitions to consume the input string
     *                           or the current state at the end of the input string is not a final state
     **/
    @Override
    public boolean accepts(String s) {
        Set<NFAState> currentStates = eClosure(states.get(startState));

        for (Character c : s.toCharArray()) {
            if (!sigma.contains(c) && c != 'e') {
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

    /** Returns Set of Characters containing the language
     * @return sigma - Set of Characters containing the language
     **/
    @Override
    public Set<Character> getSigma() {
        return sigma;
    }

    /** Returns the NFAState object requested
     * @param name - the name of the state to be returned
     * @return NFAState object
     **/
    @Override
    public NFAState getState(String name) {
        if (states.containsKey(name)) {
            return states.get(name);
        }
        return null;
    }

    /** Returns boolean value if specified string is a final state
     * @param name - the name of the state to be checked for final state status
     * @return boolean - true - state is a final state
     *                  false - state is not a final state
     **/
    @Override
    public boolean isFinal(String name) {
        return finalStates.contains(name);
    }

    /** Returns boolean value if specified string is the start state
     * @param name - the name of the state to be checked for start state status
     * @return boolean - true - state is the start state
     *                  false - state is not the start state
     **/
    @Override
    public boolean isStart(String name) {
        return startState.contentEquals(name);
    }
}

