(ns home-assistant.frontend.task.view
  (:require [re-frame.core :as rf]
            [home-assistant.frontend.events :as events]
            [home-assistant.frontend.task.subs :as task-subs]
            [home-assistant.frontend.task.events :as task-events]
            [home-assistant.frontend.util.date :as ud]))

(defn- status
  [{:keys [checked date]}]
  (cond
    (and (ud/is-date-before date)
         (= checked 0))
     "miss"

    (= checked 1)
    "done"
    
    (= checked 0)
    "due"))

(defn tasks-view
  [tasks]
  (map (fn [{:keys [id summary date] :as task}]
         (let [time (when date (js/Date. date))
               time-map (when time (ud/date-to-map time))]
          ^{:key id}
          [:div.task {:class (status task)}
           [:div.title
            {:on-click #(rf/dispatch [::task-events/select-task id])}
            summary]
           (when time
             [:div.datetime
              [:span.date (ud/date-to-string time-map)]
              [:span.time (ud/time-to-string time-map)]])])) tasks))

(defn view 
  []
  (let [{:keys [selected-task subtasks]} @(rf/subscribe [::task-subs/selected-task])]
    [:div.task-view 
     [:h2 (:summary selected-task)]
     [:button {:class "back"
               :on-click #(rf/dispatch [::events/change-view :dashboard])} "Zurück"]
     [:div
      (map (fn [subtask]
             ^{:key (:id subtask)}
             [:div {:class (status subtask)} (:summary subtask)]) subtasks)]]))