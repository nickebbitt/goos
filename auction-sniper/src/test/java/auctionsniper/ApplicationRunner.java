package auctionsniper;

import auctionsniper.ui.MainWindow;

import javax.swing.*;

import static auctionsniper.FakeAuctionServer.XMPP_HOSTNAME;
import static auctionsniper.SniperState.*;
import static auctionsniper.ui.SnipersTableModel.textFor;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer... auctions) {

        startSniper();

        for (FakeAuctionServer auction : auctions) {
            final String itemId = auction.getItemId();
            driver.startBiddingFor(itemId);
            driver.showSniperStatus(auction.getItemId(), 0, 0, textFor(JOINING));
        }
    }

    private void startSniper() {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        thread.setDaemon(true);
        thread.start();
        makeSureAwtIsLoadedBeforeStartingTheDriverOnOSXToStopDeadlock();

        driver = new AuctionSniperDriver(1000);
        driver.hasTitle(MainWindow.APPLICATION_TITLE);
        driver.hasColumnTitles();
    }

    public void showsSniperHasLostAuction(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showSniperStatus(auction.getItemId(), lastPrice, lastBid, textFor(LOST));
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showSniperStatus(auction.getItemId(), lastPrice, lastBid, textFor(BIDDING));
    }

    public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
        driver.showSniperStatus(auction.getItemId(), winningBid, winningBid, textFor(WINNING));
    }

    public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
        driver.showSniperStatus(auction.getItemId(), lastPrice, lastPrice, textFor(WON));
    }

    private void makeSureAwtIsLoadedBeforeStartingTheDriverOnOSXToStopDeadlock() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() { public void run() {} });
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
