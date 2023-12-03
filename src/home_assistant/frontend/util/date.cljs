(ns home-assistant.frontend.util.date)

(defn set-zero-if-needed
  [number]
  (if (> number 9)
    number
    (str "0" number)))

(defn get-day
  [day-number]
  (case day-number
    0 "Sonntag"
    1 "Montag"
    2 "Dienstag"
    3 "Mittwoch"
    4 "Donnerstag"
    5 "Freitag"
    6 "Samstag"))

(defn get-month
  [month-number]
  (case month-number
    0 "Januar"
    1 "Februar"
    2 "März"
    3 "April"
    4 "Mai"
    5 "Juni"
    6 "Juli"
    7 "August"
    8 "September"
    9 "Oktober"
    10 "November"
    11 "Dezember"))

(defn times-to-day
  [times]
  (map #(-> %
            js/Date.
            .getDay
            get-day) times))

(defn get-date
  [date]
  (js/Date. (str (.getFullYear date) "-" 
                 (set-zero-if-needed (.getMonth date)) "-" 
                 (set-zero-if-needed (.getDate date)))))

(defn is-date-today
  [date]
  (let [date-today-a (get-date (js/Date.))
        date-today-b (get-date (js/Date. date))]
    (= date-today-a date-today-b)))

(defn time-to-string
  [time]
  (let [hours (set-zero-if-needed (:hours time))
        minutes (set-zero-if-needed (:minutes time))]
    (str hours ":" minutes)))

(defn date-to-string
  [time]
  (let [date (set-zero-if-needed (:date time))]
    (str (get-day (:day time)) " " date " " (get-month (:month time)))))

(defn date-to-map
  [time]
  {:hours (.getHours time)
   :minutes (.getMinutes time)
   :seconds (.getSeconds time)
   :month (.getMonth time)
   :date (.getDate time)
   :day (.getDay time)
   :year (.getFullYear time)})