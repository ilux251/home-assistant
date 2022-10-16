(ns home-assistant.sidebar.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::current-time
 (fn [db]
   (:date db)))