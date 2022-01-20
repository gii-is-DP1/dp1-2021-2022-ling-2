import { Player } from "../interfaces/Player";

export default function playerParser(players: Player[]): string {
  return (
    players.map((player) => player.user?.username || "deleted").join(", ") || ""
  );
}
