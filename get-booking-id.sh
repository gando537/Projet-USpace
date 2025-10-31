#!/usr/bin/env bash
set -euo pipefail

# Usage: ./get-booking-id.sh [payload.json]
PAYLOAD="${1:-http-booking.json}"

if [[ ! -f "$PAYLOAD" ]]; then
  echo "Payload file not found: $PAYLOAD" >&2
  exit 1
fi

# Envoie la requête et récupère l'en-tête Location (redirige stderr -> stdout au cas où)
BOOKING_LOCATION=$(
  HTTP --headers POST http://localhost:8181/cruises/JUPITER_MOONS_EXPLORATION_2085/bookings @"$PAYLOAD" 2>&1 \
  | awk 'BEGIN{IGNORECASE=1} /^Location:/{print $2}' | tr -d $'\r'
)

if [[ -z "${BOOKING_LOCATION}" ]]; then
  echo "Impossible d'extraire l'en-tête Location. Réponse brute ci-dessous :" >&2
  HTTP --headers POST http://localhost:8181/cruises/JUPITER_MOONS_EXPLORATION_2085/bookings @"$PAYLOAD" 2>&1 || true
  exit 2
fi

BOOKING_ID="${BOOKING_LOCATION##*/}"
printf '%s\n' "$BOOKING_ID"
