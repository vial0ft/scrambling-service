(ns scrambling-service.spec.core
  (:require [clojure.spec.alpha :as s]
            [spec-tools.spec :as spec]))


(s/def ::a-z   #(re-matches #"^.*[a-z]" %))


(defn validation-strings [strings]
  (->> strings
       (map (fn [[str-k {:keys [value]}]]
              (if (s/valid? ::a-z value) nil
                [str-k "Only lower case letters will be used (a-z). No punctuation or digits will be included."])))
       (filter #(not (nil? %)))))
