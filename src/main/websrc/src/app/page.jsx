"use client";

import React, {useEffect, useState} from 'react';
import useKeypad from "@/hooks/useKeypad.jsx";

export default function Page() {
  const { states, actions } = useKeypad();
  const [clickedKeys, setClickedKeys] = useState([]);
  const [circleColors, setCircleColors] = useState(Array(6).fill('rgb(224, 223, 225)'));

  useEffect(() => {
    actions.axiosKeypad()
    console.log("페이지 진입하면서 최초에 딱 1번만 실행되어야 하는 코드");
  }, []);

  const cellStyle = {
    width: '50px',
    height: '50px',
    border: '1px solid #ccc', // 셀 경계선
    display: 'table-cell',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'rgba(255, 255, 255, 0)' // 배경이 이미지일 경우 가독성을 위해 반투명 배경 추가
  };

  const tableStyle = {
    justifyContent: 'center',
    borderCollapse: 'collapse',
    marginTop: '20px',
    backgroundColor: 'rgb(186, 220, 241)',
    backgroundImage: states.keypadImage ? `url(data:image/png;base64,${states.keypadImage})` : 'none',
    backgroundSize: 'cover', // 배경 이미지 크기 조정
    backgroundPosition: 'center', // 배경 이미지 위치 조정
    width: '200px', // 테이블의 너비 설정
    height: '150px' // 테이블의 높이 설정
  };

  const circleStyle = {
    width: '30px',
    height: '30px',
    borderRadius: '50%',
    backgroundColor: 'rgb(224, 223, 225)', // 원의 색상
    display: 'inline-block',
    margin: '5px' // 원 간의 간격
  };

  const handleButtonClick = (keyValue) => {
    console.log("Button clicked:", keyValue); // 클릭된 버튼의 값 출력
    setClickedKeys((prevKeys) => {
      const newKeys = [...prevKeys, keyValue];
      const nextCircleColors = [...circleColors];
      const indexToChange = newKeys.length - 1; // 현재 클릭된 키의 수를 사용하여 색상 변경

      if (indexToChange < 6) { // 최대 6개 원까지만 색상 변경
        nextCircleColors[indexToChange] = 'rgb(94, 96, 100)';
        setCircleColors(nextCircleColors);
      }

      return newKeys; // 업데이트된 키 배열 반환
    });
  };

  useEffect(() => {
    if (clickedKeys.length === 6) {
      alert(`${clickedKeys.join(', ')}`);
    }
  }, [clickedKeys]); // clickedKeys가 변경될 때마다 실행

  return (
      <div style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        height: '100vh'
      }}>
        <div>
          {circleColors.map((color, index) => (
              <div key={index} style={{
                width: '30px',
                height: '30px',
                borderRadius: '50%',
                backgroundColor: color,
                display: 'inline-block',
                margin: '5px'
              }} ></div>
          ))}
        </div>
        <table style={tableStyle}>
          <tbody>
          {[...Array(3)].map((_, rowIndex) => (
              <tr key={rowIndex}>
                {[...Array(4)].map((_, colIndex) => {
                  const keyIndex = rowIndex * 4 + colIndex;
                  const keyValue = states.keys[keyIndex];

                  return (
                      <td key={colIndex} style={cellStyle}>
                        <button
                            value={keyValue}
                            style={{
                              width: '100%',
                              height: '100%',
                              border: 'none',
                              background: 'transparent',
                              cursor: 'pointer'
                            }}
                            onClick={() => handleButtonClick(keyValue)}
                        >
                        </button>
                      </td>
                  );
                })}
              </tr>
          ))}
          </tbody>
        </table>
      </div>
  )
}
