(ns scrambling-service.service.core)

(defn scramble? [letters word]
  (if (> (count word) (count letters)) false
      (let [letters-map (frequencies letters)]
        (->> (frequencies word)
             (every? (fn [[l c]] (<= c (get letters-map l 0))))))))
