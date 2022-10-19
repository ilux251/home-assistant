(ns home-assistant.util.date)

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