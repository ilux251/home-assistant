# home-assistant

## Lokale Entwicklung
### Build
* ```npm install```
* Starten mit Calva.
  * Project Type "deps.edn + shadow-cljs" auswählen.
  * Mit dem Alias :cljs ausführen.
* Server starten
  * home-assistant.backend.core/start
  * Der Server liefer die von shadow-cljs compilierten Dateien unter ``resources/public/`` aus.

### Server
* Mit der Middleware ```wrap-reload``` wird nach dem Speichern der *.clj Dateien automatisch deployt.


## Release
JAR erstellen
```
lein uberjar
```
JAR ausführen
```
java -cp <filename>.jar clojure.main -m home-assistant.backend.core
```