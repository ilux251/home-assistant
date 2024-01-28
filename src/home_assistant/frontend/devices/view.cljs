(ns home-assistant.frontend.devices.view 
  (:require [home-assistant.frontend.devices.subs :as subs]
            [re-frame.core :as rf]))

(defn view
  []
  (let [entities @(rf/subscribe [::subs/entities])]
    [:div.entities
     (map (fn [room]
            [:div
             [:span (:name (val room))]
             [:ul (map (fn [entity]
                         [:li entity]) (:entities (val room)))]]) entities)]))