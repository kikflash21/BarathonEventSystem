# 🍺 Barathon Event System

**Système d'information pour l'organisation et le déroulement de Barathons** - Une application web complète permettant de gérer des tournées de bars en équipe avec des défis, épreuves et un système de scoring.

## 👥 À propos

Projet réalisé par **4 étudiants** en L3 MIAGE. Application backend robuste avec Spring Boot pour orchestrer une compétition ludique multi-équipes dans un réseau de bars partenaires.

---

## 📋 Le Sujet - Système de Barathon

### Qu'est-ce qu'un Barathon ?

Un **barathon** est une tournée des bars d'une ville qui se déroule en équipe. Les participants peuvent :
- Obtenir des **consommations gratuites** des bars partenaires
- Participer à des **épreuves** (jeux/défis) dans les bars
- Réaliser des **challenges** partout en ville
- Accumuler des **points** et progresser dans un classement

### Vision du Système

Ce projet modélise et gère tous les aspects d'un barathon :
- **Organisateurs** : Personnel chargé de l'organisation
- **Superviseurs** : Médiateurs et secouristes sur place
- **Participants** : Organisés en équipes avec un SAM (conducteur désigné)
- **Bars partenaires** : Avec capacité et horaires
- **Activités** : Challenges et épreuves avec système de points

---

## 🏗️ Modélisation UML

Voici le diagramme d'entités du système :

![Diagramme UML - Architecture du Barathon](./doc/situation.png)

### Principales Entités

| Entité | Description |
|--------|------------|
| **Barathon** | Édition d'un barathon dans une ville (Années 80, Fiesta Latina, etc.) |
| **Équipe** | Groupe de 2-10 participants avec un SAM obligatoire |
| **Participant** | Personne avec pseudo, email unique, téléphone et date de naissance |
| **Organisateur** | Personnel d'organisation (BDE, asso, syndicat) avec CV |
| **Superviseur** | Médiateur ou secouriste avec numéro d'urgence |
| **Bar** | Partner avec capacité max, adresse, horaires |
| **Challenge** | Défi à réaliser en équipe (description, points, hashtag) |
| **Épreuve** | Compétition entre 2 équipes dans un bar (type, participants max, points) |
| **ParticipationEpreuve** | Suivi d'une participation à une épreuve (statut, équipes A/B, vainqueur) |
| **Incident** | Problème signalé avec date et motif |

### Relations Clés

- 1 Barathon → N Équipes
- 1 Équipe → 2-10 Participants (min 1 SAM)
- 1 Barathon → N Bars partenaires
- 1 Bar → N Épreuves
- 1 Équipe → N Participations à Épreuves
- 1 Superviseur → N Incidents

---

## 🎯 Fonctionnalités Implémentées

### Gestion des Entités
- ✅ CRUD complets pour Barathons, Équipes, Participants
- ✅ Gestion des Bars partenaires et horaires
- ✅ Organisateurs et Superviseurs avec rôles
- ✅ Challenges et Épreuves avec système de points

### Système de Participation
- ✅ Inscription des équipes aux barathons
- ✅ Participation aux épreuves (statuts : inscrit → en cours → terminée)
- ✅ Réalisation des challenges avec hashtags
- ✅ Scoring automatique et classement

### Gestion des Incidents
- ✅ Signalement d'incidents par superviseurs
- ✅ Incidents impliquant participants, épreuves ou challenges
- ✅ Déduction de points pour incidents sur challenges

### API REST Complète
- ✅ Endpoints pour toutes les opérations CRUD
- ✅ Gestion des équipes : création, ajout de participants
- ✅ Gestion des bars : inscription/désinscription
- ✅ Incidents : création et suivi
- ✅ Vérification de l'état des équipes

---

## 🛠️ Stack Technologique

| Technologie | Utilisation |
|------------|-------------|
| **Java 21** | Langage principal - POO, Streams, Records |
| **Spring Boot 4.0.1** | Framework web et framework d'application |
| **Spring Data JPA** | Persistance et gestion des données |
| **Spring Web** | APIs REST et contrôleurs |
| **H2 Database** | Base de données en mémoire pour tests |
| **Maven** | Build et gestion des dépendances |
| **JUnit 5** | Framework de test unitaire |
| **Lombok** | Réduction du boilerplate (getters, setters) |
| **MapStruct** | Mapping automatique DTO ↔ Entités |
| **Swagger/OpenAPI** | Documentation interactive de l'API |
| **JaCoCo** | Mesure de couverture de code |

---

## 🏛️ Architecture

**Structure Maven multi-modules :**

```
BarathonEventSystem/
├── rest-api/              # Module DTOs et interfaces
│   ├── DTOs des entités
│   ├── Swagger annotations
│   └── Contracts API
│
├── server/                # Module backend Spring Boot
│   ├── Controllers/       # Endpoints REST
│   ├── Services/          # Logique métier
│   ├── Repositories/      # Accès aux données JPA
│   ├── Entities/          # Modèle de données
│   ├── Mappers/           # MapStruct
│   ├── Exceptions/        # Gestion erreurs personnalisées
│   └── Tests/             # Tests unitaires
│
└── pom.xml               # Configuration parent Maven
```

### Couches d'Application

```
┌─────────────────────┐
│   REST Controllers  │  ← Endpoints HTTP
├─────────────────────┤
│    Services         │  ← Logique métier
├─────────────────────┤
│   Repositories JPA  │  ← Accès DB
├─────────────────────┤
│    Entities JPA     │  ← Modèle relationnel
├─────────────────────┤
│   H2 Database       │  ← Persistance
└─────────────────────┘
```

---

## 🚀 Installation et Démarrage

### Prérequis
- Java 21 JDK
- Maven 3.8+

### Étapes

```bash
# 1. Cloner le repository
git clone https://github.com/kikflash21/BarathonEventSystem.git
cd BarathonEventSystem

# 2. Compiler le projet complet
mvn clean package

# 3. Lancer le serveur Spring Boot
cd server
mvn spring-boot:run

# Le serveur démarrera sur http://localhost:8080
# Swagger UI disponible sur http://localhost:8080/swagger-ui.html
```

### Exécuter les Tests

```bash
# Tests unitaires avec couverture JaCoCo
mvn clean test

# Rapport de couverture généré dans target/site/jacoco/
```

---

## 📡 Endpoints Principaux

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/equipe/check` | Vérifier l'état de toutes les équipes |
| POST | `/api/epreuve/{id}` | Créer une participation à une épreuve |
| PUT | `/api/teams/{teamsName}/participants` | Ajouter un participant à une équipe |
| DELETE | `/barathon/{barathonId}/bars/{barId}` | Désinscrire un bar du barathon |
| PUT | `/api/incidents/challenge/{challengeId}` | Rapporter un incident sur un challenge |

---

## 💡 Compétences Démontrées

- ✨ **Modélisation UML** et analyse de besoins
- ✨ **Architecture en couches** (MVC/layered)
- ✨ **Programmation objet avancée** en Java
- ✨ **Spring Boot expertise** (JPA, REST, Validation)
- ✨ **Gestion des transactions** et intégrité des données
- ✨ **Tests unitaires** et méthodes de validation
- ✨ **Design patterns** (Repository, DTO, Mapper)
- ✨ **API REST bien documentée** (Swagger/OpenAPI)
- ✨ **Travail collaboratif** en équipe de 4
- ✨ **Gestion de la complexité** métier

---

## 📦 Dépendances Clés

### Parent
- `spring-boot-starter-parent` 4.0.1

### Modules
- `spring-boot-starter-web` - Web framework
- `spring-boot-starter-data-jpa` - ORM Hibernate
- `spring-boot-starter-validation` - Validation des données
- `spring-boot-starter-actuator` - Health checks et métriques
- `com.h2database` - Base de données test
- `org.projectlombok` - Réduction boilerplate
- `org.mapstruct` - Mapping automatique
- `org.springdoc` - Intégration OpenAPI/Swagger

---

## 📝 Licence

MIT License - Projet éducatif

---

**Contributeurs** : Projet collectif L3 MIAGE Grenoble

**Dernière mise à jour** : Mai 2026
