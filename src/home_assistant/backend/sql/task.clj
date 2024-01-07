(ns home-assistant.backend.sql.task
  (:require [clojure.java.jdbc :as j]
            [honeysql.format :as sf]
            
            [home-assistant.backend.sql.db :refer [db]]))


(defn get-all-tasks
  []
  (let [statement (sf/format
                   {:select [:*]
                    :from [:task]
                    :where [:or 
                            [:>= [:date :date] [:date [:now]]]
                            [:= :checked 0]]})]
    (j/query db statement)))

(defn create-task
  [{:keys [summary date subtask]}]
  (let [statement (sf/format
                   {:insert-into :task
                    :values [[nil summary subtask date 0]]})]
    (j/execute! db statement)))

(defn update-task
  [update-map id]
  (let [statement (sf/format {:update :task
                              :set update-map
                              :where [:= :id id]})]
    (j/execute! db statement)))

(defn delete-task
  [id]
  (let [statement (sf/format {:delete-from :task
                              :where [:or 
                                      [:= :id id]
                                      [:= :subtaskid id]]})]
    (j/execute! db statement)))


(comment
  ;; Initialize DB with test data
  (do
    (create-task {:summary "Task" :date "2022-11-01T18:00:00"})
    (create-task {:summary "Subtask 1" :subtask 1})
    (create-task {:summary "Subtask 2" :subtask 1})
    (create-task {:summary "Task without Subtask" :date "2022-11-01T18:00:00"})
    (update-task {:checked 1} 2))

  (get-all-tasks)

  ;; Aufgabe erstellen. 
  ;; Wenn ein Attribut nicht gesetzt ist, wird dieser auf NULL gesetzt.
  (create-task {:summary "Subtask 3" :subtask 1})

  ;; Aufgabe aktualisieren
  (update-task
   {:summary "Subtask 1"}
   2)

  (delete-task 1)
  )