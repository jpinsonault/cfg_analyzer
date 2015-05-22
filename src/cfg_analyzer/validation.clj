(ns cfg-analyzer.validation
  (:require [clojure.set :refer [intersection]]
            [cfg-analyzer.utils :refer [in?]]))

(defn no-duplicate-terminals?
  "Returns true if the rules don't have duplicate starting terminals"
  [rules]
  (let [firsts (for [rule rules]
                 (first rule))]
    ; The two counts will be equal if no duplicates were found
    (= (count firsts)
       (count (set firsts)))))

(defn variables-and-terminals-disjoint?
  "Returns true if variabes and terminals are disjoint"
  [variables terminals]
  (empty? (intersection (set variables) (set terminals))))

(defn rules-start-with-terminals?
  "Returns true if all rules start with terminals"
  [rule-sets terminals]
  (every? (fn [[variable rules]]
            (every? (fn
                      [rule]
                      (in? terminals (first rule)))
                    rules))
          rule-sets))

(defn rules-dont-start-with-same-terminal?
  "Returns true if for each variable, the rules don't start with the same terminals"
  [rule-sets]
  (every? (fn [[variable rules]] (no-duplicate-terminals? rules))
          rule-sets))

(defn validate-grammar
  "Raises an exception if the grammar has some problem"
  [grammar]
  (if (not (variables-and-terminals-disjoint? (grammar :variables) (grammar :terminals)))
    (throw (Exception. "Variables and terminals should be disjoint")))
  (if (not (rules-start-with-terminals? (grammar :rules) (grammar :terminals)))
    (throw (Exception. "Not all rules start with terminals")))
  (if (not (rules-dont-start-with-same-terminal? (grammar :rules)))
    (throw (Exception. "Some rules have the same start symbol"))))

