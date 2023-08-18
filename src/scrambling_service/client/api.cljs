(ns scrambling-service.client.api
  (:require [ajax.core :refer [POST]]))

(defn scramble? [{:keys [body handler error-handler]}]
  (POST "/api"
        {:params body
         :format :json
         :response-format :json
         :keywords? true
         :handler handler
         :error-handler error-handler}))
