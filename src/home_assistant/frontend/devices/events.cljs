(ns home-assistant.frontend.devices.events
  (:require [re-frame.core :as rf]
            
            [home-assistant.frontend.util.request :as har]))

(rf/reg-event-db
 ::success
 (fn [db [_ path data]]
   (assoc-in db path data)))

(rf/reg-event-db
 ::failure
 (fn [db [_ data]]
   (println data)
   db))

(rf/reg-event-fx
 ::get-all-entities
 (fn [_ _]
   {:dispatch [::har/api-request {:method :post
                                  :url-path "template"
                                  :on-success [::success [:device :entities]]
                                  :on-failure [::failure]
                                  :params {"template" "{% set areas = areas() %} { {% for area in areas %} \"{{ area }}\": {\"entities\": [ {% set entities = area_entities(area) %} {% for entity in entities %} \"{{ entity }}\"{% if entities.index(entity) != entities.index(entities[-1:][0]) %},{% endif %} {% endfor %} ], \"name\": \"{{ area_name(area) }}\" } {% if areas.index(area) != areas.index(areas[-1:][0]) %},{% endif %} {% endfor %} }"}}]}))