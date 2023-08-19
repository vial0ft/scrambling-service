(ns scrambling-service.handler.core
  (:require [compojure.core :refer :all]
            [ring.middleware.json :refer [wrap-json-response]]
            [clojure.java.io :as io]
            [integrant.core :as ig]
            [scrambling-service.service.core :as service]))



(defn handler [{:keys [body-params]}]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body {:result (service/scramble? (:scrambling body-params) (:word body-params))}})



(defmethod ig/init-key :scrambling-service.handler/core [_ options]
  (wrap-json-response 
   (routes
    (GET "/" [] (io/resource "scrambling_service/handler/pages/index.html"))
    (POST "/api" req (handler req)))))
