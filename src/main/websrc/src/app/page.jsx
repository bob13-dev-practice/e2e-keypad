"use client";

import React, {useEffect} from 'react';
import useSecureKeypad from "../hooks/useSecureKeypad.jsx"
import KeypadUserInput from "../components/KeypadUserInput.jsx"
import SecureKeypad from "../components/SecureKeypad";
import {containerStyle} from "@/style/styles.js";

export default function Page() {
  const { states, actions } = useSecureKeypad();

  useEffect(() => {
      actions.getSecureKeypad()
  }, []);

  if (states.keypad === null) {
    return (
        <div>
          ...isLoading...
        </div>
    )
  } else {
    return (
        <div style={containerStyle}>
            <KeypadUserInput circleColors={states.circleColors}/>
            <SecureKeypad keypad={states.keypad} onKeyPressed={actions.onKeyPressed}/>
        </div>
    );
  }
}