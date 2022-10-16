(ns home-assistant.sidebar.view
  (:require [re-frame.core :as rf]
            [reagent.core :as reagent]
            [home-assistant.sidebar.events :as evt]
            [home-assistant.sidebar.subs :as sub]))

(defn- set-zero-if-needed
  [number]
  (if (> number 9)
    number
    (str "0" number)))

(defn- get-day
  [day-number]
  (case day-number
    0 "Sonntag"
    1 "Montag"
    2 "Dienstag"
    3 "Mittwoch"
    4 "Donnerstag"
    5 "Freitag"
    6 "Samstag"))

(defn- get-month
  [month-number]
  (case month-number
    0 "Januar"
    1 "Februar"
    2 "März"
    3 "April"
    4 "Mai"
    5 "Juni"
    6 "Juli"
    7 "August"
    8 "September"
    9 "Oktober"
    10 "November"
    11 "Dezember"))

(defn- time-view
  []
  (reagent/create-class
   {:component-did-mount (fn [] (js/setInterval #(rf/dispatch [::evt/update-time]) 1000))

    :render
    (fn []
      (let [time @(rf/subscribe [::sub/current-time])
            date (set-zero-if-needed (:date time))
            hours (set-zero-if-needed (:hours time))
            minutes (set-zero-if-needed (:minutes time))
            seconds (set-zero-if-needed (:seconds time))]
        
         (when time
           [:<>
            [:h2 (str hours ":" minutes ":" seconds)]
            [:h3 (str (get-day (:day time)) " " date " " (get-month (:month time)))]])))}))

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