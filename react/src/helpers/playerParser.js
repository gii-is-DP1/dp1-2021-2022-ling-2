export default function playerParser(players){
    var solution = "";
    for(var i in players){
      solution += players[i] + ", ";
    }
    return solution.substring(0, (solution.length - 2));
}