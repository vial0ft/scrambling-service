(ns scrambling-service.handler.core-test
  (:require [clojure.test :refer :all]
            [integrant.core :as ig]
            [ring.mock.request :as mock]
            [scrambling-service.handler.core :as sh]
            [clojure.spec.alpha :as s]
            [clojure.test.check.generators :as gen]))

(deftest handler-test
  (testing "index page exists"
    (let [handler  (ig/init-key :scrambling-service.handler/core {})
          response (handler (mock/request :get "/"))]
      (is (= 200 (:status response)) "response ok")))

  (testing "Successful scramble result `true`"
    (let [{:keys [body] :as resp} (sh/handler {:body-params {:letters "shiff" :word "fish"}})]
      (is (= 200 (:status resp)))
      (is (true? (:result body)))))

  (testing "Successful scramble result `false`"
    (let [{:keys [body] :as resp} (sh/handler {:body-params {:letters "hiff" :word "fish"}})]
      (is (= 200 (:status resp)))
      (is (false? (:result body)))))

  (testing "Missing field isn't acceptable"
    (is (= 400 (:status (sh/handler {:body-params {:letters "hiff"}}))))
    (is (= 400 (:status (sh/handler {:body-params {:word "hiff"}})))))

  (testing "Fields should contain only a-z symbols"
    (let [non-a-z (into #{} (map #(str (char %)) (concat (range 32 97) (range 123 126))))]
      (doseq [_ (range 100)]
        (is (= 400 (:status (sh/handler {:body-params {:letters (gen/generate (s/gen non-a-z))
                                                       :word (gen/generate (s/gen non-a-z))}})))))))
  )
