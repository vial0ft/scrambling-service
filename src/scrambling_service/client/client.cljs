(ns scrambling-service.client.client
  (:require [reagent.core :as r]
            [reagent.dom :as d]
            [scrambling-service.client.events]
            [scrambling-service.client.effects]
            [scrambling-service.client.subs]
            [re-frame.core :as re :refer [subscribe]]))


(defn- on-change-wrapper [on-change]
  (fn [e] (on-change (.-value (.-target e)))))

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

(defn LabledField [{:keys [class name lable on-change error]}]
  (fn [{:keys [class name lable on-change error]}]
    [:div
     [:div {:class class}
      [:label {:for name} lable]
      [:input
       {:name name
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
      :on-change #(re/dispatch [:args/change :letters %])
      :error @(subscribe [:args/error :letters])}]
    [LabledField
     {:class "strings"
      :name "word"
      :lable "word"
      :on-change #(re/dispatch [:args/change :word %])
      :error @(subscribe [:args/error :word])}]

    [:div.buttons
     [:button {:type :submit} "Check"]
     [:button {:on-click #(re/dispatch [:clean])
               :type :reset} "Clear"]]]])

(defn Result [result]
  (fn [result]
    [:div.result
     [:p [:strong "Result: "] [:span (str result)]]
     ]))

(defn MainPage []
  [:<>
   [:h1 {:style {:margin-top "5%"}} "Scramble?"]
   [Description]
   [ScrambleForm]
   [Result @(subscribe [:response/result])]
   ])

(do
  (re/dispatch-sync [:initialize-db])
  (d/render [MainPage]
            (js/document.getElementById "main")))
