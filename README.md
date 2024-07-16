# Système de Gestion de Tickets

Ce projet est un système de gestion de tickets simple développé en utilisant Java et Spring Framework. Il permet aux utilisateurs de créer, lire, mettre à jour, assigner et supprimer des tickets.

## Table des matières
- [Fonctionnalités principales](#fonctionnalités-principales)
- [Installation](#installation)
- [Configuration](#configuration)
- [Démarrage du projet](#démarrage-du-projet)
- [Documentation de l'API](#documentation-de-lapi)
- [Sécurisation des APIs REST](#sécurisation-des-apis-rest)
- [Exécution des tests](#exécution-des-tests)

## Fonctionnalités principales

### Gestion des Utilisateurs
- **Créer un utilisateur** : Permet de créer un nouvel utilisateur avec un nom d'utilisateur, un email et un mot de passe.
- **Récupérer tous les utilisateurs** : Permet de récupérer la liste de tous les utilisateurs enregistrés.
- **Récupérer un utilisateur par son ID** : Permet de récupérer les détails d'un utilisateur spécifique en utilisant son ID.
- **Modifier un utilisateur** : Permet de mettre à jour les informations d'un utilisateur existant.
- **Récupérer les tickets d'un utilisateur** : Permet de récupérer la liste des tickets assignés à un utilisateur spécifique.
- **Supprimer un utilisateur** : Permet de supprimer un utilisateur spécifique en utilisant son ID.

### Gestion des Tickets
- **Créer un ticket** : Permet de créer un nouveau ticket avec un titre, une description et un statut (en cours, terminé, annulé).
- **Récupérer tous les tickets** : Permet de récupérer la liste de tous les tickets.
- **Récupérer un ticket par ID** : Permet de récupérer les détails d'un ticket spécifique en utilisant son ID.
- **Mettre à jour un ticket** : Permet de mettre à jour les informations d'un ticket existant.
- **Assigner un ticket à un utilisateur** : Permet d'assigner un ticket spécifique à un utilisateur.
- **Supprimer un ticket** : Permet de supprimer un ticket spécifique en utilisant son ID.


## Installation

Avant de commencer, assurez-vous d'avoir les éléments suivants installés sur votre système :

- Java Development Kit (JDK version 17)
- Maven (version 3 ou ultérieure)
- Un environnement de développement intégré (IDE) tel qu'Eclipse ou IntelliJ IDEA (facultatif, mais recommandé)
- Un outil pour tester les APIs (Postman)
- Un système de contrôle de version distribué (Git)

## Lancement depuis un IDE

1. **Rapatrier le projet** : Clonez le projet depuis le dépôt `https://github.com/Zoyim/ticketsystem.git`

2. **Ouvrir l'IDE** : Lancez votre environnement de développement intégré (IDE) préféré.

3. **Importer le projet** : Importez le projet ticketsystem dans votre IDE.

4. **Installer les dépendances** : Exécutez la commande `mvn install` pour installer les dépendances et construire le projet.


## Configuration

### Utilisation de H2

Le projet utilise la base de données en mémoire H2 pour le stockage. Les configurations par défaut sont définies dans le fichier `src/main/resources/application.yaml`.

```yaml
	spring:
	  application:
		name: ticketsystem

	  datasource:
		url: jdbc:h2:mem:gesticket_db
		driverClassName: org.h2.Driver
		username: sa
		password:

	  h2:
		console:
		  enabled: true

	  jpa:
		database-platform: org.hibernate.dialect.H2Dialect

	server:
	  port: 8081
	  servlet:
		context-path: /api
```
			
## Démarrage du projet

1. Configurer la sécurité :

Nous allons démarrer le projet et faire les tests avec des APIs REST non sécurisé.

- Ouvrez le fichier de configuration de sécurité `SecurityConfig.java` qui se trouver dans le package `af.cmr.loti.ticketsystem.ws.config`.
- Modifier `.anyRequest().permitAll()` pour autoriser toutes les requêtes.

    ```java
		SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((request) -> request
					.requestMatchers("/token/generateToken", "/swagger-ui/**", "/v3/api-docs/**", "/error/**").permitAll()
					.requestMatchers(HttpMethod.POST, "/users").permitAll()
					.anyRequest().permitAll())
					.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).build();
	}
	```
2. Démarrer l'application
	
- Naviguez dans la classe principale `TicketsystemApplication.java` qui se trouve dans le package `af.cmr.loti.ticketsystem.boot`.
- Faites un clique droit, puis `Run AS` et afin vous clique sur `Java Application`.


## Documentation de l'API

La documentation de l'API est disponible via Swagger, qui permet de visualiser et d'interagir avec les APIs REST du projet de manière conviviale.

### Accéder à Swagger UI

1. **Démarrer l'application** :
    - Assurez-vous que l'application est en cours d'exécution. Si ce n'est pas déjà fait, démarrez-la en suivant les instructions de la section [Démarrage du projet](#démarrage-du-projet).

2. **Ouvrir Swagger UI** :
    - Ouvrez votre navigateur web préféré.
    - Accédez à l'URL suivante : `http://localhost:8081/api/swagger-ui/index.html`

### Utilisation de Swagger UI

- **Visualisation des Endpoints** :
  - Tous les endpoints disponibles pour la gestion des utilisateurs et des tickets sont listés.
  - Vous pouvez voir les détails de chaque endpoint, y compris les paramètres, les réponses possibles et les exemples de requêtes.

- **Tester les Endpoints** :
  - Vous pouvez directement tester les endpoints depuis l'interface Swagger UI.
  - Cliquez sur un endpoint pour voir les détails et utilisez le bouton `Try it out` pour exécuter une requête.

### Authentification avec Swagger UI

Pour tester les endpoints sécurisés :

1. **Générer un Token JWT** :
    - Utilisez l'endpoint `/token/generateToken` pour obtenir un token JWT.
    - Faites une requête POST avec les informations d'authentification de l'utilisateur.

2. **Ajouter le Token JWT dans Swagger UI** :
    - Cliquez sur le bouton `Authorize` en haut à droite de l'interface Swagger UI.
    - Entrez `Bearer <votre_token>` dans le champ de l'autorisation et cliquez sur `Authorize`.
    - Maintenant, toutes les requêtes effectuées depuis Swagger UI incluront ce token JWT pour l'authentification.

### Exemples d'Endpoints

- **Créer un utilisateur** : POST `/users`
- **Récupérer tous les utilisateurs** : GET `/users`
- **Créer un ticket** : POST `/tickets`
- **Récupérer tous les tickets** : GET `/tickets`

Pour plus de détails, référez-vous directement à la documentation Swagger UI.

###. Tester les APIs avec Postman :
  
- Utilisez Postman pour effectuer les mêmes scénarios de la section [Documentation de l'API](#documentation-de-lapi).

### Sécurisation des APIs REST

Maintenant que vous avez finis de faire les tests fonctionnels non sécurisé, nous allons dès à présent nous interressé sur les tests APIs REST sécurisés

1. Modifier la configuration de sécurité :

- Modifiez `SecurityConfig.java` pour exiger l'authentification sur toutes les requêtes.
	
	```java
		SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((request) -> request
					.requestMatchers("/token/generateToken", "/swagger-ui/**", "/v3/api-docs/**", "/error/**").permitAll()
					.requestMatchers(HttpMethod.POST, "/users").permitAll()
					.anyRequest().authenticated())
					.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).build();
	}
	```
	
2. Tester les APIs sécurisées :

- Avec l'outil postman Créez un nouvel utilisateur sans authentification.

- Authentifiez-vous en utilisant le lien http://localhost:8081/api/token/generateToken avec la méthode POST.

- Ajoutez le token JWT dans le header des requêtes suivantes :

    ```java
		Authorization: Bearer <votre_token>
    ```
	
- Faites des tests fonctionnels de manière sécurisé.

## Exécution des tests

1. Tests unitaires :

- Faites un clic droit sur le projet `ticketsystem`, sélectionnez Run AS et Maven test.

- Alternativement, ouvrez le terminal, accédez au projet `ticketsystem` et exécutez la commande `mvn test`.

- Les résultats des tests seront affichés dans la console.