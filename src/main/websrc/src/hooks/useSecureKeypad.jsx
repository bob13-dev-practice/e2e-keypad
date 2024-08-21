"use client";

import {useMemo, useState} from 'react';
import axios from "axios";
import {JSEncrypt} from "jsencrypt";

export default function useSecureKeypad() {
    const [keypad, setKeypad] = useState(null);
    const [userInput, setUserInput] = useState([]);
    const [circleColors, setCircleColors] = useState(Array(6).fill('rgb(224, 223, 225)'));

    const getSecureKeypad = async () => {
        const response = await axios.get('/api/keypad');
        const data = await response.data;
        setKeypad(data);
    }

    const onKeyPressed = (keyValue) => {
        setUserInput((prevInput) => {
            const newUserInput = [...prevInput, keyValue];
            const newCircleColors = [...circleColors];

            if (prevInput.length < 6) {
                newCircleColors[prevInput.length] = 'rgb(94, 96, 100)';
                setCircleColors(newCircleColors);
            }

            if (newUserInput.length === 6) {
                sendUserInput(newUserInput);
            }
            return newUserInput;
        });
    };

    const sendUserInput = async (userInput) => {
        const encrypt = new JSEncrypt();
        const publicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtkLA7dcyLqz4M6BS/XZi\n" +
            "wMee85fjwskmxfZVN/qI854Sa4mlU/5Rse0HcNY0QoF+J3kQF3xWpTKLfw2p5pzt\n" +
            "sALLN6gsO2m4qLIOk3eNR+hVL2Rh4dc8MAhuXfoTGrfMjXouiy05rYgVpqIRRCjz\n" +
            "MVGYnJ7arZ6rMN73nRxd0I9RVbe3LXEuHrBysxjfXae6z+qb+1Rp9MKnwiDuKC/i\n" +
            "2lqqqmV9p/8OuY+qUzsMCtU8URS8kvw/bkg90TEOHzjKWrRIYRcQQkdJ8KuX3/lV\n" +
            "1jBBgIQRfmQVTFUnkV5XBZw9jXYTsz6Bcp4MNWUlwHQIebAM8vMZ6/nH9p4OdETA\n" +
            "5wIDAQAB\n" +
            "-----END PUBLIC KEY-----\n"

        encrypt.setPublicKey(publicKey);
        const userInputStr = userInput.join('');
        const encryptedUserInput = encrypt.encrypt(userInputStr);

        const response  = await axios.post('/api/keypad/auth', {
            "id": keypad["id"],
            "timestamp": keypad["timestamp"],
            "hash": keypad["hash"],
            "userInput": encryptedUserInput
        });
        alert(response.data);
        window.location.reload();
    }

    return {
        states: {
            keypad,
            userInput,
            circleColors
        },
        actions: {
            getSecureKeypad,
            onKeyPressed,
            sendUserInput
        }
    }
}