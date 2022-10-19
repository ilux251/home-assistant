(ns home-assistant.sidebar.view
  (:require [re-frame.core :as rf]
            [reagent.core :as reagent]
            [home-assistant.sidebar.events :as evt]
            [home-assistant.sidebar.subs :as sub]
            [home-assistant.util.date :as ud]))

(defn- time-view
  []
  (reagent/create-class
   {:component-did-mount (fn [] (js/setInterval #(rf/dispatch [::evt/update-time]) 1000))

    :render
    (fn []
      (let [time @(rf/subscribe [::sub/current-time])
            date (ud/set-zero-if-needed (:date time))
            hours (ud/set-zero-if-needed (:hours time))
            minutes (ud/set-zero-if-needed (:minutes time))
            seconds (ud/set-zero-if-needed (:seconds time))]
        
         (when time
           [:<>
            [:h2 (str hours ":" minutes ":" seconds)]
            [:h3 (str (ud/get-day (:day time)) " " date " " (ud/get-month (:month time)))]])))}))

(defn- weather-view
  []
  (reagent/create-class
   {:component-did-mount (fn [] (rf/dispatch [::evt/get-weather-data]))
    
    :render
    (fn []
      (let [weather-data @(rf/subscribe [::sub/get-weather-data])]
        (println weather-data)
        [:h3 "Wetter"]))}))

(defn view
  []
  [:div.sidebar
   [time-view]
   [weather-view]])