# 🍺 Barathon Event System

Application web pour l'organisation et le déroulement de **barathons** - des tournées de bars en équipe avec défis, épreuves et challenges.

## 👥 À propos

Projet réalisé par **4 étudiants** pour la L3 MIAGE. Application complète de gestion d'événement avec backend Spring Boot.

## 🎯 Fonctionnalités

- 🏚️ **Gestion des bars** - Capacité, horaires, adresse
- 👥 **Gestion des participants** - Équipes, SAM (conducteur désigné)
- ⚔️ **Épreuves** - Compétitions entre équipes dans les bars
- 🎪 **Challenges** - Défis à réaliser en équipe partout en ville
- 🎖️ **Système de points** - Scoring automatique des équipes
- 🚨 **Incidents** - Signalement et gestion des problèmes
- 📊 **API REST** - Endpoints complets pour toutes les opérations

## 🛠️ Technologies

- **Java 21** - Langage principal (100%)
- **Spring Boot 4.0.1** - Framework backend
- **Spring Data JPA** - Persistance des données
- **H2 Database** - Base de données
- **Maven** - Build et gestion des dépendances
- **Swagger/OpenAPI** - Documentation API

## 🚀 Installation

```bash
# 1. Cloner le repository
git clone https://github.com/kikflash21/BarathonEventSystem.git
cd BarathonEventSystem

# 2. Compiler et tester
mvn clean package

# 3. Lancer le serveur
cd server
mvn spring-boot:run
```

## 📡 Architecture

**Structure Maven multi-modules :**
- `rest-api` - Définition des DTOs et interfaces
- `server` - Backend Spring Boot avec contrôleurs et services

## 💡 Points forts

- Architecture en couches (Controller → Service → Repository)
- Tests unitaires avec JUnit 5 et coverage JaCoCo
- Validation des données avec Spring Validation
- Mappers automatiques avec MapStruct
- Documentation API avec Swagger/OpenAPI

---

**Contributeurs** : Projet étudiant L3 MIAGE

**Licence** : MIT
