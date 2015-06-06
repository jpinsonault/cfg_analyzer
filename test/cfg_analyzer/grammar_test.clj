(ns cfg-analyzer.grammar-test
  (:require [clojure.test :refer :all]
            [cfg-analyzer.validation :refer :all]
            [cfg-analyzer.grammar :refer :all]
            [cfg-analyzer.utils :refer :all]))


(deftest ai#bj#ck#-accepts-good-strings
  (testing "Should accept on good strings"
    (let [grammar-file (read-yaml "example_grammars/ai#bj#ck#.yaml")
          grammar (make-grammar grammar-file)]
      (is (true?
            (every?
              (fn [input] (recognizes-match? grammar input))
              (grammar :accept_strings)))))))

(deftest ai#bj#ck#-rejects-bad-strings
  (testing "Should reject on bad strings"
    (let [grammar-file (read-yaml "example_grammars/ai#bj#ck#.yaml")
          grammar (make-grammar grammar-file)]
      (is (true?
            (every?
              (fn [input] (not (recognizes-match? grammar input)))
              (grammar :reject_strings)))))))

(deftest an#bn-accepts-good-strings
  (testing "Should accept on good strings"
    (let [grammar-file (read-yaml "example_grammars/an#bn.yaml")
          grammar (make-grammar grammar-file)]
      (is (true?
            (every?
              (fn [input] (recognizes-match? grammar input))
              (grammar :accept_strings)))))))

(deftest an#bn-rejects-bad-strings
  (testing "Should reject on bad strings"
    (let [grammar-file (read-yaml "example_grammars/an#bn.yaml")
          grammar (make-grammar grammar-file)]
      (is (true?
            (every?
              (fn [input] (not (recognizes-match? grammar input)))
              (grammar :reject_strings)))))))


(deftest w#wr-accepts-good-strings
  (testing "Should accept on good strings"
    (let [grammar-file (read-yaml "example_grammars/w#wr.yaml")
          grammar (make-grammar grammar-file)]
      (is (true?
            (every?
              (fn [input] (recognizes-match? grammar input))
              (grammar :accept_strings)))))))

(deftest w#wr-rejects-bad-strings
  (testing "Should reject on bad strings"
    (let [grammar-file (read-yaml "example_grammars/w#wr.yaml")
          grammar (make-grammar grammar-file)]
      (is (true?
            (every?
              (fn [input] (not (recognizes-match? grammar input)))
              (grammar :reject_strings)))))))

(deftest turing-machine-accepts-good-strings
  (testing "Should accept on good strings"
    (let [grammar-file (read-yaml "example_grammars/turing-machine.yaml")
          grammar (make-grammar grammar-file)]
      (is (true?
            (every?
              (fn [input] (recognizes-match? grammar input))
              (grammar :accept_strings)))))))

(deftest turing-machine-rejects-bad-strings
  (testing "Should reject on bad strings"
    (let [grammar-file (read-yaml "example_grammars/turing-machine.yaml")
          grammar (make-grammar grammar-file)]
      (is (true?
            (every?
              (fn [input] (not (recognizes-match? grammar input)))
              (grammar :reject_strings)))))))
