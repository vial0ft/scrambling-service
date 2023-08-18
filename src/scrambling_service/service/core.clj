(ns scrambling-service.service.core)

(defn scramble? [scrambling word]
  (if (> (count word) (count scrambling)) false
      (let [scrambling-map (frequencies scrambling)]
        (->> (frequencies word)
             (every? (fn [[l c]] (<= c (get scrambling-map l 0))))))))
