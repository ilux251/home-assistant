(ns home-assistant.frontend.views
  (:require
   [re-frame.core :as rf]
   [home-assistant.frontend.subs :as subs]
   [home-assistant.frontend.task.view :as task-view]
   [home-assistant.frontend.sidebar.view :as sidebar-view]
   [home-assistant.frontend.task.events :as task-events]))

(defn dashboard
  []
  (let [tasks @(rf/subscribe [::subs/tasks])
        tasks-without-subtask (filter #(nil? (:subtaskid %)) tasks)]
    [:<>
     [:div
      (map (fn [task]
             ^{:key (:id task)}
             [:div 
              {:on-click #(rf/dispatch [::task-events/select-task (:id task)])} 
              (:summary task)]) tasks-without-subtask)]]))

(defn main-panel []
  (let [current-view (rf/subscribe [::subs/current-view])]
    [:<>
     [sidebar-view/view]
     [:div
      (case @current-view
        :dashboard [dashboard]
        :task [task-view/view])]]))