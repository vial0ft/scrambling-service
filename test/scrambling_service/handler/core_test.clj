(ns scrambling-service.handler.core-test
  (:require [clojure.test :refer :all]
            [integrant.core :as ig]
            [ring.mock.request :as mock]
            [scrambling-service.handler.core :as sh]))

(deftest handler-test
  (testing "index page exists"
    (let [handler  (ig/init-key :scrambling-service.handler/core {})
          response (handler (mock/request :get "/"))]
      (is (= 200 (:status response)) "response ok"))))
