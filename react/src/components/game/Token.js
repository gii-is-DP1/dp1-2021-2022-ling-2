export default function Token(params) {
  const { type, value } = params;
  return (
    <>
      <div className={`flex items-center justify-center token token-${type}`}>
        <p>{value}</p>
      </div>
    </>
  );
}
