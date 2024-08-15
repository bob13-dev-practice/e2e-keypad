"use client";

import {useMemo, useState} from 'react';
import axios from "axios";
import {JSEncrypt} from "jsencrypt";

export default function useKeypad() {
  const [count, setCount] = useState(0);
  const [keypadImage, setKeypadImage] = useState('');
  const [keys, setKeys] = useState([]);

  const increase = () => {
    setCount(prevState => {
      console.log(`prevState=${prevState}`);
      return prevState + 1
    })
  }

  const axiosKeypad = () => {
    axios.get('/api/keypad')
      .then(response => {
        console.log(response.data);
        setKeypadImage(response.data.keypadImage)
        setKeys(response.data.keys)
      })
      .catch(error => {
        alert(error);
      })
  }

  return {
    states: {
      keypadImage,
      keys
    },
    actions: {
      axiosKeypad
    }
  }
}
