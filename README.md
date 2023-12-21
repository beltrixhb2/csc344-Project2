# csc344-Project2
Propositional logic inference system in Clojure.

In this project you will implement part of a propositional logic inference system in Clojure. Specifically, your system will perform not, and, and if-elimination using forward chaining inference. This means that given a proposition and a knowledge base of known facts, your system will derive all propositions which follow using the rules of inference.

You will implement four rules of inference:

    not-elimination: from (not (not X)), infer X
    and-elimination: from (and X Y), infer X and infer Y
    modus ponens: from (if X Y) and X, infer Y
    modus tollens: from (if X Y) and (not Y), infer (not X)

The main entry point for your program should be a function called fwd-infer which takes two arguments: a proposition, and a set of known facts (propositions). It should return the new knowledge base – that is, the originally known facts with the proposition and any newly inferred propositions added to it.


Examples

    (fwd-infer '(if a b) '#{(not b)})
    Because: (if a b)
    and: (not b)
    I derived: (not a)
    by modus tollens
    => #{(if a b) (not a) (not b)}
     
    (fwd-infer 'a '#{(if a b) (if b c)})
    Because: (if a b)
    and: a
    I derived: b
    by modus ponens
    Because: (if b c)
    and: b
    I derived: c
    by modus ponens
    => #{(if a b) a c (if b c) b}
     
    (fwd-infer-elim '(and (not (not (if a b))) a) '#{})
    Because: (and (not (not (if a b))) a)
    I derived: (not (not (if a b)))
    and: a
    by and-elimination
    Because: (not (not (if a b)))
    I derived: (if a b)
    by not-elimination
    Because: (if a b)
    and: a
    I derived: b
    by modus ponens
    => #{(if a b) (not (not (if a b))) a (and (not (not (if a b))) a) b}
