# Blog Backend - GraphQL
<br />
<div align="center">
<img src="GraphQL_Logo.svg.png" alt="Logo" width="600" height="400">
</div>


## Einführung
In diesem Repository findest du den Code für ein Blog-Backend, das mit GraphQL implementiert wurde.
Hier erhältst du Informationen zum Thema GraphQL, Anweisungen zum Herunterladen und Ausführen 
des Codes sowohl lokal als auch in einem Container sowie ein Cheat Sheet zur Bereitstellung des 
Backends auf Azure. Zusätzlich werden einige Beispiel-GraphQL-Queries zum Testen bereitgestellt.
Mit diesem ReadMe-File solltest du in der Lage sein, das Blog-Backend sowohl lokal als auch in einem
Container auszuführen und zu testen. Du solltest auch in der Lage sein, direkt auf den Container
zuzugreifen, der auf Azure bereitgestellt wurde. Falls du auf Probleme stößt oder weitere Informationen
benötigst, wende dich bitte an den Projektverantwortlichen oder erstelle ein Issue im GitHub-Repository.


<!-- Inhaltsverzeichnis -->
<details>
  <summary><bold>Table of Contents<bold></summary>
  <ol>
    <li>
    <a href="#graphql">GraphQL</a>
    <ul>
        <li><a href="#was-ist-graphql">Was ist GraphQL</a></li>
        <li><a href="#graphql-vs-rest">GraphQL vs REST</a></li>
      </ul>
    </li>
    <li><a href="#los-starten">Los Starten</a></li>
    <li><a href="#containisierung">Containisierung</a></li>
    <li><a href="#azure-deployment">Azure Deployment</a></li>
    <li><a href="#graphql-anfragen">GraphQL Anfragen</a></li>
  </ol>
</details>


---

# GraphQL
## Was ist GraphQL

GraphQL ist eine Abfragesprache und Laufzeitumgebung zur Anforderung von Daten über APIs. Es wurde von Facebook entwickelt und ist eine Alternative zu REST-APIs. GraphQL ermöglicht den Clients,
genau die Daten anzufordern, die sie benötigen, und reduziert somit Overfetching und Underfetching. Es gibt drei Hauptoperationen in GraphQL: Queries, Mutations und Subscriptions.

Queries:
Queries sind die grundlegenden Anfragen in GraphQL, mit denen Clients Daten von einem Server anfordern können. Sie ähneln den GET-Anfragen in REST-APIs.
In einer GraphQL-Abfrage kann der Client die gewünschten Felder und deren Struktur genau angeben. Dadurch wird die Menge der übertragenen Daten reduziert und die Effizienz der Anwendung verbessert.

Mutations:
Mutations sind in GraphQL dafür zuständig, Daten zu ändern (erstellen, aktualisieren oder löschen). Sie ähneln den POST-, PUT- und DELETE-Anfragen in REST-APIs.
Mutations sorgen dafür, dass die Änderungen am Server vorgenommen werden und eine Antwort an den Client zurückgegeben wird, die die aktualisierten Daten enthält.

Subscriptions:
Subscriptions ermöglichen Echtzeit-Updates in GraphQL. Sie ermöglichen es dem Client, auf bestimmte Ereignisse auf dem Server zu hören und Benachrichtigungen zu erhalten, wenn diese Ereignisse eintreten.
Subscriptions basieren auf dem WebSocket-Protokoll und sind nützlich für Anwendungen, bei denen Daten in Echtzeit aktualisiert werden müssen, wie z. B. bei Chat-Anwendungen oder Benachrichtigungssystemen.

Zusammenfassend bieten GraphQL-Operationen (Queries, Mutations und Subscriptions) eine flexible und effiziente Möglichkeit, mit APIs zu interagieren und genau die Daten abzurufen oder zu ändern,
die für eine bestimmte Anwendung erforderlich sind.


## GraphQL vs REST

GraphQL und REST sind zwei unterschiedliche Ansätze für API-Design. GraphQL ist eher fachlich getrieben, während REST technisch getrieben ist. Sie haben unterschiedliche Vorteile und Nachteile,
die je nach Projektanforderungen eine Rolle spielen können.

Vorteile von GraphQL:
-	Kein Overfetching oder Underfetching: Der Client erhält genau die gewünschten Daten.
-	Typsicherheit: Das Schema ist in GraphQL integriert.
-	Standardisierung: GraphQL arbeitet mit der Schema Definition Language.
-	Einfachere Versionierung: Neue Felder können hinzugefügt werden, ohne bestehende Resolver zu beeinträchtigen.
     Nachteile von GraphQL:
-	Kein Caching: Aufgrund des einzelnen Endpunkts kann kein Caching ausgeführt werden.
-	Potenziell hoher Serveraufwand: Umfangreiche Selektionen können viele Resolver und Datenbankabfragen erfordern.

Ein wesentlicher Unterschied besteht darin, dass REST auf einzelne Endpunkte und CRUD-Operationen (POST, GET, PUT, DELETE) für Ressourcenzugriff setzt,
während GraphQL nur einen Endpunkt hat (/graphql), über den viele Queries und/oder Mutations ausgeführt werden können.
Die Wahl zwischen GraphQL und REST hängt von den spezifischen Anforderungen des Projekts ab. In manchen Fällen kann es sogar sinnvoll sein,
beide Ansätze in einem Projekt zu nutzen, je nachdem, ob fachliche oder technische Aspekte im Vordergrund stehen.


## Los Starten 

Um das Projekt lokal auszuführen, klone es zunächst mit dem folgenden Befehl:
```sh
git clone https://github.com/Leoislami/blogBackend_VS2.git
```
Um den Service in einer IDE zu starten, verwenden den folgenden Befehl:
```sh
./mvnw quarkus:dev
```

Du kannst dann auf der GraphQL Client-Seite unter dieser URL landen:
```sh
http://localhost:8080/q/graphql-ui/
```

## Containisierung

Wenn du das Projekt in einem Container ausführen möchtest, findest du hier eine Anleitung,
wie du das Docker-Image herunterladen und den Container starten kannst.

Schritt 1: Docker-Image herunterladen
Lade das Docker-Image mit dem folgenden Befehl herunter:
```sh
docker pull ghcr.io/leoislami/blogbackend_vs2:latest
```
Schritt 2: Docker-Compose ausführen
Führe das Image mit docker-compose im Projektstammverzeichnis aus. Der folgende Befehl erledigt den Rest für dich:
```sh
docker-compose -f quarkus.yaml up
```
Dieser Befehl startet den Quarkus-Container und den MySQL-Datenbank-Container.

## Azure Deployment

Für das Azure Deployment wurden folgende Befehle verwendet:

a) Setup vorbereiten
```sh
az login
az account set --subscription "Azure for Students"

set RESOURCE_GROUP="d-rg-blogbackend_graphql"

set MYSQL_SERVER_NAME="d-mysql-blogbackend-graphql"

set LOCATION="switzerlandnorth"
```
b) Ressourcengruppe erstellen
```sh
az group create --name %RESOURCE_GROUP% --location %LOCATION%
```
c) MySQL Flexible Server einrichten
```sh
az mysql flexible-server create ^

--name %MYSQL_SERVER_NAME% ^

--resource-group %RESOURCE_GROUP%  ^

--location %LOCATION%  ^

--public-access None  ^

--database-name test ^

--admin-user leoadmin ^

--admin-password xxxxxxxxx ^

--sku-name Standard_B1s ^

--storage-size 32 ^

--tier Burstable ^

--version 8.0.21

az mysql flexible-server firewall-rule create ^

--resource-group %RESOURCE_GROUP% ^

--name %MYSQL_SERVER_NAME% ^

--rule-name allowip ^

--start-ip-address 0.0.0.0
```
d) Environment einrichten
```sh
az containerapp env create ^

--name d-ce-blogbackend-graphql ^

--resource-group d-rg-blogbackend_graphql ^

--location switzerlandnorth
```
e) Datenbank einrichten
```sh
Führen Sie die folgenden Befehle im Azure CLI PowerShell aus:

wget --no-check-certificate https://dl.cacerts.digicert.com/DigiCertGlobalRootCA.crt.pem

mysql -h d-mysql-blogbackend-graphql.mysql.database.azure.com -u leoadmin -p
```
f) Container erstellen und Image angeben
```sh
set GITHUB_USER="leoislami"

set GITHUB_TOKEN="xxxxxxxxxxxxxxxxxxxxxxxxx"

set RESOURCE_GROUP="d-rg-blogbackend_graphql"

set CONTAINERAPPS_ENVIRONMENT="d-ce-blogbackend-graphql"

set CONTAINERAPP_BACKEND="d-ca-blogbackend-graphql"



az containerapp create ^

--name %CONTAINERAPP_BACKEND% ^

--resource-group %RESOURCE_GROUP% ^

--environment %CONTAINERAPPS_ENVIRONMENT% ^

--image ghcr.io/leoislami/blogbackend_vs2.git:latest ^

--registry-username %GITHUB_USER% ^

--registry-password %GITHUB_TOKEN% ^

--registry-server ghcr.io ^

--min-replicas 0 ^

--max-replicas 1 ^

--cpu 0.5 --memory 1.0Gi ^

--ingress external --target-port 8080 ^

--env-vars QUARKUS_DATASOURCE_USERNAME="leoadmin" ^

QUARKUS_DATASOURCE_PASSWORD="xxxxxxxxxx" ^

QUARKUS_DATASOURCE_JDBC_URL= "jdbc:mysql://d-mysql-blogbackend-graphql.mysql.database.azure.com:3306/blogdb?useSSL=true" 
```

Die Seite ist über die folgende URL erreichbar:
```sh
https://d-ca-blogbackend-graphql.proudwater-cf99cdf6.switzerlandnorth.azurecontainerapps.io/
```
Da nach dem Deployment auf Azure bemerkt wurde, dass in den application.properties vergessen wurde, GraphQL über Open IO verfügbar zu machen,
kannst du dennoch auf das Backend zugreifen. Hier ist eine kurze Anleitung, wie du dies tun kannst:
-	Öffne Google und suche nach "Altair GraphQL Client Chrome".
-	Installiere die Erweiterung in deinem Chrome-Browser.
-	Öffne den Altair GraphQL Client und verwende ihn, um deine GraphQL-Abfragen wie gewohnt auszuführen.
-	Füge im Feld "URL" zusätzlich /graphql am Ende der URL ein, sodass sie wie folgt aussieht:
```sh
https://dcablogbackendgraphql.proudwatercf99cdf6.switzerlandnorth.azurecontainerapps.io/graphql
```

## GraphQL Anfragen

Im Folgenden findest du einige Beispielanfragen für das GraphQL-Backend:

Mutationen:

Einen neuen Eintrag hinzufügen:
```sh
mutation {
addEntry(input: {
title: "Test Eintrag",
content: "Dieser Eintrag dient nur zu Testzwecken"
})
}
```
Einen vorhandenen Eintrag durch einen neuen ersetzen:mutation{
```sh
mutation {
replaceEntry(id: 1, input: {
title: "Mein Zweiter Beitrag",
content: "Dieser Beitrag dient auch nur für Testzwecke"
})
}
```
Einen neuen Kommentar hinzufügen:
```sh
mutation {
addComment(id: 3, input: {
comment: "erster commi"
})
}
```

Abfragen (Queries):

Einen bestimmten Eintrag abrufen:
```sh
{
entry(id: 1) {
content,
title
}
}
```
Eine Liste aller Einträge abrufen:
```sh
{
entries(from: 1, to: 10) {
contentPreview,
title
}
}
```
Einen Kommentar für einen bestimmten Eintrag abrufen:{
```sh
{
entry(id: 3) {
comments {
date,
author,
comment
},
title,
content
}
}
```