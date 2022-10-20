(ns home-assistant.sidebar.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::current-time
 (fn [db]
   (:time db)))

(rf/reg-sub
 ::current-date
 (fn [db]
   (get-in db [:time :date])))

(rf/reg-sub
 ::current-hours
 (fn [db]
   (get-in db [:time :hours])))

(rf/reg-sub
 ::get-weather-data
 (fn [db]
   (get-in db [:weather :data] {})))