#!/usr/bin/env bash
set -euo pipefail

CRUISE_ID="${1:-42}"
FILL="${2:-11}"
DATE_OK="${3:-2084-03-28T09:00:00}"  # < départ 2084-04-01

attempt() {
  i="$1"

  # 1) Crée booking (1 ADULT)
  BOOKING_HEADERS=$(HTTP --headers POST :8181/cruises/$CRUISE_ID/bookings \
    Content-Type:application/json \
    <<< "{\"bookingDateTime\":\"2084-03-20T10:00:00\",\"cabinType\":\"STANDARD\",\"travelers\":[{\"name\":\"User$i\",\"category\":\"ADULT\"}]}" )
  CODE=$(printf '%s\n' "$BOOKING_HEADERS" | awk 'BEGIN{IGNORECASE=1} /^HTTP\//{print $2}')
  if [ "$CODE" != "201" ]; then
    echo "Booking creation failed (status=$CODE):"
    printf '%s\n' "$BOOKING_HEADERS"
    # Montre le body d’erreur si présent
    HTTP --body POST :8181/cruises/$CRUISE_ID/bookings Content-Type:application/json \
      <<< "{\"bookingDateTime\":\"2084-03-20T10:00:00\",\"cabinType\":\"STANDARD\",\"travelers\":[{\"name\":\"User$i\",\"category\":\"ADULT\"}]}" || true
    exit 1
  fi
  BOOKING_LOCATION=$(printf '%s\n' "$BOOKING_HEADERS" | awk 'BEGIN{IGNORECASE=1} /^Location:/{print $2}' | tr -d '\r')
  BOOKING_ID="${BOOKING_LOCATION##*/}"

  # 2) Récupère travelerId
  BODY=$(HTTP --body GET :8181/cruises/$CRUISE_ID/bookings/$BOOKING_ID)
  TRAVELER_ID=$(printf '%s' "$BODY" | sed -n 's/.*"travelers":[[][{][^}]*"id":"\([^"]*\)".*/\1/p')
  if [ -z "${TRAVELER_ID:-}" ]; then
    echo "Impossible d'extraire travelerId. Réponse brute :"
    printf '%s\n' "$BODY"
    exit 1
  fi

  echo "$BOOKING_ID $TRAVELER_ID" > /tmp/last_zeroG_ids

  # 3) Réserve Zero-G
  STATUS=$(HTTP --headers POST :8181/cruises/$CRUISE_ID/bookings/$BOOKING_ID/travelers/$TRAVELER_ID/zeroGravityExperiences \
    Content-Type:application/json \
    <<< "{\"experienceBookingDateTime\":\"$DATE_OK\"}" \
    | awk 'BEGIN{IGNORECASE=1} /^HTTP\//{print $2}')
  echo "$STATUS"
}

# Remplissage
for i in $(seq 1 $((FILL-1))); do
  code=$(attempt "$i")
  [ "$code" = "200" ] || { echo "Unexpected: $code (iteration $i)"; exit 1; }
  echo "OK ($i)"
done

code=$(attempt "$FILL")
echo "Final: $code"

# Si ce n'est pas 200, on rejoue la DERNIÈRE requête en verbeux pour afficher le JSON d'erreur
if [ "$code" != "200" ]; then
  read LAST_BOOKING_ID LAST_TRAVELER_ID < /tmp/last_zeroG_ids
  echo "Replaying last request verbosely to show error body..."
  HTTP -v POST ":8181/cruises/$CRUISE_ID/bookings/$LAST_BOOKING_ID/travelers/$LAST_TRAVELER_ID/zeroGravityExperiences" \
    Content-Type:application/json \
    <<< "{\"experienceBookingDateTime\":\"$DATE_OK\"}"
fi