(ns cfg-analyzer.core
  (:gen-class)
  (:require [cfg-analyzer.grammar :refer :all]))

(defn -main
  "Reads in a grammar and an input string, tells you if the grammar can generate the input string"
  [grammar-file input-string]
  (let [grammar (read-yaml grammar-file)]
    (validate-grammar grammar)))
