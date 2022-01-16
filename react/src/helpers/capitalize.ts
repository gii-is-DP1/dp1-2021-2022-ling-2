const capitalize = (s: string | undefined): string =>
  (s && s[0].toUpperCase() + s.slice(1)) || "";
export default capitalize;
