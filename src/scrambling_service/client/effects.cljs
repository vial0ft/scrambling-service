(ns scrambling-service.client.effects
  (:require [scrambling-service.client.api :as api]
            [re-frame.core :as re  :refer [reg-cofx reg-fx reg-event-fx inject-cofx]]))

(reg-fx :api/scramble-check
        (fn [body]
          (api/scramble?
           {:body body
            :handler (fn [{:keys [result]}] (re/dispatch [:result result]))
            :error-handler (fn [err] (re/dispatch [:result (str err)]))})))

(reg-event-fx :clean (fn [_ _] {:dispatch [:initialize-db]}))

(reg-cofx :validate (fn [cofx _] (assoc cofx :validation-error nil)))

(reg-event-fx :scramble-check (inject-cofx :validate)
              (fn [{:keys [db validation-error]} _]
                (if validation-error {:db (assoc db :result validation-error)}
                    {:db db
                     :api/scramble-check (into {} (map (fn [[k v]] [k (:value v)]) (:strings db)))})))
