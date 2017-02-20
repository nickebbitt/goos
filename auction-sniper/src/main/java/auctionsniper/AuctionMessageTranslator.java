package auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.HashMap;

public class AuctionMessageTranslator implements MessageListener {

    public static final String EVENT_TYPE_CLOSE = "CLOSE";
    public static final String EVENT_TYPE_PRICE = "PRICE";
    private final String sniperId;
    private final AuctionEventListener listener;

    public AuctionMessageTranslator(String sniperId, AuctionEventListener listener) {
        this.sniperId = sniperId;
        this.listener = listener;
    }

    public void processMessage(Chat chat, Message message) {
        AuctionEvent event = AuctionEvent.from(message.getBody());

        String type = event.type();

        if (EVENT_TYPE_CLOSE.equals(type)) {
            listener.auctionClosed();
        } else if (EVENT_TYPE_PRICE.equals(type)) {
            listener.currentPrice(event.currentPrice(),
                                  event.increment(),
                                  event.isFrom(sniperId));
        }

    }

    private static class AuctionEvent {
        private final HashMap<String, String> fields = new HashMap<String, String>();
        public String type() { return get("Event"); }
        public int currentPrice() { return getInt("CurrentPrice"); }
        public int increment() { return  getInt("Increment"); }

        private int getInt(String fieldName) {
            return Integer.parseInt(get(fieldName));
        }

        private String get(String fieldName) {
            return fields.get(fieldName);
        }

        private void addField(String field) {
            String[] pair = field.split(":");
            fields.put(pair[0].trim(), pair[1].trim());
        }

        static AuctionEvent from(String messageBody) {
            AuctionEvent event = new AuctionEvent();
            for (String field : fieldsIn(messageBody)) {
                event.addField(field);
            }
            return event;
        }

        static String[] fieldsIn(String messageBody) {
            return messageBody.split(";");
        }

        public AuctionEventListener.PriceSource isFrom(String sniperId) {
            return sniperId.equals(bidder()) ? AuctionEventListener.PriceSource.FromSniper : AuctionEventListener.PriceSource.FromOtherBidder;
        }

        private String bidder() {
            return get("Bidder");
        }
    }
}
