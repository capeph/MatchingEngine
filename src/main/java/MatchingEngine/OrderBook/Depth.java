package MatchingEngine.OrderBook;


import MatchingEngine.Logger;
import MatchingEngine.Manager.ManagerMailBox;
import MatchingEngine.Messaging.MailBox;
import MatchingEngine.Trading.Order;
import MatchingEngine.Trading.Side;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Depth {

    private Logger logger = new Logger("Depth");
    private final SortedMap<Long, PriceLevel> depthMap;
    private OrderMessage template = new OrderMessage();
    private final Side side;

    Depth(Side side) {
        this.side = side;
        depthMap = new TreeMap<>(side.getComparator());
    }

    private PriceLevel getPriceLevel(long price) {
        PriceLevel level = depthMap.get(price);
        if (level == null) {
            //TODO - lazy init not good from performance point of view
            level = new PriceLevel();
            depthMap.put(price, level);
        }
        return level;
    }

    void add(Order order, ManagerMailBox toManager) {
        PriceLevel level = getPriceLevel(order.getPrice());
        level.add(order, toManager);
        logger.info("stored order " + order.getId() + " at level " + order.getPrice());
    }

    void match(Order order, ManagerMailBox toManager) {
        Iterator<Map.Entry<Long, PriceLevel>> it = depthMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, PriceLevel> entry = it.next();
            long price = entry.getKey();
            PriceLevel level = entry.getValue();
            if (!order.acceptsPrice(price)) {
                break;
            }
            long volume = level.match(order, toManager);
            toManager.sendLevelUpdate(side, price, volume);
            if (volume == 0) {
                it.remove();
            }
        }
    }


}
