package event;

/**
 * Created by zhongyu on 1/19/2018.
 */

public class StringEvent implements Event {
    String strName;

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public StringEvent(String strName) {
        this.strName = strName;

    }
}
