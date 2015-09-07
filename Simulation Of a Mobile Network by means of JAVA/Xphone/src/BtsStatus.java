/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sina
 */
public class BtsStatus {

    private int NresChannel;
    private int NordChannel;
    private int[] reserved;
    private int[] ordinary;

    public BtsStatus(int totalChannels, int resChannel) {
        reserved = new int[20];
        ordinary = new int[20];
        for (int i = 0; i < 20; i++) {
            reserved[i] = 0;
            ordinary[i] = 0;
        }
        this.NresChannel = resChannel;
        this.NordChannel = totalChannels - resChannel;
    }

    String getOrdinary(int index) {
        //if(index==20){index--;}
        if (ordinary[index] < NordChannel) {
            ordinary[index]++;
            return "ordinary";
        } else {
            return "error";
        }
    }

    void releaseOrdinary(int index) {
        //if(index==20){index--;}
        ordinary[index]--;
    }

    void releaseReserved(int index) {
        //if(index==20){index--;}
        reserved[index]--;
    }

    String getreserved(int index) {
        //if(index==20){index--;}
        if (reserved[index] < NresChannel) {
            reserved[index]++;
            return "reserved";
        }
        if (ordinary[index] < NordChannel) {
            ordinary[index]++;
            return "ordinary";
        }
        return "error";
    }
}


