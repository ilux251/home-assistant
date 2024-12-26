(ns home-assistant.frontend.views
  (:require
   [re-frame.core :as rf]
   [home-assistant.frontend.subs :as subs]
   [home-assistant.frontend.task.view :as task-view]
   [home-assistant.frontend.sidebar.view :as sidebar-view]
   [home-assistant.frontend.task.subs :as task-subs]
   [home-assistant.frontend.devices.view :as devices-view]))

(defn- without-subtasks
  [tasks]
  (filter #(nil? (:subtask %)) tasks))

(defn- dashboard-task-view
  [tasks]
  [:<>
   (if (> (count tasks) 0)
     (task-view/tasks-view tasks)
     [:div "Keine Aufgaben"])])

(defn tasks-today
  []
  (let [tasks @(rf/subscribe [::task-subs/tasks-today])
        tasks-without-subtask (without-subtasks tasks)]
    [:div.tasks-today.task-container
     [:h3 "Heute"]
     [dashboard-task-view tasks-without-subtask]]))

(defn tasks-soon
  []
  (let [tasks @(rf/subscribe [::task-subs/tasks-soon])
        tasks-without-subtask (without-subtasks tasks)]
    [:div.tasks-soon.task-container
     [:h3 "Später"]
     [dashboard-task-view tasks-without-subtask]]))

(defn dashboard
  []
  [:div.tasks
   [tasks-today]
   [tasks-soon]])

;; Nur ein Test um die Authentifizierung im Frontend zu testen. 
;; Nach dem erfolgreichen Login wird ein Cookie gespeichert, der für die weiteren Request verwendet werden kann.
(defn login
  []
  [:div
   [:form {:action "/auth"
           :method :post}
    [:input {:type "text" :name "user" :value "alex"}]
    [:input {:type "password" :name "password" :value "user_password"}]
    [:input {:type "submit" :value "Submit"}]]])

(defn main-panel []
  (let [current-view (rf/subscribe [::subs/current-view])]
    [:div.main {:class @current-view}
     [sidebar-view/view]
     [login]
     (case @current-view
       :dashboard [dashboard]
       :task [task-view/view]
       :devices [devices-view/view])]))