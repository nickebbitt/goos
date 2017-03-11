package auctionsniper.ui;

import auctionsniper.Column;
import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;
import com.objogate.exception.Defect;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class SnipersTableModel extends AbstractTableModel implements SniperListener {
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
    private List<SniperSnapshot> snapshots = new ArrayList<SniperSnapshot>();

    private final static String[] STATUS_TEXT = {"JOINING", "Bidding", "Winning", "Lost", "Won"};

    public int getRowCount() {
        return snapshots.size();
    }

    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    public void addSniper(SniperSnapshot newSnapshot) {
        snapshots.add(newSnapshot);
        int row = rowMatching(newSnapshot);
        fireTableRowsInserted(row, row);
    }

    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        int row = rowMatching(newSnapshot);
        snapshots.set(row, newSnapshot);
        fireTableRowsUpdated(row, row);
    }

    private int rowMatching(SniperSnapshot snapshot) {
        for (int i = 0; i < snapshots.size(); i++) {
            if (snapshot.isForSameItemAs(snapshots.get(i))) {
                return i;
            }

        }
        throw new Defect("Cannot find match for " + snapshot);
    }

}
