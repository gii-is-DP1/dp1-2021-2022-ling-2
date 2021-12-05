export default function CenterZone(params) {
  return (
    <div className="grid grid-rows-3 h-full">
      <span className="bg-red-100 grid grid-cols-6 gap-2 py-1">
        {/* Market cards pile (facing down) */}
        <span className="col-start-1 bg-yellow-400">
          MARKET CARDS (FACING DOWN)
        </span>
        {/* Market cards */}
        <span className="bg-yellow-400">MARKET CARD 1</span>
        <span className="bg-yellow-400">MARKET CARD 2</span>
        <span className="bg-yellow-400">MARKET CARD 3</span>
        <span className="bg-yellow-400">MARKET CARD 4</span>
        <span className="bg-yellow-400">MARKET CARD 5</span>
      </span>
      <span className="bg-green-100 grid grid-cols-6 gap-2 py-1">
        <span className="col-start-3 bg-green-400">
          {/* HordeEnemy pile (facing up), warlord card on top a bit displaced (facing down) */}
          HORDE ENEMY PILE (FACING UP)
        </span>
        <span className="col-start-5 bg-green-400">
          {/* Scene pile, Current scene */}
          CURRENT SCENE (FACING UP)
        </span>
        <span className="bg-green-400"> SCENE PILE (FACING DOWN) </span>
      </span>
      <span className="bg-blue-100 grid grid-cols-6 gap-2 py-1">
        {/* Fighting HordeEnemies (0 to 3) and optionally a warlord (0 to 1) */}
        <span className="col-start-2 bg-blue-400">HORDE ENEMY 1</span>
        <span className="bg-blue-400">HORDE ENEMY 2</span>
        <span className="bg-blue-400">HORDE ENEMY 3</span>
        <span className="bg-blue-400">WARLORD</span>
      </span>
    </div>
  );
}
