import { useContext, useEffect, useState } from "react";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import tokenParser from "../helpers/tokenParser";
import userContext from "../context/user";
import { Button, Form } from "react-bootstrap";
import * as ROUTES from "../constants/routes";
import errorContext from "../context/error";

export default function EditAchievement() {
    const params = useParams();
    const history = useHistory();
    const { errors, setErrors } = useContext(errorContext);
    const { userToken, setUserToken } = useContext(userContext); // hook
    const loggedUser = tokenParser(useContext(userContext)); // hook

    const [achievement, setAchievement] = useState(null);
    const [name, setName] = useState(null);
    const [description, setDescription] = useState(null);

    const sendHome = () => history.push(ROUTES.HOME);

    async function fetchAchievement() {
        console.log(params.achievementId);
        try{
            const response = await axios.get(`/achievements/${params.achievementId}`);
            setAchievement(response.data);
            setName(response.data.name);
            setDescription(response.data.description);
        } catch (error) {
            setErrors([...errors, error.response]);
            sendHome();
        }
    }
    
    async function handleSubmit(e) {
        e.preventDefault();
        try{
            const payload = {
                name,
                description
            };
            const response = await axios.put("/achievements", payload, {
                headers: { Authorization: "Bearer " + userToken},
            });
            sendHome();
        } catch (error) {
            setErrors([...errors, error.response]);
        }
    }

    useEffect(() => {
        document.title = `NTFH - Edit achievement`;

        if (!loggedUser.authorities.includes("admin")) {
            history.push(ROUTES.HOME);
        } else fetchAchievement();
    }, []);

    return (
        <>
            <h1>Edit achievement</h1>
            <br />
            <Form onSubmit={handleSubmit}>
                <Form.Group controlId="formBasicAchievementName">
                    <Form.Label>Achievement Name</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Achievement Name"
                        name="achievementName"
                        defaultValue={achievement?.name}
                        required
                    />
                </Form.Group>
                <Form.Group controlId="formBasicAchievementDescription">
                    <Form.Label>Achievement Description</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Achievement Description"
                        name="achievementDescription"
                        defaultValue={achievement?.description}
                        required
                    />
                </Form.Group>
                <Button variant="primary" type="submit">
                    Submit
                </Button>
            </Form>
        </>
    );
}