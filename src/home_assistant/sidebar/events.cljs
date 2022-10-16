(ns home-assistant.sidebar.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            
            [day8.re-frame.http-fx]))

(rf/reg-cofx
 :set-time
 (fn [coeffects _]
   (let [date (js/Date.)]
    (assoc coeffects :date {:hours (.getHours date)
                            :minutes (.getMinutes date)
                            :seconds (.getSeconds date)
                            :month (.getMonth date)
                            :date (.getDate date)
                            :day (.getDay date)}))))

(rf/reg-event-fx
 ::update-time
 [(rf/inject-cofx :set-time)]
 (fn [{:keys [db date]} _]
   {:db (assoc db :date date)}))

(defn- set-zero-if-needed
  [number]
  (if (> number 9)
    number
    (str "0" number)))

(defn- time-in-range?
  [[date-from date-to] [hours-from hours-to] time]
  (let [[_ date-match hours-match] (re-matches #"\d{4}\-\d{2}-(\d{2})(?:T(\d{2})\:\d{2})?" time)
        date (js/parseInt date-match)
        hours (js/parseInt hours-match)
        in-date-range? (and (>= date date-from) (<= date date-to))
        in-hours-range? (and (>= hours hours-from) (<= hours hours-to))]
    (println date hours)
    (if hours-match
      (and in-date-range? in-hours-range?)
      in-date-range?)))

(defn- extract-weather-data
  [data {:keys [hours date]}]
  (let [date (js/parseInt (set-zero-if-needed date))
        hours (js/parseInt (set-zero-if-needed hours))
        weather-today-time (take-while (partial time-in-range? [date (+ date 1)] [hours (+ hours 4)]) (get-in data [:hourly :time]))
        weather-days-time (take-while (partial time-in-range? [date (+ date 4)] [0 0]) (get-in data [:daily :time]))]
    {:today {:weathercode (take (count weather-today-time) (get-in data [:hourly :weathercode]))
             :time weather-today-time
             :temperature (take (count weather-today-time) (get-in data [:hourly :temperature_2m]))}
     :days {:weather-code (take (count weather-days-time) (get-in data [:daily :weathercode]))
            :time weather-days-time
            :temperature-min (take (count weather-days-time) (get-in data [:daily :temperature_2m_min]))
            :temperature-max (take (count weather-days-time) (get-in data [:daily :temperature_2m_max]))}}))

(rf/reg-event-fx
 :success-handler
 [(rf/inject-cofx :set-time)]
 (fn [{:keys [db date]} [_ data]]
   (println data)
   {:db (assoc-in db [:weather :data] (extract-weather-data data date))}))

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