(ns home-assistant.frontend.sidebar.view
  (:require [re-frame.core :as rf]
            [reagent.core :as reagent]
            [home-assistant.frontend.sidebar.events :as evt]
            [home-assistant.frontend.sidebar.subs :as sub]
            [home-assistant.frontend.util.date :as ud]))

(defn- time-view
  []
  (reagent/create-class
   {:component-did-mount (fn [] (js/setInterval #(rf/dispatch [::evt/update-time]) 1000))

    :render
    (fn []
      (let [time @(rf/subscribe [::sub/current-time])]
        
         (when time
           [:<>
            [:h2 (ud/time-to-string time)]
            [:h3 (ud/date-to-string time)]])))}))

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
                           ^{:key (str "weather-today-" idx)}
                           [:span.current (Math/round (:temperature hours))]
                           ^{:key (str "weather-today-" idx)}
                           [:span (Math/round (:temperature hours))])) weather-data-hourly)]
         [:div.days
          (map (fn [day]
                 ^{:key (str "weather-days-" day)}
                 [:div
                  [:span.min (Math/round (:temperature-min day))]
                  [:span.max (Math/round (:temperature-max day))] (:weekday day)]) weather-data-days)]]))}))

(defn view
  []
  [:div.sidebar
   [time-view]
   [weather-view]])