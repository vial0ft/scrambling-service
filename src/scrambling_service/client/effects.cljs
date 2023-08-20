(ns scrambling-service.client.effects
  (:require [scrambling-service.client.api :as api]
            [scrambling-service.spec.core :as spec]
            [re-frame.core :as re  :refer [reg-cofx reg-fx reg-event-fx inject-cofx]]))

(defn- request-args [{:keys [args]}]
  (into {} (map (fn [[k v]] [k (:value v)]) args)))

(defn- validate-arg [[arg-k arg-v]]
  (if (spec/validation arg-v :scrambling-service.spec.core/a-z) nil
      [arg-k "Only lower case letters will be used (a-z). No punctuation or digits will be included."]))

(reg-fx :api/scramble-check
        (fn [body]
          (api/scramble?
           {:body body
            :handler (fn [{:keys [result]}] (re/dispatch [:result result]))
            :error-handler (fn [err] (re/dispatch [:result (str err)]))})))

(reg-event-fx :clean (fn [_ _] {:dispatch [:initialize-db]}))

(reg-cofx :validate (fn [cofx _]
                      (let [r-args (request-args (:db cofx))
                            validated-args (->> r-args
                                                (map validate-arg)
                                                (filter #(not (nil? %)))
                                                (into {}))]
                        (assoc cofx :validation-errors validated-args))))

(reg-event-fx :set-error (fn [{:keys [db]} [_ field error]]
                           (println field error)
                           {:db (assoc-in db [:args field :error] error)}))

(reg-event-fx :scramble-check (inject-cofx :validate)
              (fn [{:keys [db validation-errors]} _ ]
                (if-not (empty? validation-errors)
                  {:dispatch-n (->> (keys (:args db))
                                    (map (fn [str-k] [:set-error str-k (get validation-errors str-k "")])))}
                  {:dispatch-n (map (fn [str] [:set-error str ""]) (keys (:args db)))
                   :api/scramble-check (request-args db)})))
