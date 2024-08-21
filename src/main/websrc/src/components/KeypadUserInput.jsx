import '../style/keypad.css'
import {circleStyle} from "@/style/styles.js";
import React from "react";

export default function KeypadUserInput({ circleColors }) {
    return (
        <>
            <div>
                {circleColors.map((color, index) => (
                    <div key={index} style={{
                        ...circleStyle,
                        backgroundColor: color,
                    }}></div>
                ))}
            </div>
        </>
    );
}