# Infos Patients

Le service **Infos Patients** est un microservice de l'application **MediLabo** responsable de la gestion des informations administratives des patients.

Il constitue la source de référence des données d'identité (nom, prénom, date de naissance, sexe, adresse et numéro de téléphone) utilisées par les autres services de l'application.

---

## Fonctionnalités

Le service permet de :

* Consulter la liste des patients.
* Consulter les informations d'un patient.
* Créer un nouveau patient.
* Modifier les informations d'un patient.
* Supprimer un patient.

---

## Technologies

* Java 21
* Spring Boot 4.0.5
* Spring MVC
* Spring Data JPA
* PostgreSQL
* Maven
* Docker
* Spring Validation
* SpringDoc OpenAPI (Swagger)

---

## Architecture

Le service possède sa propre base de données PostgreSQL et expose une API REST destinée à être consommée via l'API Gateway de MediLabo.

```text
Client
    │
    ▼
API Gateway
    │
    ▼
Infos Patients
    │
    ▼
PostgreSQL
```

---

## Configuration

Le service est configuré à l'aide des variables d'environnement suivantes :

| Variable                 | Description                                        | Valeur par défaut |
| ------------------------ | -------------------------------------------------- | ----------------- |
| `URL_BDD`                | URL de connexion à PostgreSQL                      | -                 |
| `USERNAME_BDD`           | Nom d'utilisateur PostgreSQL                       | -                 |
| `PASSWORD_BDD`           | Mot de passe PostgreSQL                            | -                 |
| `SERVER_PORT`            | Port d'écoute de l'application                     | `8080`            |
| `API_SERVER_GATEWAY_URL` | URL publique de l'API Gateway utilisée par Swagger | -                 |
| `API_GATEWAY_PREFIX`     | Préfixe des routes exposées par la Gateway         | `/api/v1`         |

---

## Base de données

Le service utilise **PostgreSQL** avec **Spring Data JPA**.

La configuration Hibernate suivante est utilisée :

```properties
spring.jpa.hibernate.ddl-auto=update
```

Les tables sont automatiquement créées ou mises à jour au démarrage de l'application.

---

## Lancement en local

### Prérequis

* Java 21
* Maven 3.9 ou supérieur
* PostgreSQL

Pour un développement ou des tests en local, il est recommandé d'utiliser le fichier **`application-dev.properties`** contenant une configuration adaptée à un environnement local.

Exemple :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/medilabo
spring.datasource.username=admin
spring.datasource.password=password

server.port=9000

api.server.gateway.url=http://localhost:8080/api/v1
```

### Compilation

```bash
mvn clean package
```

### Exécution

```bash
mvn spring-boot:run
```

Ou après compilation :

```bash
java -jar target/*.jar
```

---

## Docker

### Construction de l'image

```bash
docker build -t infos-patients .
```

### Exécution

```bash
docker run \
  -e URL_BDD=jdbc:postgresql://host:5432/medilabo \
  -e USERNAME_BDD=postgres \
  -e PASSWORD_BDD=password \
  -p 8080:8080 \
  infos-patients
```

> **Remarque :**
> Dans le projet MediLabo, ce service est généralement lancé avec **Docker Compose** avec l'ensemble des autres microservices.

---

## API REST

| Méthode  | Endpoint        | Description                                     |
| -------- | --------------- | ----------------------------------------------- |
| `GET`    | `/patient/list` | Retourne la liste des patients                  |
| `GET`    | `/patient/{id}` | Retourne un patient à partir de son identifiant |
| `POST`   | `/patient`      | Crée un nouveau patient                         |
| `PUT`    | `/patient/{id}` | Met à jour les informations d'un patient        |
| `DELETE` | `/patient/{id}` | Supprime un patient                             |

Les opérations de création et de mise à jour utilisent **Jakarta Validation** afin de vérifier la validité des données reçues avant leur traitement.

---

## Sécurité

Le service **Infos Patients** n'implémente aucun mécanisme d'authentification ou d'autorisation.

La sécurité de l'application est assurée en amont par **l'API Gateway**, qui est responsable notamment de :

* l'authentification des utilisateurs ;
* la validation des jetons d'accès (JWT) ;
* le contrôle des accès aux différents microservices.

Ce microservice considère donc que toute requête reçue provient d'une source de confiance (la Gateway) et se concentre uniquement sur sa logique métier.

---

## Documentation Swagger

Chaque microservice MediLabo expose sa propre documentation **OpenAPI** grâce à SpringDoc.

Les chemins Swagger sont configurables dans les propriétés de l'application :

```properties
springdoc.swagger-ui.path=/doc
springdoc.api-docs.path=/patient/v3/api-docs
```

### Exécution locale

Lorsque le service est lancé localement (sans Docker), la documentation est accessible directement :

* **Swagger UI** : `http://localhost:9000/doc`
* **OpenAPI** : `http://localhost:9000/patient/v3/api-docs`

### Déploiement Docker

Dans l'architecture Docker de MediLabo, les microservices ne sont pas exposés directement.

La documentation Swagger est centralisée par l'API Gateway et accessible via :

```text
http(s)://<host>:8080/swagger-ui/index.html
```

Cette interface agrège automatiquement les documentations OpenAPI des différents microservices et constitue le point d'entrée unique pour explorer les API de l'application.

---

## Tests

Les tests peuvent être exécutés avec :

```bash
mvn test
```

Le projet utilise notamment :

* Spring Boot Test
* Spring MVC Test
* Spring Data JPA Test

---

## Structure du projet

```text
src
├── controllers
├── entitys
├── repositories
├── services
├── exceptions
└── configuration
```

---

## Intégration dans MediLabo

Le service **Infos Patients** fait partie de l'architecture microservices de **MediLabo**.

Il est appelé via l'API Gateway et fournit les informations administratives des patients aux autres services de l'application.

Chaque microservice expose sa propre spécification **OpenAPI**, tandis que l'API Gateway centralise ces documentations afin de proposer une interface Swagger unique pour l'ensemble de la plateforme.

