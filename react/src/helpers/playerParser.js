export default function playerParser(players) {
  return players.map((player) => player.user.username).join(", ");
}
