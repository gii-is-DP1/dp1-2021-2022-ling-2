type Params = {
  charactersPlayed?: {
    [key: string]: number;
  };
  charactersWinRates?: {
    [key: string]: any;
  };
};

export default function CharacterStatisticsTable(params: Params) {
  const { charactersPlayed, charactersWinRates } = params;
  const characterList = Object.keys(charactersWinRates ?? {});
  return (
    <div className="flex flex-col">
      <div className="overflow-x-auto">
        <div className="py-2 align-middle inline-block min-w-full">
          <div className="shadow overflow-hidden border-b border-gray-900 rounded-xl">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-800">
                <tr>
                  <th scope="col" className="text-table-th">
                    Character
                  </th>
                  <th scope="col" className="text-table-th">
                    Times played
                  </th>
                  <th scope="col" className="text-table-th">
                    Win rate
                  </th>
                </tr>
              </thead>
              <tbody className="bg-gray-900 divide-y divide-gray-200 text-center">
                {characterList.map((character) => (
                  <tr key={character}>
                    <td className="text-table-td">{character}</td>
                    <td className="text-table-td">
                      {(charactersPlayed && charactersPlayed[character]) ||
                        "--"}
                    </td>
                    <td className="text-table-td">
                      {charactersWinRates &&
                      charactersWinRates[character] !== "NaN"
                        ? (100 * charactersWinRates[character]).toFixed(2) + "%"
                        : "--"}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
