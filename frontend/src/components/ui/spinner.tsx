import React, { useEffect } from 'react';

const Spinner = () => {
    useEffect(() => {
        const keyframes = `@keyframes rotate {
            0% {
                transform: rotate(0deg);
            }
            100% {
                transform: rotate(360deg);
            }
        }

        .circle1 {
            top: 0;
            left: 50%;
            transform: translate(-50%, -50%);
            animation: circle 1.2s infinite ease-in-out;
        }

        .circle2 {
            top: 50%;
            left: 100%;
            transform: translate(-50%, -50%);
            animation: circle 1.2s infinite ease-in-out;
            animation-delay: -0.4s;
        }

        .circle3 {
            top: 100%;
            left: 50%;
            transform: translate(-50%, -50%);
            animation: circle 1.2s infinite ease-in-out;
            animation-delay: -0.8s;
        }

        .circle4 {
            top: 50%;
            left: 0;
            transform: translate(-50%, -50%);
            animation: circle 1.2s infinite ease-in-out;
            animation-delay: -1.2s;
        }

        @keyframes circle {
            0%, 80%, 100% {
                transform: scale(0);
            }
            40% {
                transform: scale(1);
            }
        }`;

        const styleTag = document.createElement('style');
        styleTag.type = 'text/css';
        styleTag.appendChild(document.createTextNode(keyframes));
        document.head.appendChild(styleTag);

        // Clean up function to remove the style tag when the component unmounts
        return () => {
            document.head.removeChild(styleTag);
        };
    }, []); // Empty dependency array ensures this effect runs only once when the component mounts

    return (
        <div className="spinner-container" style={spinnerContainerStyle}>
            <div className="spinner" style={spinnerStyle}>
                <div className="circle1" style={circleStyle}></div>
                <div className="circle2" style={circleStyle}></div>
                <div className="circle3" style={circleStyle}></div>
                <div className="circle4" style={circleStyle}></div>
            </div>
        </div>
    );
};

// CSS 스타일
const spinnerContainerStyle: React.CSSProperties = {
    position: 'fixed',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 9999, // 다른 요소들 위에 나타나도록 설정
};

const spinnerStyle: React.CSSProperties = {
    width: '40px',
    height: '40px',
    position: 'relative',
    animation: 'rotate 1.2s linear infinite',
};

const circleStyle: React.CSSProperties = {
    width: '10px',
    height: '10px',
    backgroundColor: '#333',
    borderRadius: '50%',
    position: 'absolute',
};

export default Spinner;
