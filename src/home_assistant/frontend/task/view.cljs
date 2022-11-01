(ns home-assistant.frontend.task.view
  (:require [re-frame.core :as rf]
            [home-assistant.frontend.events :as events]
            [home-assistant.frontend.task.subs :as task-subs]))

(defn view 
  []
  (let [{:keys [selected-task subtasks] :as task} @(rf/subscribe [::task-subs/selected-task])]
    (println task)
    [:<> 
     [:h2 (:summary selected-task)]
     [:button {:on-click #(rf/dispatch [::events/change-view :dashboard])} "Zurück"]
     [:div
      (map (fn [subtask]
             ^{:key (:id subtask)}
             [:div (:summary subtask)]) subtasks)]]))