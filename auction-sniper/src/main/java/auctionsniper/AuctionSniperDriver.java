package auctionsniper;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import auctionsniper.ui.MainWindow;

import static auctionsniper.ui.MainWindow.SNIPER_STATUS_NAME;
import static org.hamcrest.Matchers.equalTo;

public class AuctionSniperDriver extends JFrameDriver {
    public AuctionSniperDriver(int timeoutMillis) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame(
                        named(MainWindow.MAIN_WINDOW_NAME),
                        showingOnScreen()),
                        new AWTEventQueueProber(timeoutMillis, 100));
    }

    public void showSniperStatus(String statusText) {
        new JLabelDriver(
                this, named(SNIPER_STATUS_NAME)).hasText(equalTo(statusText));
    }
}
