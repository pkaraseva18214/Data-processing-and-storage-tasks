(ns first.philosophers)

(defn make-fork []
  (ref true))

(defn make-philosopher [name forks food-amt]
  (ref {:name name :forks forks :eating? false :food food-amt}))

(defn start-eating [phil]
  (dosync
    (if (every? true? (map ensure (:forks @phil)))
      (do
        (doseq [f (:forks @phil)] (alter f not))
        (alter phil assoc :eating? true)
        (alter phil update-in [:food] dec)
        true)
      false)))

(defn stop-eating [phil]
  (dosync
    (when (:eating? @phil)
      (alter phil assoc :eating? false)
      (doseq [f (:forks @phil)] (alter f not)))))

(defn dine [phil retry-interval max-eat-duration max-think-duration]
  (while (pos? (:food @phil))
    (if (start-eating phil)
      (do
        (Thread/sleep (rand-int max-eat-duration))
        (stop-eating phil)
        (Thread/sleep (rand-int max-think-duration)))
      (Thread/sleep retry-interval))))

(def forks (cycle (take 5 (repeatedly #(make-fork)))))

(def philosophers
  (doall (map #(make-philosopher %1 [(nth forks %2) (nth forks (inc %2))] 200)
              ["Phil. 1" "Phil. 2" "Phil.3" "Phil. 4" "Phil. 5"]
              (range 5))))

(defn start []
  (doseq [phil philosophers]
    (.start (Thread. #(dine phil 5 100 100)))))

(defn status []
  (dosync
    (doseq [i (range 5)]
      (let [f @(nth forks i)
            p @(nth philosophers i)]
        (println (str "fork: available=" f))
        (println (str (:name p)
                      ": eating=" (:eating? p)
                      " food=" (:food p)))))))