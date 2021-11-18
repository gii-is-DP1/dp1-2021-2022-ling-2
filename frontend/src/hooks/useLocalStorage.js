import { useState, useEffect } from "react";

/**
 * Hook that handles LocalStorage access
 *
 * @param key Key that we want to fetch from LocalStorage
 * @param defaultValue default value that we want to store and use as initial if the key is not found
 * @author andrsdt
 * @see https://blog.logrocket.com/using-localstorage-react-hooks/
 */
export const useLocalStorage = (key, defaultValue) => {
  const [value, setValue] = useState(() => {
    // Aux function to get the initial value, or the default one if non existing
    const saved = localStorage.getItem(key);
    const initial = JSON.parse(saved);
    return initial || defaultValue;
  });

  useEffect(() => {
    // Storing input value in localStorage.
    // The function in useEffect every time "setValue" is called
    localStorage.setItem(key, JSON.stringify(value));
  }, [key, value]);

  return [value, setValue];
};
