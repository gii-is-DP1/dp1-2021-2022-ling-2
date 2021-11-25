import Homebar from "../components/home/Homebar";
import { Table, Col, Row } from "react-bootstrap";
import { useState, useEffect } from "react";
import axios from "axios";
import errorContext from "../context/error";

export default function Statistics() {
    const { errors, setErrors } = useContext(errorContext); // Array of errors
    const [gameHistoryList, setGameHistoryList] = useState([]);
    const [gameHistoryCount, setGameHistoryCount] = useState(null);

    const placeholderGameHistory = [
        {
            id: 1,
            game: {
                id: 2,
                startTime: "2020-04-01T00:00:00Z",
                hasScenes: true,
                players: ["stockie", "andres", "admin"],
                leader: ["stockie"],
            },
            finishTime: "2020-04-01T00:45:16Z",
            winner: {
                username: "admin",
            },
            comments: [],
        },
        {
            id: 2,
            game: {
                id: 3,
                startTime: "2020-04-01T00:00:00Z",
                hasScenes: true,
                players: ["stockie", "andres"],
                leader: ["stockie"],
            },
            finishTime: "2020-04-01T00:45:16Z",
            winner: {
                username: "andres",
            },
            comments: [],
        },
    ];

    useEffect(() => {
        const countGamesHistory = async () => {
            try{
                const response = await axios.get(`gameHistory/count`)
                setGameHistoryCount(response.data);
            } catch {
                setErrors([...errors, error.response]);
            }

        };

        countGamesHistory();
    },[]);

    return (
        <>
            <Homebar />
            <h1>Statistics</h1>
            <Table>
                <Col>
                    <Row>
                        Total Games Played: {gameHistoryCount}
                    </Row>
                </Col>
            </Table>
        </>
    );
}