(ns scrambling-service.handler.core
  (:require [compojure.core :refer :all]
            [ring.middleware.json :refer [wrap-json-response]]
            [clojure.java.io :as io]
            [integrant.core :as ig]
            [scrambling-service.service.core :as service]
            [clojure.spec.alpha :as s]
            [ring.util.response :as resp]))

(defn handler [{:keys [body-params]}]
  (let [valid? (s/valid? :scrambling-service.spec.core/scambling-request body-params)
        response (if valid?
                   (resp/response {:result (service/scramble? (:scrambling body-params) (:word body-params))})
                   (resp/bad-request {:error "Request requires `letters` and `word` string fields which contains only `a-z` letters"}))]
    (resp/content-type response "application/json")))


(defmethod ig/init-key :scrambling-service.handler/core [_ _]
  (wrap-json-response 
   (routes
    (GET "/" [] (io/resource "scrambling_service/handler/pages/index.html"))
    (POST "/api" req (handler req)))))
