#!/bin/bash

# Starta ett nytt spel med två spelare
echo "Starting new game..."
curl -X POST http://localhost:8080/api/game/start \
  -H "Content-Type: application/json" \
  -d '["Player1", "Player2"]'

echo -e "\n\nGetting Player1 status..."
curl http://localhost:8080/api/game/player/Player1

echo -e "\n\nPlaying a card..."
curl -X POST "http://localhost:8080/api/game/play?playerName=Player1&cardName=Print%20Function"

echo -e "\n\nStarting next round..."
curl -X POST http://localhost:8080/api/game/next-round
