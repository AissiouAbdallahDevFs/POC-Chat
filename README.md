## Application de Chat WebSocket

Cette application permet de discuter en temps réel à l'aide de WebSocket. Elle se compose de deux parties principales :

1. **Serveur Spring Boot** : Gère les connexions WebSocket et la logique des messages.
2. **Client Angular** : Interface utilisateur pour discuter et afficher les messages.

### Lancer le Serveur

#### 1. Serveur Spring Boot

1. **Installer les dépendances** :
   - Assurez-vous d'avoir [Maven](https://maven.apache.org/install.html) installé.

2. **Lancer le serveur** :
   - Ouvrez un terminal dans le dossier du projet Spring Boot.
   - Exécutez la commande suivante :
     ```bash
     mvn spring-boot:run
     ```

   Cela démarrera le serveur et le rendra disponible sur `http://localhost:8080`.

#### 2. Client Angular

1. **Installer les dépendances** :
   - Assurez-vous d'avoir [Node.js](https://nodejs.org/) et [npm](https://www.npmjs.com/get-npm) installés.

2. **Démarrer l'application Angular** :
   - Ouvrez un terminal dans le dossier du projet Angular.
   - Exécutez les commandes suivantes :
     ```bash
     npm install
     ng serve
     ```

   Cela démarre le client Angular et le rendra disponible sur `http://localhost:4200`.

### Base de Données avec Docker

Pour configurer et lancer la base de données avec Docker, vous devez utiliser `docker-compose`.

1. **Installer Docker et Docker Compose** :
   - Suivez les instructions sur le [site de Docker](https://docs.docker.com/get-docker/) pour installer Docker et Docker Compose.

2. **Lancer Docker Compose** :
   - Ouvrez un terminal dans le dossier contenant le fichier `docker-compose.yml` pour la base de données.
   - Exécutez la commande suivante :
     ```bash
     docker-compose up
     ```

   Cela construira et démarrera les conteneurs Docker pour la base de données.

### Résumé

- **Serveur Spring Boot** : Lancez avec `mvn spring-boot:run`.
- **Client Angular** : Lancez avec `npm install` puis `ng serve`.
- **Base de Données** : Lancez avec `docker-compose up`.

