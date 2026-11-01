#!/usr/bin/env bash
# Crawls every page listed in AppMenu.java and fails on HTTP errors or error-page / stack-trace signatures.
# Usage: crawl.sh [baseUrl]   (default http://localhost:8080/showcase; use http://host:port for a ROOT context)
BASE="${1:-http://localhost:8080/showcase}"
BASE="${BASE%/}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MODULE_DIR="$(cd "$SCRIPT_DIR/../../.." && pwd)"
MENU="$MODULE_DIR/src/main/java/org/primefaces/extensions/showcase/menu/AppMenu.java"

# URLs whose demo text legitimately contains an error signature (exact URL paths).
WHITELIST=()

ERROR_REGEX='Error Page|ViewExpiredException:|could not be restored|jakarta\.faces\.[A-Za-z.]*Exception:|jakarta\.el\.[A-Za-z.]*Exception:|java\.lang\.[A-Za-z.]*(Exception|Error):|\bat (org|jakarta|java)\.[A-Za-z0-9_.$]+\([A-Za-z0-9_]+\.java:[0-9]+\)|An error has occurred|Whitelabel'

URLS=("/")
[ -f "$MODULE_DIR/src/main/webapp/taglib.xhtml" ] && URLS+=("/taglib")
while IFS= read -r u; do URLS+=("$u"); done < <(grep -o '"/ui/[^"]*"' "$MENU" | tr -d '"' | sort -u)

# standalone demo pages linked from ui pages: .../standalone/<name> -> extensionless URL. Include fragments (no <html>) are not pages and are skipped.
while IFS= read -r u; do
  f="$MODULE_DIR/src/main/webapp$u.xhtml"
  [ -f "$f" ] && ! grep -q '<html' "$f" && continue
  URLS+=("$u")
done < <(grep -rhoE "/ui/[^\"'# ]*/standalone/[^\"'# ?]+" "$MODULE_DIR/src/main/webapp/ui" --include=*.xhtml | sed -E 's/\.xhtml$//' | sort -u)

TOTAL=${#URLS[@]}
OK=0
FAIL=0
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT

for u in "${URLS[@]}"; do
  jar="$TMP/jar.txt"; body="$TMP/body.html"
  rm -f "$jar"
  code=$(curl -s --max-time 60 -L -c "$jar" -b "$jar" -o "$body" -w '%{http_code}' "$BASE$u")
  reason=""
  if [ "$code" != "200" ]; then
    reason="HTTP $code"
  else
    skip=0
    for w in "${WHITELIST[@]}"; do [ "$w" = "$u" ] && skip=1; done
    if [ $skip -eq 0 ] && match=$(grep -aEo "$ERROR_REGEX" "$body" | head -1) && [ -n "$match" ]; then
      reason="error signature: $match"
    fi
  fi
  if [ -z "$reason" ]; then
    OK=$((OK + 1))
  else
    FAIL=$((FAIL + 1))
    echo "FAIL $u ($reason)"
  fi
done

echo "OK $OK/$TOTAL"
[ $FAIL -eq 0 ] || exit 1
