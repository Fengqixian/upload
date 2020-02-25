/**
 * 创建抛物线小球
 * @param ballEl 小球对象
 * @param startLoc 起始位置 相对于整个页面来说的
 * @param endLoc 终点位置 相对于整个页面来说
 * @return ball, ballWrapper, ballStyle, ballWrapperStyle
 */
const createBall = ({ballCss, startLoc, id}) => {
// 创建小球样式
    let ballStyle = document.createElement('style');
    let ballWrapperStyle = document.createElement('style');
    ballCss = `
            #cbBallStyle_${id} {
                ${ballCss}
                position: absolute;
                top: 0;
                left: 0;
            }`;
    {
        ballWrapperStyle.type = 'text/css';
        const ballWrapperCss = `
                #cbBallWrapperStyle_${id} {
                    position: fixed;
                    top: ${startLoc[1]}px;
                    left: ${startLoc[0]}px;
                    z-index: 9999;
                }`;
        ballWrapperStyle.innerHTML = ballWrapperCss;
        // 将style样式存放到head标签
        document.getElementsByTagName('head')[0].appendChild(ballWrapperStyle);

        ballStyle.type = 'text/css';
        ballStyle.innerHTML = ballCss;
        // 将style样式存放到head标签
        document.getElementsByTagName('head')[0].appendChild(ballStyle);
    }

    let ballWrapper = document.createElement('div');
    ballWrapper.setAttribute('id', 'cbBallWrapperStyle_' + id);
    document.body.appendChild(ballWrapper);

    let ball = document.createElement('div');
    ball.setAttribute('id', 'cbBallStyle_' + id);
    ballWrapper.appendChild(ball);
    return {ball, ballWrapper, ballStyle, ballWrapperStyle}
};

/**
 * 清除小球和style
 * @param ball
 * @param ballWrapper
 * @param ballStyle
 * @param ballWrapperStyle
 */
const clearBall = ({ballWrapper, ballStyle, ballWrapperStyle}) => {
    ballWrapper.parentNode.removeChild(ballWrapper);
    ballStyle.parentNode.removeChild(ballStyle);
    ballWrapperStyle.parentNode.removeChild(ballWrapperStyle);
};

const createKeyFrames = ({ball, ballWrapper, startLoc, endLoc, duration, id}) => {
    let x1 = startLoc[0];
    let y1 = startLoc[1];
    let x2 = endLoc[0];
    let y2 = endLoc[1];
    let keyFramesCss = '';
    let cubicBezier = '';
    // 向上抛
    if (y2 !== y1) {
        keyFramesCss = `
            @keyframes hor-animation_${id} {
                0% {
                    left: 0px;
                }
                100% {
                    left: ${x2 - x1}px;
                }
            }
            @keyframes ver-animation_${id} {
                0% {
                    top: ${y1}px;
                }
                100% {
                    top: ${y2}px;
                }
            }`;
        if (y2 < y1) {
            cubicBezier = 'cubic-bezier(.13,.6,.41,.8)';
        } else {
            cubicBezier = 'cubic-bezier(.72,.1,.92,.43)';
        }
    } else if (y2 === y1) {
        keyFramesCss = `
            @keyframes hor-animation_${id} {
                0% {
                    left: 0px;
                }
                100% {
                    left: ${x2 - x1}px;
                }
            }
            @keyframes ver-animation_${id} {
                0% {
                    top: ${y1}px;
                }
                45% {
                    top: ${y1 - 50}px;
                }
                55% {
                    top: ${y1 - 50}px;
                }
                100% {
                    top: ${y2}px;
                }
            }`;
        cubicBezier = 'cubic-bezier(.49,.48,.49,.48)';
    }

    let keyFramesStyle = document.createElement('style');
    keyFramesStyle.type = 'text/css';
    keyFramesStyle.innerHTML = keyFramesCss;
    document.getElementsByTagName('head')[0].appendChild(keyFramesStyle);
    ballWrapper.setAttribute('style', `
    animation: ver-animation_${id} ${duration}s infinite;
    animation-timing-function: ${cubicBezier};`);
    ball.setAttribute('style',
        `animation: hor-animation_${id} ${duration}s linear infinite;`);
};
/**
 * 抛物线小球运动函数
 * @param ballEl 小球对象
 * @param startLoc 起始位置 相对于整个页面来说的
 * @param endLoc 终点位置 相对于整个页面来说
 * @param duration 运行时间
 */
const parabolaBall = ({ballCss, startLoc, endLoc, duration}) => {
    if (!duration) duration = 0.5;
    if (!ballCss) ballCss = `width: 20px;
                          height: 20px;
                          border-radius: 50%;
                          background-color: #00aa00;`;
    let id = parseInt(Math.random() * 10000) + new Date().getTime();
    // 创建小球
    const {ball, ballWrapper, ballStyle, ballWrapperStyle} = createBall({ballCss, startLoc, id});
    createKeyFrames({ball, ballWrapper, startLoc, endLoc, duration, id});


    setTimeout(() => {
        clearBall({ballWrapper, ballStyle, ballWrapperStyle});
    }, duration * 1000)
};

export default parabolaBall;
