package auctionsniper;

import auctionsniper.ui.SnipersTableModel;

class SwingThreadSniperListener implements SniperListener {
    private final SnipersTableModel snipers;

    public SwingThreadSniperListener(SnipersTableModel snipers) {
        this.snipers = snipers;
    }

    public void sniperStateChanged(final SniperSnapshot state) {
        snipers.sniperStateChanged(state);
    }
}
