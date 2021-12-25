import { createContext } from "react";
/**
 * @see https://es.reactjs.org/docs/context.html
 */

export default createContext<{
  selected: any[];
  addSelect: (arg0: any) => void;
  removeSelect: (arg0: any) => void;
}>({
  selected: [],
  addSelect: () => {},
  removeSelect: () => {},
});
