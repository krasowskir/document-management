# Entscheidungen

## bevorzuge embedded jetty gegenüber jakarta.servlet Spezifikation

>Hintergrund:
> 
> - wenn mann servlets entwickelt, ist die Vertestung von Ihnen schwierig
> - man muss einen servletcontainer hochfahren und er erwartet nur ein war file mit web.xml deployment descriptor
> - war-file enthält class files mit ggf annotationen in einer bestimmten Verzeichnisstruktur
> - das Deployment sieht so aus, dass man ein war file in ein Verzeichnis packt und dann den server startet (separat)
> - ist eher schwierig -> besser jetty embedded