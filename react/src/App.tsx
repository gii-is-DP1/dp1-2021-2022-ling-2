import { Toaster } from "react-hot-toast";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import "./App.css";
import * as ROUTES from "./constants/routes";
import unregisteredUserContext from "./context/unregisteredUser";
import userContext from "./context/user";
import useLocalStorage from "./hooks/useLocalStorage";
import AdminPage from "./pages/admin-page";
import AllAchievements from "./pages/all-achievements";
import CreateAchievement from "./pages/create-achievement";
import CreateLobby from "./pages/create-lobby";
import EditAchievement from "./pages/edit-achievement";
import EditProfile from "./pages/edit-profile";
import GameRouter from "./pages/game-router";
import Home from "./pages/home";
import LobbyBrowser from "./pages/lobby-browser";
import Login from "./pages/login";
import NotFound from "./pages/not-found";
import Profile from "./pages/profile";
import Ranking from "./pages/ranking";
import SignUp from "./pages/signup";
import Statistics from "./pages/statistics";
import UserAchievements from "./pages/user-achievements";
import UserStatistics from "./pages/user-statistics";

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
        <Toaster />
        <Router>
          <Switch>
            <Route exact path={ROUTES.HOME} component={Home} />
            <Route exact path={ROUTES.SIGNUP} component={SignUp} />
            <Route exact path={ROUTES.LOGIN} component={Login} />
            <Route exact path={ROUTES.PROFILE} component={Profile} />
            <Route exact path={ROUTES.EDIT_PROFILE} component={EditProfile} />
            <Route
              exact
              path={ROUTES.EDIT_ACHIEVEMENT}
              component={EditAchievement}
            />
            <Route exact path={ROUTES.CREATE_GAME} component={CreateLobby} />
            <Route
              exact
              path={ROUTES.ALL_ACHIEVEMENTS}
              component={AllAchievements}
            />
            <Route
              exact
              path={ROUTES.USER_ACHIEVEMENTS}
              component={UserAchievements}
            />
            <Route
              exact
              path={ROUTES.CREATE_ACHIEVEMENT}
              component={CreateAchievement}
            />
            <Route exact path={ROUTES.STATISTICS} component={Statistics} />
            <Route
              exact
              path={ROUTES.USER_STATISTICS}
              component={UserStatistics}
            />
            <Route exact path={ROUTES.RANKING} component={Ranking} />
            <Route exact path={ROUTES.BROWSE_GAMES} component={LobbyBrowser} />
            <Route exact path={ROUTES.GAME} component={GameRouter} />
            <Route exact path={ROUTES.ADMIN_PAGE} component={AdminPage} />
            <Route component={NotFound} />
            {/* All routes have "exact" since that means that the path has to match the exact string. If a route is not correct, the fallback is ROUTES.NOT_FOUND since it's the only one without "exact" */}
          </Switch>
        </Router>
      </unregisteredUserContext.Provider>
    </userContext.Provider>
  );
}
