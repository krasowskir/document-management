# Entscheidungen

## bevorzuge embedded jetty gegenüber jakarta.servlet Spezifikation

>Hintergrund:
> 
> - wenn mann servlets entwickelt, ist die Vertestung von Ihnen schwierig
> - man muss einen servletcontainer hochfahren und er erwartet nur ein war file mit web.xml deployment descriptor
> - ist eher schwierig -> besser jetty embedded