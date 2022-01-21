/**
 *
 * @author andrsdt
 */

type Params = {
  rankingByWins?: any;
  rankingByGlory?: any;
  rankingByKills?: any;
};

type Pair = {
  first: string;
  second: number;
};

export default function Ranking(params: Params) {
  const { rankingByWins, rankingByGlory, rankingByKills } = params;

  return (
    <div className="flex justify-evenly w-full bg-felt rounded-3xl border-20 border-gray-900 p-8 divide-x-2 divide-gray-700 text-2xl ">
      <div className="flex flex-col items-center space-y-4">
        <h2 className="text-3xl font-bold">Wins</h2>
        {rankingByWins &&
          rankingByWins.map((pair: Pair) => (
            <h1 className="bg-opacity-40 bg-yellow-100 rounded-xl p-3">
              {pair.first} | 🏆 {pair.second}
            </h1>
          ))}
      </div>
      <div className="flex flex-col items-center space-y-4 pl-5">
        <h2 className="text-3xl font-bold">Glory</h2>
        {rankingByGlory &&
          rankingByGlory.map((pair: Pair) => (
            <h1 className="bg-opacity-40 bg-yellow-100 rounded-xl p-3">
              {pair.first} | 🧿 {pair.second}
            </h1>
          ))}
      </div>
      <div className="flex flex-col items-center space-y-4 pl-5">
        <h2 className="text-3xl font-bold">Kills</h2>
        {rankingByKills &&
          rankingByKills.map((pair: Pair) => (
            <h1 className="bg-opacity-40 bg-yellow-100 rounded-xl p-3">
              {pair.first} | 🔪 {pair.second}
            </h1>
          ))}
      </div>
    </div>
  );
}
