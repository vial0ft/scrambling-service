(ns scrambling-service.handler.core
  (:require [compojure.core :refer [routes GET POST]]
            [ring.middleware.json :refer [wrap-json-response]]
            [clojure.java.io :as io]
            [integrant.core :as ig]
            [scrambling-service.service.core :refer [scramble?]]
            [scrambling-service.spec.core :as s]
            [ring.util.response :as resp]))

(defn handler [{:keys [body-params]}]
  (-> (if (s/validation body-params :scrambling-service.spec.core/scrambling-request)
                   (resp/response {:result (scramble? (:letters body-params) (:word body-params))})
                   (resp/bad-request {:error "Request requires `letters` and `word` string fields which contains only `a-z` letters"}))
    (resp/content-type "application/json")))


(defmethod ig/init-key :scrambling-service.handler/core [_ _]
  (wrap-json-response
   (routes
    (GET "/" [] (io/resource "scrambling_service/handler/pages/index.html"))
    (POST "/api" req (handler req)))))
