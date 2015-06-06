(ns cfg-analyzer.utils
  (:require
    [clojure.java.io :as io]))

(defn in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

(defn find-files
  [dir, ends-with]
  (filter #(.endsWith (.getName %) ends-with) (file-seq (io/file dir))))
