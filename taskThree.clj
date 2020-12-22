(ns first.taskThree
  (:use clojure.test))

(defn p-filter-3
  ([value-f coll]
   (let [chunk-size (int (Math/ceil (Math/sqrt (count coll)))),
         parts (partition-all chunk-size coll)]
     (->> parts
          (map (fn [coll1]
                 (future (filter value-f coll1))))

          (doall)
          (map deref)
          (flatten )))))

(deftest test-adder
  (is (=  1 (first (p-filter-3 odd? (range 10)))))
  (is (=  21 (first (p-filter-3 odd? '(20 21 23 15)))))
  (is (=  6 (first (p-filter-3 even? '(215 1 5 3 71 99 45 5 6)))))
  (is (=  7 (nth (p-filter-3 odd? '(20 21 23 15 12 210 77 77 7 46 5 12)) 5)))
  (is (=  26 (nth (p-filter-3 even? (range 30)) 13))))

(run-tests)
