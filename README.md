# 📦 EasyStock (Ex-SmartShop)

EasyStock est une application web de gestion commerciale (Backend REST API) conçue pour **MicroTech Maroc**, un distributeur B2B de matériel informatique basé à Casablanca.

L’objectif est d’optimiser :
- la gestion du portefeuille client (650 clients actifs),
- la trésorerie,
- et de garantir une traçabilité complète des événements financiers.

---

## ✨ Fonctionnalités Clés

### 🧾 Gestion des Clients
- CRUD complet
- Suivi des statistiques (commandes totales, montant cumulé)
- Historique des achats

### 🎯 Système de Fidélité Automatique
- Niveaux : `BASIC`, `SILVER`, `GOLD`, `PLATINUM`
- Remises progressives appliquées automatiquement

### 📦 Gestion des Produits
- CRUD complet
- Soft delete pour préserver l’historique des commandes

### 🛒 Gestion des Commandes
- Commandes multi-produits
- Validation du stock
- Calcul automatique (HT, remises, TVA 20%, TTC)
- Mise à jour automatique des statistiques client

### 💳 Paiements Multi-Moyens
- Paiements fractionnés : ESPÈCES, CHÈQUE, VIREMENT
- Statuts : `EN_ATTENTE`, `ENCAISSÉ`, `REJETÉ`
- Une commande ne peut être **CONFIRMED** que si elle est entièrement payée

### 🔐 Matrice de Permissions
- `ADMIN` → gestion complète
- `CLIENT` → accès en lecture seule à ses propres données

---

## 🛠️ Stack Technique

| Catégorie        | Technologie / Outil       | Détails |
|------------------|----------------------------|--------|
| Framework        | Spring Boot                 | REST API |
| Langage          | Java 8+                     | Stream API, Java Time API |
| Base de données  | PostgreSQL / MySQL          | Relationnelle |
| ORM              | Spring Data JPA / Hibernate | Persistance |
| Mapping          | MapStruct                   | Entity ↔ DTO ↔ ViewModel |
| Utilitaires      | Lombok                      | Builder Pattern |
| Authentification | HTTP Session                | Sans JWT |
| Tests            | JUnit, Mockito              | Unitaires & Intégration |
| API Testing      | Postman / Swagger           | Documentation |

---

## ⚙️ Configuration

### 1. Base de données
Créez une base de données avec la commande suivante :
```sql
CREATE DATABASE easystock_db;
```

### 2. Fichier de configuration
Configurez le fichier `src/main/resources/application.properties` :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/easystock_db
spring.datasource.username=votre_utilisateur
spring.datasource.password=votre_mot_de_passe

spring.jpa.hibernate.ddl-auto=update
```

### 3. Cloner le dépôt
```bash
git clone https://github.com/BensaltanaHoussam/easystock.git
cd easystock
```

### 4. Construire le projet
```bash
mvn clean install
```

### 5. Lancer l’application
```bash
mvn spring-boot:run
```
L'API sera disponible sur `http://localhost:8080`.

---

## 🔑 Endpoints de l’API

👉 Une collection Postman ou une documentation Swagger est disponible dans le projet.

| Catégorie | Méthode | URI                        | Description                  | Rôle           |
|-----------|---------|----------------------------|------------------------------|----------------|
| Auth      | POST    | `/api/auth/login`          | Connexion                    | Tous           |
| Auth      | POST    | `/api/auth/logout`         | Déconnexion                  | Tous           |
| Clients   | POST    | `/api/clients`             | Créer un client              | ADMIN          |
| Clients   | GET     | `/api/clients/{id}`        | Consulter un client          | ADMIN / CLIENT |
| Produits  | POST    | `/api/products`            | Ajouter un produit           | ADMIN          |
| Produits  | GET     | `/api/products`            | Liste des produits           | Tous           |
| Commandes | POST    | `/api/orders`              | Créer une commande           | ADMIN          |
| Commandes | POST    | `/api/orders/{id}/confirm` | Confirmer une commande payée | ADMIN          |
| Paiements | POST    | `/api/orders/{id}/payments`| Ajouter un paiement          | ADMIN          |

---

## 🚨 Gestion des Erreurs

| Code HTTP | Description            | Exemple                    |
|-----------|------------------------|----------------------------|
| 400       | Bad Request            | Données invalides          |
| 401       | Unauthorized           | Non authentifié            |
| 403       | Forbidden              | Accès interdit             |
| 404       | Not Found              | Ressource inexistante      |
| 422       | Unprocessable Entity   | Règle métier violée        |
| 500       | Internal Server Error  | Erreur interne             |

---

## 🧑‍💻 Auteur

**HOUSSAM BENSALTANA**
- [LinkedIn](https://www.linkedin.com/in/houssam-bensaltana/)
- [GitHub](https://github.com/BensaltanaHoussam)
