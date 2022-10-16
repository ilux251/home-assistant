(ns home-assistant.views
  (:require
   [re-frame.core :as rf]
   [home-assistant.subs :as subs]
   [home-assistant.events :as events]
   [home-assistant.task.view :as task-view]
   [home-assistant.sidebar.view :as sidebar-view]
   ))

(defn dashboard
  []
  [:<>
   [:button {:on-click #(rf/dispatch [::events/change-view :to-do])} "To-do"]])

(defn main-panel []
  (let [current-view (rf/subscribe [::subs/current-view])]
    [:<>
     [sidebar-view/view]
     [:div
      (case @current-view
        :dashboard [dashboard]
        :to-do [task-view/view])]]))