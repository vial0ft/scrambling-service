(ns scrambling-service.spec.core
  (:require [clojure.spec.alpha :as s]
            [spec-tools.spec :as spec]))


(s/def ::a-z   #(re-matches #"^.*[a-z]" %))

(s/def ::scambling-request (s/keys :req [::letters ::word]))


(defn validation-strings [strings]
  (->> strings
       (map (fn [[str-k str-v]]
              (if (s/valid? ::a-z str-v) nil
                [str-k "Only lower case letters will be used (a-z). No punctuation or digits will be included."])))
       (filter #(not (nil? %)))
       (into {})))
