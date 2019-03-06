package io.github.oblarg.logexample;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

import java.util.Map;

public class LoggedComponent implements Loggable {

    int columnIndex, rowIndex;


    public LoggedComponent(int columnIndex, int rowIndex){
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
    }

    @Log
    private int i = 1;

    private double d;

    @Config.NumberSlider
    private void setD(double d) {
        this.d = d;
        System.out.println("d set to: " + this.d);
    }

    @Override
    public int[] configureLayoutSize() {
        return new int[] {2, 2};
    }

    @Override
    public int[] configureLayoutPosition() {
        return new int[] {columnIndex, rowIndex};
    }

    @Override
    public Map<String, Object> configureLayoutProperties() {
        return Map.ofEntries(
                Map.entry("numberOfColumns", 2),
                Map.entry("numberOfRows", 2)
        );
    }

    @Override
    public LayoutType configureLayoutType() {
        return BuiltInLayouts.kGrid;
    }
}
