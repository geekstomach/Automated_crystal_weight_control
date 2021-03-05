package model;

public class WeightUnit {

    private long timeStamp1 ;
    private long timeStamp2;
    private long weight;

    public WeightUnit(long tS1, long tS2, long w) {
        timeStamp1 = tS1;
        timeStamp2 = tS2;
        weight = w;
    }

    public long getTimeStamp1() {
        return timeStamp1;
    }

    public void setTimeStamp1(long timeStamp1) {
        this.timeStamp1 = timeStamp1;
    }
    public long getTimeStamp2() {
        return timeStamp2;
    }

    public void setTimeStamp2(long timeStamp2) {
        this.timeStamp2 = timeStamp2;
    }


    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }
}
