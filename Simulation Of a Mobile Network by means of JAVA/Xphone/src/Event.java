/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sina
 */
public class Event {

    private float time = -1;
    private float position = -1;
    private float duration = -1;
    private float speed = -1;
    private String type = "";       //arrival or departure or handover
    private String channel ="";  //reserved or ordinary to release , not used for arrival events
    
    public Event(float time, float position, float duration, float speed, String type, String channel) {
        this.time = time;
        this.position = position;
        this.duration = duration;
        this.speed = speed;
        this.type = type;
        this.channel = channel;
    }

    public float getDuration() {
        return duration;
    }

    public float getPosition() {
        return position;
    }

    public float getSpeed() {
        return speed;
    }

    public float getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getChannel() {
        return channel;
    }

    public void setTime(float time) {
        this.time = time;
    }
    
}
