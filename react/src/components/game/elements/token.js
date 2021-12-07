export default function Token(params) {
  const { type, value, counterclockwise } = params;
  return (
    <div
      className={`hover:z-50 flex items-center justify-center token token-${type} hover:scale-200 ${
        counterclockwise !== undefined &&
        (counterclockwise
          ? "transform-gpu -rotate-90"
          : "transform-gpu rotate-90")
      }`}
    >
      <p>{value}</p>
    </div>
  );
}
