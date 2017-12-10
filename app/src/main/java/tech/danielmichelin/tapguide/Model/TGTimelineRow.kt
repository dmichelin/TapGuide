package tech.danielmichelin.tapguide.Model

import org.qap.ctimelineview.TimelineRow

/**
 * Created by Daniel on 12/9/2017.
 */
class TGTimelineRow(i: Int) : TimelineRow(i) {
    lateinit var businesses: Array<TGBusiness>
    var position: Int = 0

}