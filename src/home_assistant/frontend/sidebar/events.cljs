(ns home-assistant.frontend.sidebar.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            
            [home-assistant.frontend.util.date :as ud]))

(rf/reg-cofx
 :set-time
 (fn [coeffects _]
   (let [date (js/Date.)]
    (-> (assoc coeffects :time {:hours (.getHours date)
                                :minutes (.getMinutes date)
                                :seconds (.getSeconds date)
                                :month (.getMonth date)
                                :date (.getDate date)
                                :day (.getDay date)})))))

(rf/reg-event-fx
 ::update-time
 [(rf/inject-cofx :set-time)]
 (fn [{:keys [db time]} _]
   {:db (assoc db :time time)}))

(defn- times-to-hours
  [times]
  (map #(.getHours (js/Date. %)) times))

(defn- map-hourly-weather-data
  [weather-data]
  (let [time-count 48
        weather-hourly-time (get-in weather-data [:hourly :time])
        weather-hourly-weathercodes (get-in weather-data [:hourly :weathercode])
        weather-hourly-temperature (get-in weather-data [:hourly :temperature_2m])
        weather-hourly-hours (times-to-hours weather-hourly-time)]
    (for [idx (range time-count)]
      {:weathercode (nth weather-hourly-weathercodes idx)
       :hour (nth weather-hourly-hours idx)
       :temperature (nth weather-hourly-temperature idx)})))

(defn- map-daily-weather-data
  [weather-data]
  (let [time-count 4
        weather-daily-time (rest (get-in weather-data [:daily :time]))
        weather-daily-weathercodes (rest (get-in weather-data [:daily :weathercode]))
        weather-daily-temperature-min (rest (get-in weather-data [:daily :temperature_2m_min]))
        weather-daily-temperature-max (rest (get-in weather-data [:daily :temperature_2m_max]))
        weather-daily-weekdays (ud/times-to-day weather-daily-time)]
    (for [idx (range time-count)]
      {:weathercode (nth weather-daily-weathercodes idx)
       :weekday (nth weather-daily-weekdays idx)
       :temperature-min (nth weather-daily-temperature-min idx)
       :temperature-max (nth weather-daily-temperature-max idx)})))

(defn- extract-weather-data
  [data]
  {:hourly (map-hourly-weather-data data)
   :days (map-daily-weather-data data)})

(rf/reg-event-fx
 :success-handler
 (fn [{:keys [db]} [_ data]]
   {:db (assoc-in db [:weather :data] (extract-weather-data data))}))

(rf/reg-event-db
 :failure-handler
 (fn [{:keys [db]} [_ data]]
   (println data)
   {:db (assoc-in db [:weather :error] data)}))

(rf/reg-event-fx
 ::get-weather-data
 (fn [_ _]
   {:http-xhrio {:method :get
                 :uri "https://api.open-meteo.com/v1/forecast?latitude=50.74&longitude=7.14&hourly=temperature_2m,weathercode&daily=weathercode,temperature_2m_max,temperature_2m_min&timezone=Europe%2FBerlin"
                 :timeout "10000"
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:success-handler]
                 :on-failure [:failure-handler]}}))