(ns scrambling-service.client.events
  (:require [re-frame.core :as re :refer [reg-event-db inject-cofx]]
            [scrambling-service.client.db :as db]))

(reg-event-db :initialize-db
              (fn [_ _] db/default-state))

(reg-event-db :args/change
              (fn [db [_ field value]] (assoc-in db [:args field :value] value)))

(reg-event-db :result
              (fn [db [_ result]] (assoc db :result result)))
