package auctionsniper;

import auctionsniper.ui.SnipersTableModel;

import static auctionsniper.FakeAuctionServer.XMPP_HOSTNAME;
import static auctionsniper.SniperState.*;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";

    private String itemId;

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction) {
        itemId = auction.getItemId();

        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
    }

    public void showsSniperHasLostAuction(int lastPrice, int lastBid) {
        driver.showSniperStatus(itemId, lastPrice, lastBid, SnipersTableModel.textFor(LOST));
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
        driver.showSniperStatus(itemId, lastPrice, lastBid, SnipersTableModel.textFor(BIDDING));
    }

    public void hasShownSniperIsWinning(int winningBid) {
        driver.showSniperStatus(itemId, winningBid, winningBid, SnipersTableModel.textFor(WINNING));
    }

    public void showsSniperHasWonAuction(int lastPrice) {
        driver.showSniperStatus(itemId, lastPrice, lastPrice, SnipersTableModel.textFor(WON));
    }
}
