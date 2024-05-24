import React from 'react';

const Spinner = () => {
    return (
        <div className="spinner" style={spinnerStyle}>
            <div className="dot1" style={dotStyle}></div>
            <div className="dot2" style={dotStyle}></div>
        </div>
    );
};

// CSS 스타일
const spinnerStyle: React.CSSProperties = {
    width: '40px',
    height: '40px',
    position: 'relative',
    margin: '100px auto',
};

const dotStyle: React.CSSProperties = {
    width: '20px',
    height: '20px',
    backgroundColor: '#333',
    borderRadius: '100%',
    position: 'absolute',
    animation: 'bouncedelay 1.2s infinite ease-in-out',
    WebkitAnimation: 'bouncedelay 1.2s infinite ease-in-out',
};

// CSS 애니메이션
const keyframes = `@keyframes bouncedelay {
    0%, 80%, 100% {
        transform: scale(0);
    }
    40% {
        transform: scale(1.0);
    }
}

@-webkit-keyframes bouncedelay {
    0%, 80%, 100% {
        -webkit-transform: scale(0);
    }
    40% {
        -webkit-transform: scale(1.0);
    }
}`;

// CSS 애니메이션 추가
const styleTag = document.createElement('style');
styleTag.type = 'text/css';
styleTag.appendChild(document.createTextNode(keyframes));
document.head.appendChild(styleTag);

export default Spinner;