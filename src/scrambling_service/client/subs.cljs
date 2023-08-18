(ns scrambling-service.client.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re :refer [reg-sub]]))




(reg-sub :strings/error (fn [{:keys [strings]} [_ name]]
                          (get-in strings [name :error])))

(reg-sub :strings-values (fn [{:keys [strings]}]
                           (into {} (map (fn [[k v]] [k (:value v)]) strings))))

(reg-sub :result (fn [{:keys [result]}] result))
