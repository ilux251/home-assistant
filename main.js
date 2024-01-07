const { app, BrowserWindow } = require('electron')

const createWindow = () => {
  const win = new BrowserWindow({
    width: 800,
    height: 600,
    fullscreen: true,
    alwaysOnTop: true,
  })

  win.loadURL("http://v2202401214473251973.ultrasrv.de")
}

app.whenReady().then(() => {
  createWindow()
})