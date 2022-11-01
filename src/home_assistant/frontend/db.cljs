(ns home-assistant.frontend.db)

(def default-db
  {:name "Home Assistant"
   :current-view :dashboard
   :task {:tasks [{:id 1
                   :summary "Einkaufen"
                   :checked 0
                   :retry false
                   :date "2022-11-01T19:00:00Z"}
                  {:id 2
                   :summary "Paprika"
                   :checked 1
                   :subtaskid 1}
                  {:id 4
                   :summary "Wäsche waschen"
                   :checked false
                   :retry true
                   :date "2022-11-01T19:00:00Z"}]}})