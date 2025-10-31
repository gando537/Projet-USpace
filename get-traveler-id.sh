#!/usr/bin/env bash
set -euo pipefail

# Usage: ./get-traveler-id.sh <bookingId>
# Optionnel: export CRUISE_ID si tu veux autre chose que la croisière seedée.
CRUISE_ID="${CRUISE_ID:-JUPITER_MOONS_EXPLORATION_2085}"

if [[ $# -lt 1 ]]; then
  echo "Usage: $0 <bookingId>" >&2
  exit 1
fi

BOOKING_ID="$1"

# Récupère le JSON de la booking (seulement le body)
BOOKING_JSON=$(
  HTTP --body GET "http://localhost:8181/cruises/${CRUISE_ID}/bookings/${BOOKING_ID}" 2>&1 || true
)

# ---- Méthode demandée: sed (premier traveler) ----
TRAVELER_ID=$(printf '%s' "$BOOKING_JSON" \
  | sed -n 's/.*"travelers":[[][{][^}]*"id":"\([^"]*\)".*/\1/p')

# (Optionnel) si jq est dispo, on fiabilise :
if [[ -z "${TRAVELER_ID}" ]] && command -v jq >/dev/null 2>&1; then
  TRAVELER_ID=$(printf '%s' "$BOOKING_JSON" | jq -r '.travelers[0].id // empty')
fi

if [[ -z "${TRAVELER_ID}" ]]; then
  echo "Impossible d'extraire travelerId. Réponse brute :" >&2
  printf '%s\n' "$BOOKING_JSON" >&2
  exit 2
fi

printf '%s\n' "$TRAVELER_ID"
