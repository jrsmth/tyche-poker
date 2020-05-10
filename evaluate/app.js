'use strict'

const express = require('express');
const bodyParser = require('body-parser');
var evaluation = require('./evaluation');


const app = express();
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(bodyParser.raw());


// Route that receives a POST request to /winners
app.post('/winners', function (req, res) {
  const body = req.body;
  console.log("req body: " + req.body.userHands);
  const winners = evaluation.evaluate(body.userHands);
  res.set('Content-Type', 'application/json');
  res.json(winners);
})


// Tell our app to listen on port 6969
app.listen(6969, function (err) {
  if (err) {
    throw err;
  }
  console.log('Server started on port 3000');
})


// run using: node express-post.js