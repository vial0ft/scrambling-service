(ns scrambling-service.client.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re :refer [reg-sub]]))

(reg-sub :args/error (fn [{:keys [args]} [_ name]]
                          (get-in args [name :error])))

(reg-sub :response/result (fn [{:keys [result]}] result))

