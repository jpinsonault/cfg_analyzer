(ns cfg-analyzer.core
  (:gen-class)
  (:require [cfg-analyzer.grammar :refer [read-yaml
                                          recognizes?
                                          recognizes-match?
                                          make-grammar]]
            [cfg-analyzer.validation :refer [validate-grammar]]))

(defn -main
  "Reads in a grammar and an input string, tells you if the grammar can generate the input string"
  [grammar-file input-string]
  (let [grammar (make-grammar (read-yaml grammar-file))]
    (validate-grammar grammar)
    (println (recognizes-match? grammar input-string))))
