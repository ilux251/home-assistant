(ns home-assistant.events
  (:require
   [re-frame.core :as re-frame]
   [home-assistant.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
