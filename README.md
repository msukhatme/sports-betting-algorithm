# Betting Parlay Bot

This repository contains a Java program for calculating first-basket parlay odds and a Node.js (Puppeteer) script for automating bets on DraftKings.

## Overview

- **Java Parlay Calculator** (`JavaAlgorithm` folder):
  - `BettingParlayMain.java`
  - `Player.java`
  - Reads input files (`input1.txt`, `input2.txt`, etc.) and outputs possible parlay combinations and probabilities.
  - Exports the data to `bettingOutput.txt` and `jsonOutput.json`.

- **Node/Puppeteer Automation** (`bettingBot2.js`):
  - Opens DraftKings in a Chromium browser.
  - Attempts to place bets by clicking on player lines.
  - May require updating selectors as DraftKings changes their site structure.

## Important Notes

- **Disclaimer**: This script and program are likely to break or become outdated because DraftKings frequently changes their HTML elements and class names.
- **No Guarantee**: Do not use this program to make money. This project is purely educational.
- **Contact**: If you want to use any part of this code, email me at [msukhatme@uchicago.edu](mailto:msukhatme@uchicago.edu).
