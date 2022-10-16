(ns home-assistant.db)

(def default-db
  {:name "Home Assistant"
   :current-view :dashboard
   :task {:tasks [{:id 1
                    :title "Einkaufen"
                    :sub [{:title "Paprika"
                           :checked? false}
                          {:title "Duschgel"
                           :checked? false}
                          {:title "Trinken"
                           :checked? false}
                          {:title "Nudeln"
                           :checked? false}
                          {:title "Zwiebeln"
                           :checked? false}]
                    :checked? false
                    :retry false
                    :date "16.10.2022"
                    :time "11:40"}
                   {:id 2
                    :title "Wäsche waschen"
                    :checked? false
                    :retry true
                    :date "22.10.2022"
                    :time "17:00"}]}})