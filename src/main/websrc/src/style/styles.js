export const cellStyle = {
    width: '50px',
    height: '50px',
    border: '1px solid #ccc',
    display: 'table-cell',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'rgba(209, 232, 250, 0)'
};

export const buttonStype = {
    width: '100%',
    height: '100%',
    border: 'none',
    background: 'transparent',
    cursor: 'pointer'
}

export const tableStyle = (keypadImage) => ({
    justifyContent: 'center',
    borderCollapse: 'collapse',
    marginTop: '20px',
    backgroundColor: 'rgb(184, 220, 244)',
    backgroundImage: keypadImage ? `url(data:image/png;base64,${keypadImage})` : 'none',
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    width: '200px',
    height: '150px',
});

export const circleStyle = {
    width: '30px',
    height: '30px',
    borderRadius: '50%',
    backgroundColor: 'rgb(224, 223, 225)',
    display: 'inline-block',
    margin: '5px',
};

export const containerStyle = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    height: '100vh',
};
