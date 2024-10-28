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
# multiform data upload
curl -i -XPOST http://localhost:8080/api/documents -F testFile=@/Users/rkrasowski/uebung_workspace/document-management/tikataka.txt
HTTP/1.1 100 Continue

HTTP/1.1 200 OK
Date: Sat, 27 Apr 2024 08:46:42 GMT
Content-Length: 0
Server: Jetty(11.0.20)

Alternative:

# binary data in HTTP POST body
curl -i -XPOST http://localhost:8080/api/documents/binary -H "Content-Type: application/octet-stream" -T /Users/rkrasowski/uebung_workspace/document-management/src/main/resources/application.properties -H "Filename: application.properties"
```

Herunterladen eines Dokuments:

im browser ausführen
``` 
http://localhost:8080/api/documents?fileName=VmVybWVpdGVyYmVzY2hlaW5pZ3VuZy5wZGYK
```
in terminal
``` 
curl -XGET http://localhost:8080/api/documents?fileName="11.svg" -o test.svg
curl -XGET http://localhost:8080/api/documents?objectId="671face051ea4103c0d9decb" -o test2.svg
```

docker:
=======
```
docker run -dit --name mongo -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=richard -e MONGO_INITDB_ROOT_PASSWORD=pelikan -e MONGO_INITDB_DATABASE=test mongo:4.4
./mongosh -u richard -p pelikan mongodb://127.0.0.1:27017
db.createUser({user: "richard",pwd: "pelikan", roles: [ {role: "dbAdmin", db: "test"} ] })
admin> db.grantRolesToUser("richard",[{ role: "dbAdmin", db: "test"}])
./mongosh -u richard -p pelikan mongodb://127.0.0.1:27017/test
```

mongofiles:
=======
```
./mongofiles -v --uri mongodb://192.168.0.11:27017/test list --prefix documents
mongofiles -u richard -p pelikan --authenticationDatabase admin -v --uri mongodb://127.0.0.1:27017/test list --prefix documents
./mongofiles -v --uri mongodb://192.168.0.11:27017/test get testfile.txt --prefix documents
mongofiles -u richard -p pelikan --authenticationDatabase admin -v --uri mongodb://127.0.0.1:27017/test get 4.png --prefix documents
mongofiles -v --uri mongodb://192.168.0.11:27017/test delete --prefix documents 'tikataka.txt'
```

mongosh:
=======
```
./mongosh --username richard --password pelikan
use test
db.documents.files.find()
db.documents.files.findOne({ _id: ObjectId("66fe5b443fbdf2296d5598e1") });
{
  _id: ObjectId('66fe5b443fbdf2296d5598e1'),
  filename: '5.png',
  length: Long('32'),
  chunkSize: 1048576,
  uploadDate: ISODate('2024-10-03T08:52:20.483Z'),
  metadata: { type: 'mp4' }
}
```