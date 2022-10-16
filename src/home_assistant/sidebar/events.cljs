(ns home-assistant.sidebar.events
  (:require [re-frame.core :as rf]))

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
 (fn [{:keys [db date] :as cofx} _]
   {:db (assoc db :date date)}))