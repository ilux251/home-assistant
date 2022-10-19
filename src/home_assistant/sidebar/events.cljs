(ns home-assistant.sidebar.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            
            [home-assistant.util.date :as ud]))

(rf/reg-cofx
 :set-time
 (fn [coeffects _]
   (let [date (js/Date.)]
    (assoc coeffects :time {:hours (.getHours date)
                            :minutes (.getMinutes date)
                            :seconds (.getSeconds date)
                            :month (.getMonth date)
                            :date (.getDate date)
                            :day (.getDay date)}))))

(rf/reg-event-fx
 ::update-time
 [(rf/inject-cofx :set-time)]
 (fn [{:keys [db time]} _]
   {:db (assoc db :time time)}))

(defn- times-to-hour-and-date
  [times]
  (map #(let [time (js/Date. %)
              hours (.getHours time)
              date (.getDate time)]
          {:hours hours
           :date date}) times))

(defn- extract-weather-data
  [data]
  (let [days-count 4
        weather-today-time (take (* 24 2) (get-in data [:hourly :time]))
        weather-days-time (take days-count (rest (get-in data [:daily :time])))]
    {:hourly {:weathercode (take (count weather-today-time) (get-in data [:hourly :weathercode]))
              :time weather-today-time
              :hour-and-date (times-to-hour-and-date weather-today-time)
              :temperature (take (count weather-today-time) (get-in data [:hourly :temperature_2m]))}
     :days {:weather-code (take days-count (rest (get-in data [:daily :weathercode])))
            :time weather-days-time
            :weekdays (ud/times-to-day weather-days-time)
            :temperature-min (take days-count (rest (get-in data [:daily :temperature_2m_min])))
            :temperature-max (take days-count (rest (get-in data [:daily :temperature_2m_max])))}}))

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