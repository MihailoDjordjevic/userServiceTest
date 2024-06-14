const canvas = document.getElementById('drawingCanvas');
const ctx = canvas.getContext('2d');

// Set canvas width and height to match the container's size
canvas.width = canvas.offsetWidth;
canvas.height = canvas.offsetHeight;

let drawing = false;

function getMousePos(canvas, evt) {
    var rect = canvas.getBoundingClientRect();
    return {
        x: evt.clientX - rect.left,
        y: evt.clientY - rect.top
    };
}

function startDrawing(e) {
    drawing = true;
    draw(e);  // Start drawing immediately
}

function stopDrawing() {
    drawing = false;
    ctx.beginPath();  // Reset the path
}

function draw(e) {
    if (!drawing) return;

    const pos = getMousePos(canvas, e);
    
    ctx.lineWidth = 15;
    ctx.lineCap = 'round';
    ctx.strokeStyle = 'black';
    
    ctx.lineTo(pos.x, pos.y);
    ctx.stroke();
    ctx.beginPath();
    ctx.moveTo(pos.x, pos.y);
}

canvas.addEventListener('mousedown', startDrawing);
canvas.addEventListener('mouseup', stopDrawing);
canvas.addEventListener('mousemove', draw);

const checkButton = document.getElementById('checkButton');
checkButton.addEventListener('click', () => {
    const link = document.createElement('a');
    link.download = 'canvas-drawing.png';
    link.href = canvas.toDataURL('image/png');
    link.click();
});
