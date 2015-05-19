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
  [rules terminals]
  (every? (fn [rule]
            (let [[variable right-sides] rule]
              (every? (fn
                        [right-side]
                        (in? (apply vector terminals) (first right-side)))
                      right-sides)))
          rules))

(defn validate-grammar
  "Raises an exception if the grammar has some problem"
  [grammar]
  (if (not (variables-and-terminals-disjoint? (grammar :variables) (grammar :terminals)))
    (throw (Exception. "Variables and terminals should be disjoint")))
  (if (not (rules-start-with-terminals? (grammar :rules) (grammar :terminals)))
    (throw (Exception. "Not all rules start with terminals"))))

(defn recognize?
  "Returns true if the grammar can generate the string"
  [grammar input-string]
  )
