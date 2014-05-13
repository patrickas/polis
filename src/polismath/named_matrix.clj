(ns polismath.named-matrix
  (:require [clojure.core.matrix :as matrix])
  (:use polismath.utils))


(defn named-matrix [& [rows cols matrix]]
  "Convenience function for creating a named matrix (nmat). Shape of named matrix is {:rows :cols :matrix}.
  Values of :rows and :cols are in same order as corresponding values (votes) in matrix and thus let us find a
  position within the matrix based on rowname and colname."
  (let [rows (or rows [])
        cols (or cols [])
        matrix (or matrix [[]])]
    {:rows rows :cols cols :matrix matrix}))


(defn update-nmat [nmat values]
  "This takes (rowname, colname, value) pairs and inserts them into a named matrix, adding rows and columns
  if necessary."
  (reduce
    (fn [{:keys [rows cols matrix] :as nmat} [row col value]]
      (let [[row-index col-index] (map #(typed-indexof %1 %2) [rows cols] [row col])
            [rows cols] (map (fn [xs x i] (if (= -1 i) (conj xs x) xs))
                          [rows cols] [row col] [row-index col-index])
            ; If necessary, insert new columns and/or rows of zeros
            matrix (if (= -1 col-index) (mapv #(conj % 0) matrix) matrix)
            matrix (if (> (count rows) (count matrix)) (conj matrix (into [] (repeat (count cols) 0))) matrix)
            ; Get indices of matrix position to be updated (note typed-indexof above gives -1 if item not in coll)
            indices (mapv (fn [xs i] (if (= -1 i) (- (count xs) 1) i))
                          [rows cols] [row-index col-index])
            matrix (assoc-in matrix indices value)]
        (assoc nmat :rows rows :cols cols :matrix matrix)))
    nmat values))

(require '[taoensso.timbre.profiling :as profiling
           :refer (pspy pspy* profile defnp p p*)])
(defnp row-subset [nmat row-indices]
  "Returns a new named matrix which has been subset to the given row-indices"
  (assoc nmat
    :rows (greedy (filter-by-index (:rows nmat) row-indices))
    :matrix (greedy-false (filter-by-index (:matrix nmat) row-indices))))


(defnp rowname-subset [nmat row-names]
  "Returns a new named matrix which has been subset based on the given row-names"
  (let [nmat-rows (into [] (:rows nmat))]
    (->> row-names
      ; a bit of a hack with into [] ; shouold really fix upstream
      (map #(typed-indexof nmat-rows %))
      (greedy)
      (row-subset nmat))))


(defnp safe-rowname-subset [nmat row-names]
  "This version of rowname-subset filters out negative indices, so that if not all names in row-names
  are in nmat, it just subsets to the rownames that are. Should scrap other one?"
  (let [nmat-rows (into [] (:rows nmat))]
    (->> row-names
      ; a bit of a hack with into [] ; shouold really fix upstream
      (map #(typed-indexof nmat-rows %))
      (filterv #(>= % 0))
      (row-subset nmat))))


(defn get-row-by-name [nmat row-name]
  (matrix/get-row (:matrix nmat) (typed-indexof (:rows nmat) row-name)))


(defn inv-rowname-subset [nmat row-names]
  "Returns named matrix which has been subset to all the rows not in row-names"
  (row-subset nmat
    (remove (set (map #(typed-indexof (:rows nmat) %) row-names))
      (range (count (:rows nmat))))))


