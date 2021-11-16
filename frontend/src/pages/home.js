export default function Home() {
  useEffect(() => {
    document.title = "No Time for Heroes";
  });
  return (
    <div className="home">
      <h1>Home</h1>
      <Link to={ROUTES.SIGN_UP}>
        <p>Sign up</p>
      </Link>
      <Link to={ROUTES.LOG_IN}>
        <p>Log in</p>
      </Link>
    </div>
  );
}
