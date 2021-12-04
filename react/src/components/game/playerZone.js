import { useState, useEffect } from "react";
import Token from "./Token";

export default function PlayerZone(params) {
  const [player, setPlayer] = useState(params.player);
  const reverse = params.reverse;

  useEffect(() => {
    const fetchPlayer = async (player) => {
      //   const response = await fetch(`/players/${player}`);
      //   const data = await response.json();
      //   setPlayer(data);
    };
    fetchPlayer();
  }, []);
  return (
    <div className={`flex ${reverse && "flex-row-reverse"} h-full`}>
      <div className="flex flex-col justify-end space-y-2">
        <Token type="kill" value={player.kills} />
        <Token type="gold" value={player.gold} />
        <Token type="glory" value={player.glory} />
      </div>
    </div>
  );
}
