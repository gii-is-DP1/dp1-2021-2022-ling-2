export default function PlayerNames(params) {
  const { players } = params;

  return (
    <div className="absolute text-white text-3xl h-screen py-6 px-20 w-full flex flex-col-reverse justify-between content-between">
      <div className="flex justify-between">
        <p>{players[0] && players[0].user.username}</p>
        <p>{players[1] && players[1].user.username}</p>
      </div>
      <div className="flex justify-between">
        <p>{players[2] && players[2].user.username}</p>
        <p>{players[3] && players[3].user.username}</p>
      </div>
    </div>
  );
}
