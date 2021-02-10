package InfrastructureManager.Modules.HistoryDiagnostic;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * Time object for easier usage in history (with function like get date, get hour, get second etc,
 * extract from a string if possible maybe?)
 *
 * @author Zero
 */
public class TimeData {
 //=============================================Variable initiation===========================================
    int second;
    int minute;
    int hour;

    int date;
    int month;
    int year;
//=============================================setters and getters===========================================


    public int getSecond() {
        return second;
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }

    public int getDate() {
        return date;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    private void setSecond(int second) {
        this.second = second;
    }

    private void setMinute(int minute) {
        this.minute = minute;
    }

    private void setHour(int hour) {
        this.hour = hour;
    }

    private void setDate(int date) {
        this.date = date;
    }

    private void setMonth(int month) {
        this.month = month;
    }

    private void setYear(int year) {
        this.year = year;
    }

//============================================================================================================

    /**
     * Turn a set of number into TimeData object and return it
     * @return TimeData object of input time;
     */
    private TimeData setTimeData(int thisHour, int thisMinute, int thisSecond, int thisDate, int thisMonth, int thisYear) {
        TimeData result = new TimeData();
        result.setDate(thisDate);
        result.setMonth(thisMonth);
        result.setYear(thisYear);

        result.setHour(thisHour);
        result.setMinute(thisMinute);
        result.setSecond(thisSecond);

        return result;
    }

    /**
     * Get current time and return it in TimeData format. Need more throughout thinking
     * This maybe a reinventing the wheel but fck it.
     */
    public TimeData getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();

        int thisHour = now.getHour();
        int thisMinute = now.getMinute();
        int thisSecond = now.getSecond();

        int thisDate = now.getDayOfMonth();
        int thisMonth = now.getMonthValue();
        int thisYear = now.getYear();

        return setTimeData(thisHour, thisMinute, thisSecond, thisDate, thisMonth, thisYear);
    }




    @Override
    public String toString() {
        return String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second) +
                " " + String.format("%02d", date) + "." + String.format("%02d", month) + "." + String.format("%04d", year);
    }


}
