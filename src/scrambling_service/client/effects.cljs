(ns scrambling-service.client.effects
  (:require [scrambling-service.client.api :as api]
            [scrambling-service.spec.core :as spec]
            [re-frame.core :as re  :refer [reg-cofx reg-fx reg-event-fx inject-cofx]]))

(defn- strings [{:keys [strings]}]
  (into {} (map (fn [[k v]] [k (:value v)]) strings)))

(reg-fx :api/scramble-check
        (fn [body]
          (api/scramble?
           {:body body
            :handler (fn [{:keys [result]}] (re/dispatch [:result result]))
            :error-handler (fn [err] (re/dispatch [:result (str err)]))})))

(reg-event-fx :clean (fn [_ _] {:dispatch [:initialize-db]}))

(reg-cofx :validate (fn [cofx _]
                      (assoc cofx :validation-errors (-> (:db cofx)
                                                         strings
                                                         spec/validation-strings))))

(reg-event-fx :set-error (fn [{:keys [db]} [_ field error]]
                           {:db (assoc-in db [:args field :error] error)}))

(reg-event-fx :scramble-check (inject-cofx :validate)
              (fn [{:keys [db validation-errors]} _]
                (if-not (empty? validation-errors)
                  {:dispatch-n (->> (keys (:args db))
                                    (map (fn [str-k] [:set-error str-k (get validation-errors str-k "")])))}
                  {:dispatch-n (map (fn [str] [:set-error str ""]) (keys (:args db)))
                   :api/scramble-check (strings db)})))
