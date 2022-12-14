(ns home-assistant.frontend.views
  (:require
   [re-frame.core :as rf]
   [home-assistant.frontend.subs :as subs]
   [home-assistant.frontend.task.view :as task-view]
   [home-assistant.frontend.sidebar.view :as sidebar-view]
   [home-assistant.frontend.task.subs :as task-subs]))

(defn- without-subtasks
  [tasks]
  (filter #(nil? (:subtaskid %)) tasks))

(defn tasks-today
  []
  (let [tasks @(rf/subscribe [::task-subs/tasks-today])
        tasks-without-subtask (without-subtasks tasks)]
    [:div.tasks-today (task-view/tasks-view tasks-without-subtask)]))

(defn tasks-soon
  []
  (let [tasks @(rf/subscribe [::task-subs/tasks-soon])
        tasks-without-subtask (without-subtasks tasks)]
    [:div.tasks-soon (task-view/tasks-view tasks-without-subtask)]))

(defn dashboard
  []
  [:div.tasks
   [tasks-today]
   [tasks-soon]])

(defn main-panel []
  (let [current-view (rf/subscribe [::subs/current-view])]
    [:<>
     [sidebar-view/view]
     (case @current-view
       :dashboard [dashboard]
       :task [task-view/view])]))