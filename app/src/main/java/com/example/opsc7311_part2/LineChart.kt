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

        val headingText = "Line Graph - Hours vs Categories"
        val headingPaint = Paint().apply {
            color = Color.BLACK
            textSize = 40f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        /// Draw heading
        val headingX = width / 2f
        val headingY = padding - headingPaint.fontMetrics.top  // Position the heading above the chart
        canvas.drawText(headingText, headingX, headingY, headingPaint)

        val path = Path()

        // Draw x-axis line
        //canvas.drawLine(padding, height - padding, width - padding, height - padding, linePaint)

        // Draw x-axis line
        /*canvas.drawLine(
            padding,
            height - padding - 1,
            width - padding,
            height - padding - 1,
            linePaint
        )*/

        // Draw y-axis line
        canvas.drawLine(padding, height - padding, padding, padding, linePaint)

        // Draw y-axis values
        for (i in 0..maxHours) {
            val x = padding
            val y = height - padding - i * scaleY

            canvas.drawText(i.toString(), x - 40, y + textPaint.textSize / 2, textPaint)
        }

        // Draw line chart
        path.reset() // Reset the path before drawing the line chart
        for ((index, dataPoint) in dataPoints.withIndex()) {
            val x = padding + (index * scaleX) // Adjusted to distribute points evenly
            val y = height - padding - dataPoint * scaleY

            // Draw mark at each data point
            canvas.drawCircle(x, y, 10f, markPaint)

            if (index < dataPoints.size - 1) {
                val nextX = padding + ((index + 1) * scaleX) // Adjusted to distribute points evenly
                val nextY = height - padding - dataPoints[index + 1] * scaleY

                // Draw line segment between consecutive points
                canvas.drawLine(x, y, nextX, nextY, linePaint)
            }
        }

        // Draw x-axis line
        canvas.drawLine(padding, height - padding - 1, width - padding, height - padding - 1, linePaint)

        // Draw x-axis values
        for (i in 1..dataPoints.size) { // Adjusted to start from 1 instead of 0
            val x = padding + ((i - 1) * scaleX) // Adjusted to distribute points evenly
            val y = height - padding

            canvas.drawText(i.toString(), x, y + 40, textPaint)
        }

        canvas.drawPath(path, linePaint)
    }
}