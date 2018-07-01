package com.gcd.cpp;


import java.util.*;
import java.util.List;
import java.awt.Point;

public class Solution {
    public static void main(String args[]) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */
        Scanner scan = new Scanner(System.in);

        int sizeOfWorld = Integer.parseInt(scan.nextLine());
        int numberOfEvents = Integer.parseInt(scan.nextLine());
        List<Event> eventsList = new ArrayList<>();
        List<Buyer> buyersList = new ArrayList<>();
        while (numberOfEvents > 0) {
            String event = scan.nextLine();
            String[] eventInfo = event.split(" ");
            int eventId = Integer.parseInt(eventInfo[0]);
            Point location = new Point(Integer.parseInt(eventInfo[1]), Integer.parseInt(eventInfo[2]));
            TreeSet<Integer> prices = new TreeSet<>();
            for (int i = 3; i < eventInfo.length; i++) {
                prices.add(Integer.parseInt(eventInfo[i]));
            }
            eventsList.add(new Event(eventId, location, prices));
            numberOfEvents--;
        }

        int numberOfBuyers = Integer.parseInt(scan.nextLine());
        int i = 0;
        while (numberOfBuyers > 0) {
            String buyer = scan.nextLine();
            String[] buyerInfo = buyer.split(" ");
            Point buyerLocation = new Point(Integer.parseInt(buyerInfo[0]), Integer.parseInt(buyerInfo[1]));
            buyersList.add(new Buyer(i, buyerLocation));
            i++;
            numberOfBuyers--;
        }

        // The solution to the first sample above would be to output the following to console:
        // (Obviously, your solution will need to figure out the output and not just hard code it)

        buyersList.forEach((Buyer buyer) -> {
            Point currentPoint = buyer.location;
            eventsList.sort(getComparator(currentPoint));
            Event selectedEvent = null;
            for (int index = 0; index < eventsList.size(); index++) {
                if (eventsList.get(index).price.size() > 0) {
                    selectedEvent = eventsList.get(index);
                    break;
                }
            }
            System.out.println(selectedEvent.eventId + " " + selectedEvent.price.first());
            TreeSet<Integer> currentPrices = selectedEvent.price;
            currentPrices.remove(currentPrices.first());
            selectedEvent.price = currentPrices;
            eventsList.remove(selectedEvent);
            eventsList.add(selectedEvent);
        });

    }

    private static Comparator<? super Event> getComparator(Point currentPoint) {
        Comparator<Event> comparator = new Comparator<Event>() {
            @Override
            public int compare(Event event1, Event event2) {
                if (getManhattanDistance(currentPoint, event1) < getManhattanDistance(currentPoint, event2))
                    return -1;
                else if (getManhattanDistance(currentPoint, event1) < getManhattanDistance(currentPoint, event2))
                    return +1;
                TreeSet<Integer> event1PriceSortedSet = event1.price;
                TreeSet<Integer> event2PriceSortedSet = event2.price;
                if (event1PriceSortedSet.size() > 0 && event2PriceSortedSet.size() > 0) {
                    if (event1PriceSortedSet.first() < event2PriceSortedSet.first()) {
                        return -1;
                    } else if (event1PriceSortedSet.first() > event2PriceSortedSet.first()) {
                        return +1;
                    } else {
                        return Integer.compare(event1.eventId, event2.eventId);
                    }
                }
                return 0;
            }

            private int getManhattanDistance(Point currentPoint, Event event) {
                return Math.abs(currentPoint.x - event.location.x) + Math.abs(currentPoint.y - event.location.y);
            }
        };
        return comparator;
    }
}

class Event {
    int eventId;
    Point location;
    TreeSet<Integer> price;

    public Event(int eventId, Point location, TreeSet<Integer> price) {
        this.eventId = eventId;
        this.location = location;
        this.price = price;
    }
}

class Buyer {
    int buyerId;
    Point location;

    public Buyer(int buyerId, Point location) {
        this.buyerId = buyerId;
        this.location = location;
    }
}



