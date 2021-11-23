import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import "./App.css";
import * as ROUTES from "./constants/routes";
import unregisteredUserContext from "./context/unregisteredUser";
import userContext from "./context/user";
import useLocalStorage from "./hooks/useLocalStorage";
import AdminPage from "./pages/admin-page";
import CreateLobby from "./pages/create-lobby";
import EditProfile from "./pages/edit-profile";
import Game from "./pages/game";
import Home from "./pages/home";
import Lobby from "./pages/lobby";
import LobbyBrowser from "./pages/lobby-browser";
import Login from "./pages/login";
import NotFound from "./pages/not-found";
import Profile from "./pages/profile";
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
            <Route exact path={ROUTES.HOME} component={Home} />
            <Route exact path={ROUTES.SIGNUP} component={SignUp} />
            <Route exact path={ROUTES.LOGIN} component={Login} />
            <Route exact path={ROUTES.PROFILE} component={Profile} />
            <Route exact path={ROUTES.EDIT_PROFILE} component={EditProfile} />
            <Route exact path={ROUTES.CREATE_LOBBY} component={CreateLobby} />
            <Route
              exact
              path={ROUTES.BROWSE_LOBBIES}
              component={LobbyBrowser}
            />
            <Route exact path={ROUTES.LOBBY} component={Lobby} />
            <Route exact path={ROUTES.GAME} component={Game} />
            <Route exact path={ROUTES.ADMIN_PAGE} component={AdminPage} />
            <Route component={NotFound} />
            {/* All routes have "exact" since that means that the path has to match the exact string. If a route is not correct, the fallback is ROUTES.NOT_FOUND since it's the only one without "exact" */}
          </Switch>
        </Router>
      </unregisteredUserContext.Provider>
    </userContext.Provider>
  );
}
