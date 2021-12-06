export default function Token(params) {
  const { type, value } = params;
  return (
    <>
      <div
        className={`flex items-center justify-center token token-${type} hover:scale-200`}
      >
        <p>{value}</p>
      </div>
    </>
  );
}
