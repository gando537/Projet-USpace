[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/_kGCrB7F)
# USpace

## Mise en contexte

Le système **USpace** est une application de réservation de croisières spatiales.
Votre équipe est mandatée pour développer de nouvelles fonctionnalités dans l'application.

## Critères

1. Vous devez appliquer le Clean Code et les concepts vus en cours jusqu'à maintenant :
* Conception orientée objet
* Polymorphisme
* Inversion des dépendances
* Abstraction
* Etc!

2. Nous vous recommandons fortement d'utiliser le TDD pour les exercices.
3. Assurez-vous que l'application est toujours fonctionnel après vos modifications. 
Le code doit compiler et s'exécuter selon les instructions du `README` à la racine du projet.
**Une note de 0 sera donnée si l'application ne compile pas selon les instructions fournies.**
4. Assurez-vous que tous vos tests s'exécutent et passent avec `mvn test`.
**Une note de 0 sera donnée si les tests ne s'exécutent pas ou ne passent pas.**
5. Assurez-vous de tester unitairement tous les comportements que vous ajoutez ou modifiez.

## Prérequis

* Java JDK (au minimum la version 18)
* [Maven 3](https://maven.apache.org/download.cgi)
* Node.js (au minimum la version 18.3) et npm (au minimum la version 10) pour l'interface utilisateur du TP4 (le téléchargement de Node.js inclut npm)

## Démarrer le projet

Pour démarrer le projet, démarrer le serveur en exécutant `UspaceMain`.

Vous pouvez aussi démarrer le serveur via maven:

```bash
mvn clean install
mvn exec:java
```

## Fonctionnalités

Certaines fonctionnalités sont déjà implémentées dans le projet.

### 1. Réserver une croisière

Pour l'instant, seule la croisière "JUPITER_MOONS_EXPLORATION_2085" est disponible:
* Date et heure de départ: 2085-01-25, 12:00

**Conditions:**
* La réservation d'une croisière est possible selon la disponibilité des cabines.
* La réservation d'une croisière est possible si la date de réservation est avant la date de départ de la croisière.
* La réservation d'une croisière est possible avec au moins un voyageur.

POST cruises/{cruiseId}/bookings (cruises/JUPITER_MOONS_EXPLORATION_2085/bookings)

```
{
    "travelers": [
        {
            "name": "John Doe"::string,
            "category": "child"::string(CHILD | ADULT | SENIOR)
        },
        {
            "name": "Jane Doe"::string,
            "category": "adult"::string(CHILD | ADULT | SENIOR)
        }
    ],
    "cabinType": "Standard"::string(STANDARD | DELUXE | SUITE),
    "bookingDateTime": "2084-04-08T12:30"::string(dateTime, format ISO)
}
```

Réponses:

HTTP 201 CREATED

Headers: `Location: cruises/<cruiseId>/bookings/<bookingId>`

Si un des champs est manquant:

HTTP 400 BAD REQUEST

```
{
    "error": "MISSING_PARAMETER",
    "message": "Missing parameter: <parameterName>"
}
```

Si la bookingDate n'est pas dans le bon format:

HTTP 400 BAD REQUEST

```
{
"error": "INVALID_DATE_FORMAT",
"message": "Invalid date format"
}
```

Si la croisière n'existe pas:

HTTP 404 NOT FOUND

```
{
    "error": "CRUISE_NOT_FOUND",
    "message": "Cruise not found"
}
```

Si la date de réservation n'est pas avant le départ de la croisière:

HTTP 400 BAD REQUEST

```
{
    "error": "INVALID_PARAMETER",
    "description": "Invalid booking date."
}
```

Si le type de cabine n'est pas valide (STANDARD, DELUXE, SUITE):

HTTP 400 BAD REQUEST

```
{
    "error": "INVALID_PARAMETER",
    "description": "Invalid cabin type."
}
```

Si la catégorie d'un voyageur n'est pas valide (CHILD, ADULT, SENIOR):

HTTP 400 BAD REQUEST

```
{
    "error": "INVALID_PARAMETER",
    "description": "Invalid traveler category."
}
```

Si la réservation ne contient aucun voyageur:

HTTP 400 BAD REQUEST

```
{
    "error": "NO_TRAVELER",
    "description": "No traveler to book."
}
```

### 2. Consulter une réservation

GET cruises/{cruiseId}/bookings/{bookingId} (cruises/JUPITER_MOONS_EXPLORATION_2085/bookings/1)

Réponses:

HTTP 200 OK

```
{
    "cruiseId": "JUPITER_MOONS_EXPLORATION_2085"::string,
    "bookingId": "a6d1a050-e4e2-451a-a315-45e2d551ed33"::string,
    "travelers": [
        {
            "id": ""395e7715-74fc-468a-9795-1adc79e6148a"::string,
            "name": "John Doe"::string,
            "category": "CHILD"::string(CHILD | ADULT | SENIOR),
            "badges": ["ZERO_G"]::list(string(ZERO_G | MINI_ZERO_G | STILL_GOT_IT)) | []
        },
        {
            "id": "093f9a8d-f7c6-4d3d-b878-90452922e768"::string,
            "name": "Jane Doe"::string,
            "category": "ADULT"::string(CHILD | ADULT | SENIOR),
            "badges": ["ZERO_G"]::list(string(ZERO_G | MINI_ZERO_G | STILL_GOT_IT)) | []
        }
    ],
    "cabinType": "STANDARD"::string(STANDARD | DELUXE | SUITE),
    "bookingDateTime": "2084-04-08T12:30"::string(dateTime, format ISO)
}
```

Si la croisière n'existe pas:

HTTP 404 NOT FOUND

```
{
    "error": "CRUISE_NOT_FOUND",
    "message": "Cruise not found"
}
```

Si la réservation n'existe pas:

HTTP 404 NOT FOUND

```
{
    "error": "BOOKING_NOT_FOUND",
    "message": "Booking not found"
}
```

### 3. Consulter les détails d'une croisière

GET cruises/{cruiseId} (cruises/JUPITER_MOONS_EXPLORATION_2085)

Réponses:

HTTP 200 OK

```
{
    "id": "JUPITER_MOONS_EXPLORATION_2085"::string,
    "departureDateTime": "2085-01-25T12:00"::string(dateTime, format ISO),
    "endDateTime": "2085-02-01T12:00"::string(dateTime, format ISO),
    "planets": [
        {
            "name": "Jupiter"::string,
            "arrivalDateTime": "2085-01-26T12:00"::string(dateTime, format ISO),
            "departureDateTime": "2085-01-30T12:00"::string(dateTime, format ISO),
        },
        ...
    ]
}
```

Si la croisière n'existe pas:

HTTP 404 NOT FOUND

```
{
    "error": "CRUISE_NOT_FOUND",
    "message": "Cruise not found"
}
```