(ns cfg-analyzer.grammar
  (:require [clj-yaml.core :as yaml]
            [clojure.set :refer [intersection]]
            [cfg-analyzer.utils :refer [in?]]))

(defn read-yaml
  "Reads a yaml file, returns hash"
  [filename]
  (yaml/parse-string (slurp filename)))

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


(defn rhs-dont-start-with-same-terminal?
  "Returns true if for each variable, the rules don't start with the same terminals"
  [rule-sets]
  (every? (fn [[variable rules]]
            ; If the intersection is empty, each rule started with a different terminal
            (empty? (apply intersection
                           (for [rule rules]
                             ; for will return a seq of length 1 sets
                             (set (first rule))))))
          rule-sets))

(defn validate-grammar
  "Raises an exception if the grammar has some problem"
  [grammar]
  (if (not (variables-and-terminals-disjoint? (grammar :variables) (grammar :terminals)))
    (throw (Exception. "Variables and terminals should be disjoint")))
  (if (not (rules-start-with-terminals? (grammar :rules) (grammar :terminals)))
    (throw (Exception. "Not all rules start with terminals")))
  (if (not (rhs-dont-start-with-same-terminal? (grammar :rules)))
    (throw (Exception. "Not all rules start with terminals"))))


(defn recognize?
  "Returns true if the grammar can generate the string"
  [grammar input-string]
  )