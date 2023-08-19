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
                      (assoc cofx :validation-errors (-> (get-in cofx [:db :strings])
                                                        spec/validation-strings))))

(reg-event-fx :scramble-check (inject-cofx :validate)
              (fn [{:keys [db validation-errors]} _]
                (if-not (empty? validation-errors)
                  {:db (update-in db [:strings]
                                  #(reduce (fn [acc [field error]]
                                             (assoc-in acc [field :error] error)) % validation-errors))}
                  {:db db
                   :api/scramble-check (strings db)})))
