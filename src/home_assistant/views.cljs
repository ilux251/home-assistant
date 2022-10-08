(ns home-assistant.views
  (:require
   [re-frame.core :as re-frame]
   [home-assistant.subs :as subs]
   ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1
      @name]
     ]))