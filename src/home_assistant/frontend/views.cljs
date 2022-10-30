(ns home-assistant.frontend.views
  (:require
   [re-frame.core :as rf]
   [home-assistant.frontend.subs :as subs]
   [home-assistant.frontend.task.view :as task-view]
   [home-assistant.frontend.sidebar.view :as sidebar-view]
   [home-assistant.frontend.events :as events]))

(defn dashboard
  []
  [:h1 "Dashboard"])

(defn main-panel []
  (let [current-view (rf/subscribe [::subs/current-view])]
    [:<>
     [sidebar-view/view]
     [:div
      [:button {:on-click #(rf/dispatch [::events/get-server-data])} "Get server data"]
      [:button {:on-click #(rf/dispatch [::events/auth])} "Auth"]
      (case @current-view
        :dashboard [dashboard]
        :to-do [task-view/view])]]))