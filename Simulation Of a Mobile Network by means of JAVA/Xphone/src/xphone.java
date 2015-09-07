
import java.text.DecimalFormat;
import java.util.TreeMap;

public class xphone {

    public static void main(String[] args) {

        int seed = 98987;
        int replLength = 3600;   //1 hour     86400 for a day    3600 for an hour
        int replication = 100;
        int warmup = 200;   //in seconds
        int channel = 10;  //in each base station
        int reserved = 0;   //for hand over

        if (args.length != 0) {
            seed = Integer.parseInt(args[0]);
            replLength = Integer.parseInt(args[1]);
            replication = Integer.parseInt(args[2]);
            warmup = Integer.parseInt(args[3]);
            channel = Integer.parseInt(args[4]);
            reserved = Integer.parseInt(args[5]);
        }

        int totalCalls = 0;
        int totalDroped = 0;
        int totalBlocked = 0;
        randomNumberGeneratorG22 rng = new randomNumberGeneratorG22(seed);
        DecimalFormat df = new DecimalFormat(".##");

        for (int repl = 1; repl <= replication; repl++) {   //Each replications

            float clock = 0; //time stamp of system
            TreeMap<Float, Event> Fel = new TreeMap<Float, Event>();
            BtsStatus Bts = new BtsStatus(channel, reserved);
            int nCalls = 0;
            int nBlocked = 0;
            int nDroped = 0;

            //create First event
            float rnPos;
            for (rnPos = rng.getRndPosition(); rnPos >= 40.00; rnPos = rng.getRndPosition()) {
            }
            Event e = new Event(clock + rng.getRndInterArrival(), rnPos, rng.getRndDuration(), rng.getRndSpeed(), "arrival", "");
            Fel.put(e.getTime(), e);

            do {
                e = Fel.firstEntry().getValue();
                clock = e.getTime();

                Fel.remove(e.getTime());
                //-----------------------------------Arrival Handler---------------------------------
                if (e.getType().equals("arrival")) {

                    if (clock > warmup) {
                        nCalls++;
                        totalCalls++;
                    }

                    //create next arrival event regarding the replication lenght
                    float nextArrival = Float.valueOf(df.format(clock + rng.getRndInterArrival()));
                    if (nextArrival <= (float) replLength) {
                        for (rnPos = rng.getRndPosition(); rnPos >= 40.00; rnPos = rng.getRndPosition()) {
                        }
                        Event event = new Event(nextArrival, rnPos, rng.getRndDuration(), rng.getRndSpeed(), "arrival", "");
                        for (; Fel.containsKey((event.getTime())); event.setTime(Float.valueOf(df.format(event.getTime() + 0.01))));
                        Fel.put(event.getTime(), event);

                    }


                    int curBts = (int) Math.floor(e.getPosition() / 2);  //current bts
                    if (Bts.getOrdinary(curBts) == "ordinary") {  // try getting an ordinary channel

                        int nextBts = (int) Math.floor((e.getPosition() + (e.getSpeed() * e.getDuration()) / 3600) / 2); //calculate next bts

                        if (nextBts == curBts) {  //create departure event, since no handover needed
                            float nextPosition = Float.valueOf(df.format((e.getPosition() + (e.getSpeed() * e.getDuration()) / 3600)));
                            float endTime = Float.valueOf(df.format(e.getTime() + e.getDuration()));
                            Event event = new Event(endTime, nextPosition, -1, -1, "departure", "ordinary");
                            for (; Fel.containsKey((event.getTime())); event.setTime(Float.valueOf(df.format(event.getTime() + 0.01))));
                            Fel.put(event.getTime(), event);

                        } else {  //this call needs handover
                            double remainedTimetoHO = ((((curBts + 1) * 2) - e.getPosition()) * 3600) / e.getSpeed();  //remained time to next bts
                            float handoverTime = Float.valueOf(df.format(e.getTime() + remainedTimetoHO));
                            float remainedCall = Float.valueOf(df.format(e.getDuration() - remainedTimetoHO));
                            float hoPosition;
                            if (((curBts + 1) * 2) == 40) {
                                hoPosition = 0;
                            } else {
                                hoPosition = (curBts + 1) * 2;
                            }
                            Event event = new Event(handoverTime, hoPosition, remainedCall, e.getSpeed(), "handover", "ordinary");
                            for (; Fel.containsKey((event.getTime())); event.setTime(Float.valueOf(df.format(event.getTime() + 0.01))));
                            Fel.put(event.getTime(), event);
                        }

                    } else {
                        if (clock > warmup) {
                            totalBlocked++;
                            nBlocked++;
                        }
                    }
                }
                //-----------------------------------Handover Handler---------------------------------
                if (e.getType() == "handover") {

                    int curBts = (int) Math.floor(e.getPosition() / 2);  //current bts
                    int index;
                    if (curBts == 0) {
                        index = 19;
                    } else {
                        index = curBts - 1;
                    }
                    if (e.getChannel() == "ordinary") {  // release the previously allocated channel from previous bts
                        Bts.releaseOrdinary(index);
                    } else {
                        Bts.releaseReserved(index);
                    }

                    //if (curBts >20) {System.out.println("error " + e.getPosition());}
                    String alocChannel = Bts.getreserved(curBts); // try getting a reserved or an ordinary channel
                    if (alocChannel != "error") {

                        int nextBts = (int) Math.floor((e.getPosition() + (e.getSpeed() * e.getDuration()) / 3600) / 2); //calculate next bts

                        if (nextBts == curBts) {  //create departure event, since no handover needed
                            float nextPosition = Float.valueOf(df.format((e.getPosition() + (e.getSpeed() * e.getDuration()) / 3600)));
                            float endTime = Float.valueOf(df.format(e.getTime() + e.getDuration()));
                            Event event = new Event(endTime, nextPosition, -1, -1, "departure", alocChannel);
                            for (; Fel.containsKey((event.getTime())); event.setTime(Float.valueOf(df.format(event.getTime() + 0.01))));
                            Fel.put(event.getTime(), event);

                        } else {  //this call needs handover
                            double remainedTimetoHO = ((((curBts + 1) * 2) - e.getPosition()) * 3600) / e.getSpeed();  //remained time to next bts
                            float handoverTime = Float.valueOf(df.format(e.getTime() + remainedTimetoHO));
                            float remainedCall = Float.valueOf(df.format(e.getDuration() - remainedTimetoHO));
                            float hoPosition;
                            if (((curBts + 1) * 2) == 40) {
                                hoPosition = 0;
                            } else {
                                hoPosition = (curBts + 1) * 2;
                            }
                            Event event = new Event(handoverTime, hoPosition, remainedCall, e.getSpeed(), "handover", alocChannel);
                            for (; Fel.containsKey((event.getTime())); event.setTime(Float.valueOf(df.format(event.getTime() + 0.01))));
                            Fel.put(event.getTime(), event);
                        }

                    } else {
                        if (clock > warmup) {
                            totalDroped++;
                            nDroped++;
                        }
                    }
                }
                //-----------------------------------Departure Handler---------------------------------
                if (e.getType().equals("departure")) {

                    int curBts = (int) Math.floor(e.getPosition() / 2);  //current bts
                    //if (curBts>=20) {System.out.println("error" + e.getPosition());}
                    if (curBts == 20) {
                        curBts--;
                    }
                    if (e.getChannel() == "ordinary") {
                        Bts.releaseOrdinary(curBts);
                    } else {
                        Bts.releaseReserved(curBts);
                    }
                }
                //------------------------------------------------------------s--------------------------

            } while (!Fel.isEmpty());
            System.out.println("-----------------------------------------");
            System.out.println("replication number =   " + repl);
            System.out.println("total calls =          " + nCalls);
            System.out.println("dropped calls =        " + nDroped);
            System.out.println("blocked calls =        " + nBlocked);
            System.out.println();
            double drop = nDroped * 100.0 / nCalls;
            double block = nBlocked * 100.0 / nCalls;
            System.out.format("percent dropped calls =  %.2f%n", drop);
            System.out.format("percent blocked calls =  %.2f%n", block);

        }
        System.out.println("----------------FINAL SUMMARY---------------");
        System.out.format("average total calls =        %.2f%n", Float.valueOf(totalCalls / replication));
        System.out.format("average dropped calls =      %.2f%n", Float.valueOf(totalDroped / replication));
        System.out.format("average blocked calls =      %.2f%n", Float.valueOf(totalBlocked / replication));
        System.out.println();
        double drop = totalDroped * 100.0 / totalCalls;
        double block = totalBlocked * 100.0 / totalCalls;
        System.out.format("percent dropped calls =  %.2f%n", drop);
        System.out.format("percent blocked calls =  %.2f%n", block);
        System.out.println("-----------------------------------------");

    }
}
