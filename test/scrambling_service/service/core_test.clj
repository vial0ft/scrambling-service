(ns scrambling-service.service.core-test
  (:require [clojure.test :refer :all]
            [scrambling-service.service.core :as sc]))

(deftest service-test
  (testing "true scrambled"
    (is (true? (sc/scramble? "rekqodlw" "world")))
    (is (true? (sc/scramble? "cedewaraaossoqqyt" "codewars")))
    (is (true? (sc/scramble? "abrakadaba" ""))))

  (testing "false scrambled"
    (is (false? (sc/scramble? "katas" "steak")))
    (is (false? (sc/scramble? "" "steak")))))
