(ns scrambling-service.spec.core
  (:require #?(:clj [clojure.spec.alpha :as s] :cljs [cljs.spec.alpha :as s])))


(s/def ::a-z #(re-matches #"^.*[a-z]" %))

(s/def ::letters ::a-z)
(s/def ::word ::a-z)


(s/def ::scrambling-request (s/keys :req-un [::letters ::word]))


(defn validation [value spec]
   (s/valid? spec value))
