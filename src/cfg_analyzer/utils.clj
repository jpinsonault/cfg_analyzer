(ns cfg-analyzer.utils)

(defn in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))
