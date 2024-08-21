import '../style/keypad.css'
import React from "react";
import {buttonStype, cellStyle, tableStyle} from "../style/styles.js"

export default function SecureKeypad({ keypad, onKeyPressed }) {
    return (
        <>
            <table style={tableStyle(keypad["keypadImage"])}>
                <tbody>
                {[...Array(3)].map((_, rowIndex) => (
                    <tr key={rowIndex}>
                        {[...Array(4)].map((_, colIndex) => {
                            const keyIndex = rowIndex * 4 + colIndex;
                            const keyValue = keypad["keys"][keyIndex];
                            return (
                                <td key={colIndex} style={cellStyle}>
                                    <button
                                        value={keyValue}
                                        style={buttonStype}
                                        onClick={() => onKeyPressed(keyValue)}>
                                    </button>
                                </td>
                            );
                        })}
                    </tr>
                ))}
                </tbody>
            </table>
        </>
    );
}