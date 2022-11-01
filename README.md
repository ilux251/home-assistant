# home-assistant

## Lokale Entwicklung
### Build
* ```npm install```
* Starten mit Calva.
  * Project Type "deps.edn + shadow-cljs" auswählen.
  * Mit dem Alias :cljs ausführen.
* Server starten
  * Über die PowerShell ausführen
    ```
    clj -X:run
    ```

### Server
* Mit der Middleware ```wrap-reload``` wird nach dem Speichern der *.clj Dateien automatisch deployt.


## Release
JAR erstellen
```
clj -X:uberjar :jar <filename>.jar
```
JAR ausführen
```
java -cp <filename>.jar clojure.main -m home-assistant.backend.core
```