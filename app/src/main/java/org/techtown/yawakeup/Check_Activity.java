package org.techtown.yawakeup;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.List;

//MPAndroidChart import
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.charts.LineChart;

public class Check_Activity extends Activity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        //LineChart lineChart = (LineChart) findViewById(R.id.chart);
        BarChart barChart = (BarChart) findViewById(R.id.chart);

        // 그래프 값
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 1));
        entries.add(new BarEntry(2, 2));
        entries.add(new BarEntry(3, 0));
        entries.add(new BarEntry(4, 4));
        entries.add(new BarEntry(5, 3));

        //그래프 속성
        BarDataSet set = new BarDataSet(entries, "값1");
        set.setBarBorderWidth(0.9f);
        BarData barData = new BarData(set);


/*      LineDataSet lineDataSet = new LineDataSet(entries, "속성명1");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleHoleColor(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);*/

       /* LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.WHITE);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EaseInCubic);
        lineChart.invalidate();*/



    }

}
