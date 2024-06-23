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

nc -l localhost 8080
POST /api/document HTTP/1.1
Host: localhost:8080
User-Agent: curl/7.79.1
Accept: */*
Content-Length: 211
Content-Type: multipart/form-data; boundary=------------------------9dd36b0576a69928

--------------------------9dd36b0576a69928
Content-Disposition: form-data; name="testFile"; filename="tikataka.txt"
Content-Type: text/plain

Richard is best!

--------------------------9dd36b0576a69928--
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

Herunterladen eines Dokuments:
``` im browser ausführen
http://localhost:8080/api/documents?fileName=VmVybWVpdGVyYmVzY2hlaW5pZ3VuZy5wZGYK
```


mongofiles:
=======
```
./mongofiles -v --uri mongodb://192.168.0.11:27017/test list --prefix documents
./mongofiles -v --uri mongodb://192.168.0.11:27017/test get testfile.txt --prefix documents
mongofiles -v --uri mongodb://192.168.0.11:27017/test delete --prefix documents 'tikataka.txt'
```

mongosh:
=======
```
use test
db.documents.files.find()
```