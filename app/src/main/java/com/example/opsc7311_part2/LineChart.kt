package com.example.opsc7311_part2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.View

class LineChart(context: Context) : View(context) {
    private val dataPoints = mutableListOf<Float>()
    private var maxHours: Int = 0
    var xAxisLabels: Int = 0

    fun setDataPoints(points: List<Float>) {
        dataPoints.clear()
        dataPoints.addAll(points)
        invalidate() // Refresh the chart when data points are updated
    }

    fun setMaxHours(maxHours: Int) {
        this.maxHours = maxHours
        invalidate() // Refresh the chart when maxHours is updated
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //var catIDs: List<String> = emptyList()
        maxHours = ToolBox.AccountManager.getSettingsObject().maxHours

        xAxisLabels = ToolBox.CategoryManager.getCategoryList().size

        val width = width.toFloat()
        val height = height.toFloat()
        val padding = 50f

        val chartWidth = width - padding * 2
        val chartHeight = height - padding * 2

        val minDataPoint = 0f

        //val scaleX = chartWidth / (dataPoints.size - 1)
        val scaleX = chartWidth / (dataPoints.size)
        val scaleY = chartHeight / maxHours

        val linePaint = Paint().apply {
            color = Color.BLUE
            strokeWidth = 2f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        val markPaint = Paint().apply {
            color = Color.RED
            strokeWidth = 10f
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 30f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        val path = Path()

        // Draw x-axis line
        canvas.drawLine(padding, height - padding, width - padding, height - padding, linePaint)

        // Draw y-axis line
        canvas.drawLine(padding, height - padding, padding, padding, linePaint)

        // Draw x-axis values
        for (i in dataPoints.indices) {
            val x = padding + i * scaleX
            val y = height - padding

            canvas.drawText(i.toString(), x, y + 40, textPaint)
        }


        // Draw y-axis values
        for (i in 0..maxHours) {
            val x = padding
            val y = height - padding - i * scaleY

            canvas.drawText(i.toString(), x - 40, y + textPaint.textSize / 2, textPaint)
        }

        // Draw line chart
        for ((index, dataPoint) in dataPoints.withIndex()) {
            val x = padding + index * scaleX
            val y = height - padding - dataPoint * scaleY

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }

            // Draw mark at each data point
            canvas.drawCircle(x, y, 10f, markPaint)
        }

        canvas.drawPath(path, linePaint)
    }
}