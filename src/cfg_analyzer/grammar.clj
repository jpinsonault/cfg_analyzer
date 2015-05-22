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
  {:variables (set (yaml-map :variables))
   :terminals (set (yaml-map :terminals))
   :start (yaml-map :start)
   :rules (yaml-map :rules)})

(defn match-rule
  "Returns the rule that matches the given char"
  [rules input-char]
  (first (filter
           (fn [rule] (= input-char (first rule)))
           rules)))

(defn find-next-rule
  "docstring"
  [rule-sets next-symbol input-buffer]
  (match-rule (get rule-sets (keyword next-symbol)) (first input-buffer)))

(defn recognizes-match?
  "Same as recognizes? but uses the match library"
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

(defn recognizes?
  "Returns true if the grammar can generate the string"
  [grammar input-string]
  (let [start-variable (grammar :start)
        rule-sets (grammar :rules)
        is-var? (partial contains? (grammar :variables))
        is-term? (partial contains? (grammar :terminals))]

    (loop [stack (list start-variable)
           input-buffer (map str input-string)]
      ; Check for terminating conditions
      (if (empty? stack)
        (if-not (empty? input-buffer)
          ; Stack empty but buffer is not, reject
          false
          ; Both are empty, accept
          true)

        (let [next-symbol (first stack)]
          (cond
            (is-var? next-symbol)
            (let [next-rule (match-rule (get rule-sets (keyword next-symbol)) (first input-buffer))]
              (if-not (nil? next-rule)
                ; Rule found. Pop from stack, then add the next rule backwards onto the stack
                (recur
                  (concat next-rule (rest stack))
                  input-buffer)
                ; No rule found, reject
                false))

            (is-term? next-symbol)
            (if (= next-symbol (first input-buffer))
              ; Remove the char from stack and buffer, loop
              (recur (rest stack) (rest input-buffer))
              ; else reject
              false)))))))













