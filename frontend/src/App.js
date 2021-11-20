import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import "./App.css";
import * as ROUTES from "./constants/routes";
import unregisteredUserContext from "./context/unregisteredUser";
import userContext from "./context/user";
import useLocalStorage from "./hooks/useLocalStorage";
import CreateGame from "./pages/create-game";
import GameBrowser from "./pages/game-browser";
import Home from "./pages/home";
import Login from "./pages/login";
import NotFound from "./pages/not-found";
import SignUp from "./pages/signup";

export default function App() {
  const [userToken, setUserToken] = useLocalStorage("token", null);
  const [unregisteredUser, setUnregisteredUser] = useLocalStorage(
    "unregisteredUser",
    null
  );

  return (
    <userContext.Provider value={{ userToken, setUserToken }}>
      <unregisteredUserContext.Provider
        value={{ unregisteredUser, setUnregisteredUser }}
      >
        <Router>
          <Switch>
            <Route exact path={ROUTES.LOGIN} component={Login} />
            <Route exact path={ROUTES.SIGNUP} component={SignUp} />
            <Route exact path={ROUTES.HOME} component={Home} />
            <Route exact path={ROUTES.CREATE_GAME} component={CreateGame} />
            <Route exact path={ROUTES.BROWSE_GAMES} component={GameBrowser} />

            <Route component={NotFound} />
            {/* All routes have "exact" since that means that the path has to match the exact string. If a route is not correct, the fallback is ROUTES.NOT_FOUND since it's the only one without "exact" */}
          </Switch>
        </Router>
      </unregisteredUserContext.Provider>
    </userContext.Provider>
  );
}
