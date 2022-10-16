(ns home-assistant.events
  (:require
   [re-frame.core :as rf]
   [home-assistant.db :as db]
   ))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
 ::change-view
 (fn [db [_ view]]
   (assoc db :current-view view)))
