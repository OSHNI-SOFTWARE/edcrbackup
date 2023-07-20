package bd.com.aristo.edcr.models.db;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by monir.sobuj on 2/5/2018.
 */

public class DateModel extends RealmObject implements Serializable{
    @PrimaryKey
    long id;
    int day;
    int month;
    int year;
    int cell;

    public int getLastDay() {
        return lastDay;
    }

    public void setLastDay(int lastDay) {
        this.lastDay = lastDay;
    }

    int lastDay;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DateModel(int day, int month, int year, int cell, int lastDay) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.cell = cell;
        this.lastDay = lastDay;
    }

    public DateModel(long id, int day, int month, int year, int cell, int lastDay) {
        this.id = id;
        this.day = day;
        this.month = month;
        this.year = year;
        this.cell = cell;
        this.lastDay = lastDay;
    }

    public DateModel() {
    }

    @Override
    public String toString() {
        return "DateModel{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", cell=" + cell +
                ", lastDay=" + lastDay +
                '}';
    }
}
