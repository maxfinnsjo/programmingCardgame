# Testing the Game API

## Start a new game
```bash
curl -X POST http://localhost:8080/api/game/start \
  -H "Content-Type: application/json" \
  -d '["Player1", "Player2"]'
```

## Get player status
```bash
curl http://localhost:8080/api/game/player/Player1
```

## Play a card
```bash
curl -X POST "http://localhost:8080/api/game/play?playerName=Player1&cardName=Print%20Function"
```

## Start next round
```bash
curl -X POST http://localhost:8080/api/game/next-round
