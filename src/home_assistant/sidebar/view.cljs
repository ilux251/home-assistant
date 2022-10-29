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
      (let [weather-data @(rf/subscribe [::sub/get-weather-data])
            current-hours @(rf/subscribe [::sub/current-hours])
            weather-data-hourly (take 4 (nthnext (:hourly weather-data) current-hours))
            weather-data-days (:days weather-data)]
        [:<>
         [:h3 "Wetter"]
         [:div.currrent-day
          (map-indexed (fn [idx hours]
                         (if (= idx 0)
                           [:span.current (Math/round (:temperature hours))]
                           [:span (Math/round (:temperature hours))])) weather-data-hourly)]
         [:div.days
          (map (fn [day]
                 [:<>
                  [:div
                   [:span.min (Math/round (:temperature-min day))]
                   [:span.max (Math/round (:temperature-max day))] (:weekday day)]]) weather-data-days)]]))}))

(defn view
  []
  [:div.sidebar
   [time-view]
   [weather-view]])