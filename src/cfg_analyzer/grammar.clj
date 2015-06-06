(ns cfg-analyzer.grammar
  (:require [clj-yaml.core :as yaml]
            [clojure.set :refer [intersection]]
            [cfg-analyzer.utils :refer [in?]]
            [clojure.core.match :refer [match]]))

(defn read-yaml
  "Reads a yaml file, returns hash"
  [filename]
  (yaml/parse-string (slurp filename)))

(defn make-grammar
  "Transforms the yaml into something more suitable"
  [yaml-map]
  {:accept_strings (yaml-map :accept_strings)
   :reject_strings (yaml-map :reject_strings)
   :variables      (set (yaml-map :variables))
   ; Ensure all the termals are strings and not integers
   :terminals      (set (map str (yaml-map :terminals)))
   :start          (yaml-map :start)
   :rules          (yaml-map :rules)})

(defn match-rule
  "Returns the rule that matches the given char"
  [rules input-char]
  (first (filter
           (fn [rule] (= input-char (first rule)))
           rules)))

(defn find-next-rule
  [rule-sets next-symbol input-buffer]
  (match-rule (get rule-sets (keyword next-symbol)) (first input-buffer)))

(defn recognizes?
  "Returns true if grammar can generate input-string"
  [grammar input-string]
  (let [start-variable (grammar :start)
        rule-sets (grammar :rules)
        is-var? (partial contains? (grammar :variables))
        is-term? (partial contains? (grammar :terminals))
        next-rule (partial find-next-rule rule-sets)]

    (loop [stack (list start-variable)
           input-buffer (map str input-string)]
      (let [next-symbol (first stack)]
        (match [{:stack-empty (empty? stack)
                 :input-empty (empty? input-buffer)
                 :is-var      (is-var? next-symbol)
                 :is-term     (is-term? next-symbol)
                 :terms-match (= (first stack) (first input-buffer))
                 :found-rule  (next-rule next-symbol input-buffer)}]

               ; loop terminating conditions
               [{:stack-empty true :input-empty true}] true
               [{:stack-empty true :input-empty false}] false

               ; Is the next-symbol a variable?
               ; Reject if no rule found
               [{:is-var true :found-rule nil}] false
               [{:is-var true :found-rule found-rule}] (recur
                              ; Push items from next rule onto stack
                              (concat found-rule (rest stack))
                              input-buffer)
               ; Or a terminal?
               [{:is-term true :terms-match true}] (recur (rest stack) (rest input-buffer))
               :else false)))))











