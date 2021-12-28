import { createContext } from "react";
import { Game } from "../interfaces/Game";
/**
 * @see https://es.reactjs.org/docs/context.html
 */

export default createContext<{
  game: Game | null;
  setGame: (arg0: Game | null) => void;
}>({
  game: null,
  setGame: () => {},
});
