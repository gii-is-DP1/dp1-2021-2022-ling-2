import Homebar from "../components/home/Homebar";
import { Table, Col, Row } from "react-bootstrap";
import { useState, useEffect } from "react";

export default function Statistics() {
    const [gameHistoryList, setGameHistoryList] = useState([]);
    const [gameHistoryCount, setGameHistoryCount] = useState("");

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
                username: "stockie",
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
                username: "stockie",
            },
            comments: [],
        },
    ];

    useEffect(() => {

    })

    return (
        <>
            <Homebar />
            <h1>Statistics</h1>
            <Table>
                <Col>
                    <Row>
                        Total Games Played: { }
                    </Row>
                </Col>
            </Table>
        </>
    );
}