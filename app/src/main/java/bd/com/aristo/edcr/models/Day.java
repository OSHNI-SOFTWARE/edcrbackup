package bd.com.aristo.edcr.models;

import java.io.Serializable;

/**
 * Created by monir.sobuj on 10/15/2018.
 */

public class Day implements Serializable{
    int day;
    int month;
    int year;
    int cell;
    int lastDay;
    int copyDate;
    public Day(){

    }

    public Day(int day, int month, int year, int cell, int lastDay, int copyDate) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.cell = cell;
        this.lastDay = lastDay;
        this.copyDate = copyDate;
    }

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

    public int getLastDay() {
        return lastDay;
    }

    public void setLastDay(int lastDay) {
        this.lastDay = lastDay;
    }

    public int getCopyDate() {
        return copyDate;
    }

    public void setCopyDate(int copyDate) {
        this.copyDate = copyDate;
    }

}
