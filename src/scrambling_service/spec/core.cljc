(ns scrambling-service.spec.core
  (:require #?(:clj [clojure.spec.alpha :as s] :cljs [cljs.spec.alpha :as s])))


(s/def ::a-z   #(re-matches #"^.*[a-z]" %))

(s/def ::scambling-request (s/map-of #{:letters :word} ::a-z))


(defn validation [value spec]
   (s/valid? spec value))
