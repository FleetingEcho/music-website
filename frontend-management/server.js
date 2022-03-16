const express = require('express')
const history = require('connect-history-api-fallback')
const path = require('path')
const app = express()

app.use(express.static('dist'))
app.use(
	history({
		index: '/',
	})
)
app.get('/', (req, res) => {
	res.sendFile(path.join(__dirname, 'dist', 'index.html'))
})

app.listen(8081, () => {
	console.log('Music App is running at port localhost:8081')
})
