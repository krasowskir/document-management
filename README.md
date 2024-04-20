Dokumentenverwaltung
====================

Erstellung der Struktur:
```
mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-webapp -DarchetypeVersion=1.4 -DgroupId=org.richard.home -DartifactId=document-management  -DinteractiveMode=false

tree document-management/
document-management/
├── pom.xml
└── src
    └── main
        └── webapp
            ├── WEB-INF
            │   └── web.xml
            └── index.jsp

4 directories, 3 files
```

Test:
```
curl -i -X GET http://localhost:8080/api/player?age
HTTP/1.1 200 OK
Date: Sat, 20 Apr 2024 10:13:58 GMT
Content-Length: 19
Server: Jetty(11.0.20)

get age of me: 32 
```