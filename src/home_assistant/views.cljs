(ns home-assistant.views
  (:require
   [re-frame.core :as rf]
   [home-assistant.subs :as subs]
   [home-assistant.task.view :as task-view]
   [home-assistant.sidebar.view :as sidebar-view]
   ))

(defn dashboard
  []
  [:h1 "Dashboard"])

(defn main-panel []
  (let [current-view (rf/subscribe [::subs/current-view])]
    [:<>
     [sidebar-view/view]
     [:div
      (case @current-view
        :dashboard [dashboard]
        :to-do [task-view/view])]]))