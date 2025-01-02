const fs = require('fs');
const puppeteer = require('puppeteer');

async function givePage() {
    const browser = await puppeteer.launch({headless: false});
    const page = await browser.newPage();
    return page;
}

async function navigateToUrl(page, url) {
    await page.goto(url);
    console.log('Opened url');
    // Possibly wait, close any pop-ups
}

async function clickPlayer(page, playerName) {
    await page.waitForTimeout(1000);
    await page.click(`[aria-label='${playerName} ']`);
    console.log('Clicked player: ' + playerName);
}

async function makeBet(page) {
    await page.waitForTimeout(1000);
    await page.type("input[id='betslip-wager-box__input-0']", "1");
    console.log('Input $1 bet');

    await page.waitForTimeout(500);
    await page.click("[class='dk-place-bet-button__wrapper']");
    console.log('Clicked bet button');
}

async function clearBetSlip(page) {
    try {
        await page.waitForSelector('.betslip__clear-all', {timeout: 2000});
        await page.click('.betslip__clear-all');
        console.log('Cleared bet slip.');
    } catch (e) {
        console.log('No clear button found, skipping.');
    }
}

async function login(page) {
    await page.waitForTimeout(2000);
    await page.type("input[id='sportsbook-login-email-input']", "[INPUT EMAIL HERE]");  // Input email
    console.log('Entered email');

    await page.waitForTimeout(300);
    await page.type("input[id='sportsbook-login-password-input']", "[INPUT PASSWORD HERE]");  // Input password
    console.log('Entered password');

    await page.waitForTimeout(300);
    await page.click("button[id='sportsbook-login-button']");
    console.log('Logged in');
}

async function placeAllParlays(page, parlaysArray) {
    for (let i = 0; i < parlaysArray.length; i++) {
        const singleParlay = parlaysArray[i];
        const parlayPlayers = Object.values(singleParlay);

        console.log(`\n=== Parlay #${i+1} ===`);
        console.log(parlayPlayers);

        for (const playerName of parlayPlayers) {
            await clickPlayer(page, playerName);
        }

        await makeBet(page);

        await clearBetSlip(page);
    }
}

async function runProgram() {
    // 1) Launch + new page
    const page = await givePage();
    console.log('Gave page');

    // 2) Actually wait for the promise
    const browserPage = await page;

    // 3) Navigate + login
    await navigateToUrl(browserPage, 'https://sportsbook.draftkings.com/leagues/basketball/88670846?category=player-props&subcategory=first-fg');
    console.log('Navigated to url');
    await login(browserPage);

    // 4) Read parlays from jsonOutput.json
    const parlaysData = JSON.parse(fs.readFileSync('jsonOutput.json', 'utf-8'));

    // 5) Place all parlays
    await placeAllParlays(browserPage, parlaysData.allGames);

    console.log('All parlays attempted.');
}

runProgram();
