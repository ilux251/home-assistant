(ns home-assistant.frontend.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 ::name
 (fn [db]
   (:name db)))

(rf/reg-sub
 ::current-view
 (fn [db]
   (:current-view db)))
