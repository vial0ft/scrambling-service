(ns scrambling-service.client.client
  (:require [ajax.core :refer [GET POST]]
            [reagent.core :as r]
            [reagent.dom :as d]
            [scrambling-service.client.events]
            [scrambling-service.client.effects]
            [scrambling-service.client.subs]
            [re-frame.core :as re :refer [subscribe]]))


(defn- on-change-wrapper [on-change]
  (fn [e]
    "catch on-change"
    (on-change (.-value (.-target e)))))

(defn Description []
  [:div.description
   [:p
    [:u "(scramble? letters word)"] " returns " [:strong "true"] " if a portion of " [:i "letters"] " characters can be rearranged to match " [:i "word"] " , otherwise returns " [:strong "false"]]
   [:p
    [:strong "Note:"] " Only lower case letters will be used (a-z). No punctuation or digits will be included."]
   [:p
    [:strong "Example:"] " (scramble? “rekqodlw” ”world”) ==> true"]])


(defn Error [err-msg]
  (fn [err-msg]
    [:div.error (when err-msg [:span {:style {:color "red"}} err-msg])]))

(defn LabledField [{:keys [key class on-change error]}]
  (fn [{:keys [key class lable input error]}]
    [:div
     [:div {:class class}
      [:label {:for key} lable]
      [:input
       {:name key
        :type "text"
        :on-change (on-change-wrapper on-change)}]]
     [Error error]]))

(defn on-submit-form []
  (fn [e]
    (.preventDefault e)
    (re/dispatch [:scramble-check])))

(defn ScrambleForm []
  [:<>
   [:form {:on-submit (on-submit-form)}
    [LabledField
     {:class "strings"
      :name "letters"
      :lable "letters"
      :on-change #(re/dispatch [:strings/change :letters %])
      :error @(subscribe [:strings/error :letters])}]
    [LabledField
     {:class "strings"
      :name "word"
      :lable "word"
      :on-change #(re/dispatch [:strings/change :word %])
      :error @(subscribe [:strings/error :word])}]

    [:div.buttons
     [:button {:type :submit} "Check"]
     [:button {:on-click #(re/dispatch [:clean])
               :type :reset} "Clear"]]]])

(defn Result [result]
  (fn [result]
    [:div.result
     [:p [:strong "Result: "] [:span (str result)]]
     ]))

(defn State []
  [:div (str @(subscribe [:state]))])

(defn MainPage []
  [:<>
   [:h1 {:style {:margin-top "5%"}} "Scramble?"]
   [Description]
   [ScrambleForm]
   [Result @(subscribe [:result])]
   [State]
   ])



(do
  (re/dispatch-sync [:initialize-db])
  (d/render [MainPage]
            (js/document.getElementById "main")))
