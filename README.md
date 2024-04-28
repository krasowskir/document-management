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

upload document:
```
curl -i -XPOST http://localhost:8080/api/document -T /Users/rkrasowski/uebung_workspace/document-management/README.md
HTTP/1.1 100 Continue

HTTP/1.1 200 OK
Date: Sat, 27 Apr 2024 08:46:42 GMT
Content-Length: 0
Server: Jetty(11.0.20)

Alternative:
curl -i -XPOST http://localhost:8080/api/document -F testFile=@/Users/rkrasowski/uebung_workspace/document-management/tikataka.txt
```


mongofiles:
=======
```
./mongofiles -v --uri mongodb://192.168.0.11:27017/test list --prefix documents
./mongofiles -v --uri mongodb://192.168.0.11:27017/test get testfile.txt --prefix documents
```

mongosh:
=======
```
use test
db.documents.files.find()
```