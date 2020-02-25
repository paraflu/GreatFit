package com.dinodevs.greatfitwatchface.resource

import java.util.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 *Moon phase calculation routines for computing:
 *
 *  1.  current moon phase as an index from 0 to 7,
 *  1.  current moon age as days/hours/minutes, and
 *  1.  percentage of the moon's illuminated portion
 *
 *
 * Converted from rc-utils.c in the GCal package
 * (http://ftp.gnu.org/gnu/gcal/gcal-2.40.tar.gz)
 */
class MoonPhase {
    //private instance fields
    private var _curCal: Calendar
    private var _JD = 0.0
    private var _phase = 0.0
    private var _moonAgeAsDays = 0.0
    private val moon_phase_name = arrayOf(
            "New moon",  // 0
            "Waxing crescent",  // 1
            "First quarter",  // 2
            "Waxing gibbous",  // 3
            "Full moon",  // 4
            "Waning gibbous",  // 5
            "Last quarter",  // 6
            "Waning crescent" // 7
    )

    constructor(c: Calendar) {
        _curCal = c
    }

    constructor() {
        _curCal = Calendar.getInstance()
    }

    /*
     * Some useful mathematical functions used by John Walkers `phase()' function.
     */
    private fun FIXANGLE(a: Double): Double {
        return a - 360.0 * Math.floor(a / 360.0)
    }

    private fun TORAD(d: Double): Double {
        return d * (MY_PI / 180.0)
    }

    private fun TODEG(r: Double): Double {
        return r * (180.0 / MY_PI)
    }

    /*
       Solves the equation of Kepler.
    */
    private fun kepler(m: Double): Double {
        var m1 = m
        var e: Double
        var delta: Double
        m1 = TORAD(m1)
        e = m1
        do {
            delta = e - ECCENT_EARTH_ORBIT * sin(e) - m1
            e -= delta / (1.0 - ECCENT_EARTH_ORBIT * cos(e))
        } while (abs(delta) - KEPLER_EPSILON > 0.0)
        return e
    }

    //    /**
//     * Computes the absolute number of days of the given date since
//     * 00010101(==YYYYMMDD) respecting the missing period of the
//     * Gregorian Reformation.
//     * @param day int day as integer value
//     * @param month int month as integer value
//     * @param year int year as integer value
//     */
//    public static int date2num(int day,
//                        int month,
//                        int year) {
//        int julian_days = (year - 1) * (DAY_LAST) + ((year - 1) >> 2);
//
//
//        if (year > greg[YEAR]
//                || ((year == greg[YEAR])
//                && (month > greg[MONTH]
//                || ((month == greg[MONTH])
//                && (day > greg[LAST_DAY])))))
//            julian_days -= greg[LAST_DAY] - greg[FIRST_DAY] + 1;
//        if (year > greg[YEAR]) {
//            julian_days += (((year - 1) / 400) - (greg[YEAR] / 400));
//            julian_days -= (((year - 1) / 100) - (greg[YEAR] / 100));
//            if (!((greg[YEAR] % 100) == 0)
//                    && ((greg[YEAR] % 400) == 0)) {
//                julian_days--;
//            }
//        }
//        julian_days += mvec[month - 1];
//        julian_days += day;
//        if ((days_of_february(year) == 29)
//                && (month > 2))
//            julian_days++;
//
//        return (julian_days);
//    }
//
/*
       Computes the number of days in February --- respecting the Gregorian
         Reformation period likewise the leap year rule as used by the
         Eastern orthodox churches --- and returns them.
    */
    private fun days_of_february(year: Int): Int {
        var day: Int
        day = if (year > greg[YEAR]
                || (year == greg[YEAR]
                        && (greg[MONTH] == 1
                        || (greg[MONTH] == 2
                        && greg[LAST_DAY] >= 28)))) {
            if (orthodox_calendar) if (year and 3 != 0) 28 else if (year % 100 == 0) if (year % 9 == 2 || year % 9 == 6) 29 else 28 else 29 else if (year and 3 != 0) 28 else if (year % 100 == 0 && year % 400 != 0) 28 else 29
        } else if (year and 3 != 0) 28 else 29
        /*
           Exception, the year 4 AD was historically NO leap year!
        */if (year == 4) day--
        return day
    }

    private fun SGN(gc_x: Double): Int {
        return if (gc_x < 0) -1 else if (gc_x > 0) 1 else 0
    }

    /**
     *
     * Calculates the phase of the Moon and returns the illuminated fraction of
     * the Moon's disc as a value within the range of -99.9~...0.0...+99.9~,
     * which has a negative sign in case the Moon wanes, otherwise the sign
     * is positive.  The New Moon phase is around the 0.0 value and the Full
     * Moon phase is around the +/-99.9~ value.  The argument is the time for
     * which the phase is requested, expressed as a Julian date and fraction.
     *
     * This function is taken from the program "moontool" by John Walker,
     * February 1988, which is in the public domain.  So see it for more
     * information!  It is adapted (crippled) and `pretty-printed' to the
     * requirements of Gcal, which means it is lacking all the other useful
     * computations of astronomical values of the original code.
     *
     *
     * Here is the blurb from "moontool":
     *
     * ...The algorithms used in this program to calculate the positions Sun
     * and Moon as seen from the Earth are given in the book "Practical Astronomy
     * With Your Calculator" by Peter Duffett-Smith, Second Edition,
     * Cambridge University Press, 1981. Ignore the word "Calculator" in the
     * title; this is an essential reference if you're interested in
     * developing software which calculates planetary positions, orbits,
     * eclipses, and the like. If you're interested in pursuing such
     * programming, you should also obtain:
     *
     *
     * "Astronomical Formulae for Calculators" by Jean Meeus, Third Edition,
     * Willmann-Bell, 1985. A must-have.
     *
     * "Planetary Programs and Tables from -4000 to +2800" by Pierre
     * Bretagnon and Jean-Louis Simon, Willmann-Bell, 1986. If you want the
     * utmost (outside of JPL) accuracy for the planets, it's here.
     *
     *
     * "Celestial BASIC" by Eric Burgess, Revised Edition, Sybex, 1985. Very
     * cookbook oriented, and many of the algorithms are hard to dig out of
     * the turgid BASIC code, but you'll probably want it anyway.
     *
     *
     * Many of these references can be obtained from Willmann-Bell, P.O. Box
     * 35025, Richmond, VA 23235, USA. Phone: (804) 320-7016. In addition
     * to their own publications, they stock most of the standard references
     * for mathematical and positional astronomy.
     *
     *
     * This program was written by:
     *
     *
     * John Walker<br></br>
     * Autodesk, Inc.<br></br>
     * 2320 Marinship Way<br></br>
     * Sausalito, CA 94965<br></br>
     * (415) 332-2344 Ext. 829
     *
     *
     * Usenet: {sun!well}!acad!kelvin
     *
     *
     * This program is in the public domain: "Do what thou wilt shall be the
     * whole of the law". I'd appreciate receiving any bug fixes and/or
     * enhancements, which I'll incorporate in future versions of the
     * program. Please leave the original attribution information intact so
     * that credit and blame may be properly apportioned.
     */
    private fun phase(julian_date: Double): Double {
        val date_within_epoch: Double
        var sun_eccent: Double
        val sun_mean_anomaly: Double
        val sun_perigree_co_ordinates_to_epoch: Double
        val sun_geocentric_elong: Double
        val moon_evection: Double
        val moon_variation: Double
        val moon_mean_anomaly: Double
        val moon_mean_longitude: Double
        val moon_annual_equation: Double
        val moon_correction_term1: Double
        val moon_correction_term2: Double
        val moon_correction_equation_of_center: Double
        val moon_corrected_anomaly: Double
        val moon_corrected_longitude: Double
        val moon_present_age: Double
        var moon_present_phase: Double
        val moon_present_longitude: Double
        /*
           Calculation of the Sun's position.
        */date_within_epoch = julian_date - EPOCH
        sun_mean_anomaly = FIXANGLE(360.0 / 365.2422 * date_within_epoch)
        sun_perigree_co_ordinates_to_epoch = FIXANGLE(sun_mean_anomaly + SUN_ELONG_EPOCH - SUN_ELONG_PERIGEE)
        sun_eccent = kepler(sun_perigree_co_ordinates_to_epoch)
        sun_eccent = Math.sqrt((1.0 + ECCENT_EARTH_ORBIT) / (1.0 - ECCENT_EARTH_ORBIT)) * Math.tan(sun_eccent / 2.0)
        sun_eccent = 2.0 * TODEG(Math.atan(sun_eccent))
        sun_geocentric_elong = FIXANGLE(sun_eccent + SUN_ELONG_PERIGEE)
        /*
           Calculation of the Moon's position.
        */moon_mean_longitude = FIXANGLE(13.1763966 * date_within_epoch + MOON_MEAN_LONGITUDE_EPOCH)
        moon_mean_anomaly = FIXANGLE(moon_mean_longitude - 0.1114041 * date_within_epoch - MOON_MEAN_LONGITUDE_PERIGREE)
        moon_evection = 1.2739 * Math.sin(TORAD(2.0 * (moon_mean_longitude - sun_geocentric_elong) - moon_mean_anomaly))
        moon_annual_equation = 0.1858 * Math.sin(TORAD(sun_perigree_co_ordinates_to_epoch))
        moon_correction_term1 = 0.37 * Math.sin(TORAD(sun_perigree_co_ordinates_to_epoch))
        moon_corrected_anomaly = moon_mean_anomaly + moon_evection - moon_annual_equation - moon_correction_term1
        moon_correction_equation_of_center = 6.2886 * Math.sin(TORAD(moon_corrected_anomaly))
        moon_correction_term2 = 0.214 * Math.sin(TORAD(2.0 * moon_corrected_anomaly))
        moon_corrected_longitude = moon_mean_longitude + moon_evection + moon_correction_equation_of_center
        -moon_annual_equation + moon_correction_term2
        moon_variation = 0.6583 * Math.sin(TORAD(2.0 * (moon_corrected_longitude - sun_geocentric_elong)))
        // true longitude
        moon_present_longitude = moon_corrected_longitude + moon_variation
        moon_present_age = moon_present_longitude - sun_geocentric_elong
        moon_present_phase = 100.0 * ((1.0 - Math.cos(TORAD(moon_present_age))) / 2.0)
        if (0.0 < FIXANGLE(moon_present_age) - 180.0) {
            moon_present_phase = -moon_present_phase
        }
        _moonAgeAsDays = SYNMONTH * (FIXANGLE(moon_present_age) / 360.0)
        return moon_present_phase
    }

    /**  UCTTOJ  --  Convert GMT date and time to astronomical
     * Julian time (i.e. Julian date plus day fraction,
     * expressed as a double).
     * @param cal Calendar object
     * @return JD float Julian date
     *
     * Converted to Java by vriolk@gmail.com from original file mooncalc.c,
     * part of moontool http://www.fourmilab.ch/moontoolw/moont16s.zip
     */
    private fun calendarToJD(cal: Calendar): Double { /* Algorithm as given in Meeus, Astronomical Algorithms, Chapter 7, page 61*/
        val year = cal[Calendar.YEAR].toLong()
        val mon = cal[Calendar.MONTH]
        val mday = cal[Calendar.DATE]
        val hour = cal[Calendar.HOUR_OF_DAY]
        val min = cal[Calendar.MINUTE]
        val sec = cal[Calendar.SECOND]
        val a: Int
        val b: Int
        var m: Int
        var y: Long
        m = mon + 1
        y = year
        if (m <= 2) {
            y--
            m += 12
        }
        /* Determine whether date is in Julian or Gregorian
         * calendar based on canonical date of calendar reform.
         */if (year < 1582 || year == 1582L && (mon < 9 || mon == 9 && mday < 5)) {
            b = 0
        } else {
            a = (y / 100).toInt()
            b = 2 - a + a / 4
        }
        return (365.25 * (y + 4716)).toLong() + (30.6001 * (m + 1)).toInt() +
                mday + b - 1524.5 +
                (sec + 60L * (min + 60L * hour)) / 86400.0
    }

    /**
     * Returns current phase as double value
     * Uses class Calendar field _curCal
     */
    val phase: Double
        get() {
            _JD = calendarToJD(_curCal)
            _phase = phase(_JD)
            return _phase
        }

    val phaseIndex: Int
        get() = computePhaseIndex(_curCal)

    val phaseName: String
        get() = moon_phase_name[phaseIndex]

    /**
     * Computes the moon phase index as a value from 0 to 7
     * Used to display the phase name and the moon image
     * for the current phase
     * @param cal Calendar calendar object for today's date
     * @return moon index 0..7
     */
    private fun computePhaseIndex(cal: Calendar): Int {
        val day_year = intArrayOf(-1, -1, 30, 58, 89, 119,
                150, 180, 211, 241, 272,
                303, 333)
        val phase: Int // Moon phase
        val year: Int
        var month: Int
        val day: Int
        val hour: Int
        val min: Int
        val sec: Int
        year = cal[Calendar.YEAR]
        month = cal[Calendar.MONTH] + 1 // 0 = Jan, 1 = Feb, etc.
        day = cal[Calendar.DATE]
        hour = cal[Calendar.HOUR]
        min = cal[Calendar.MINUTE]
        sec = cal[Calendar.SECOND]
        val day_exact = day + hour / 24 + min / 1440 + (sec / 86400).toDouble()
        //int      phase;    // Moon phase
        val cent: Int // Century number (1979 = 20)
        var epact: Int // Age of the moon on Jan. 1
        var diy: Double // Day in the year
        val golden: Int // Moon's golden number
        if (month < 0 || month > 12) {
            month = 0 // Just in case
        } // Just in case
        diy = day_exact + day_year[month] // Day in the year
        if (month > 2 && isLeapYearP(year)) {
            diy++
        } // Leapyear fixup
        cent = year / 100 + 1 // Century number
        golden = year % 19 + 1 // Golden number
        epact = ((11 * golden + 20 // Golden number
                + (8 * cent + 5) / 25) - 5 // 400 year cycle
                - (3 * cent / 4 - 12)) % 30 //Leap year correction
        if (epact <= 0) {
            epact += 30
        } // Age range is 1 .. 30
        if (epact == 25 && golden > 11 ||
                epact == 24) {
            epact++
            // Calculate the phase, using the magic numbers defined above.
// Note that (phase and 7) is equivalent to (phase mod 8) and
// is needed on two days per year (when the algorithm yields 8).
        }
        // Calculate the phase, using the magic numbers defined above.
// Note that (phase and 7) is equivalent to (phase mod 8) and
// is needed on two days per year (when the algorithm yields 8).
// this.factor = ((((diy + (double)epact) * 6) + 11) % 100 );
        phase = ((diy.toInt() + epact) * 6 + 11) % 177 / 22 and 7
        return phase
    }

    /** isLeapYearP
     * Return true if the year is a leapyear
     */
    private fun isLeapYearP(year: Int): Boolean {
        return year % 4 == 0 &&
                (year % 400 == 0 || year % 100 != 0)
    }

    val moonAgeAsDays: String
        get() {
            val aom_d = _moonAgeAsDays.toInt()
            val aom_h = (24 * (_moonAgeAsDays - Math.floor(_moonAgeAsDays))).toInt()
            val aom_m = (1440 * (_moonAgeAsDays - Math.floor(_moonAgeAsDays))).toInt() % 60
            return "" + aom_d + (if (aom_d == 1) " day, " else " days, ") +
                    aom_h + (if (aom_h == 1) " hour, " else " hours, ") +
                    aom_m + if (aom_m == 1) " minute" else " minutes"
        }

    /*
public static void main(String args[]) {
        System.out.println(new Date());

        MoonPhase mp = new MoonPhase(Calendar.getInstance());
        System.out.printf("Current phase: %f%n", mp.getPhase());
        System.out.println("Moon Age: " + mp.getMoonAgeAsDays());

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_WEEK, -22);

        for (int i=0; i< 33; i++){
            c.add(Calendar.DAY_OF_WEEK, 1);
            mp = new MoonPhase(c);
            System.out.format("%1$td %1$tB,%1$tY  %1$tH:%1$tM:%1$tS  ",c);
			System.out.printf("Phase index: %d ", mp.getPhaseIndex());
			System.out.printf("Phase string: %s ", mp.getPhaseName());
            System.out.printf("%f%n", mp.getPhase());
        }
}
*/
    private fun adjustTimeZone(c: Calendar, offsetInHours: Int): Calendar {
        val currTime = c.time.time
        c.time = Date(currTime + offsetInHours * 1000 * 60 * 60)
        return c
    }

    val currentTimeZone: Int
        get() = TimeZone.getDefault().rawOffset / 1000 * 60 * 60

    companion object {
        //Prefs prefs;
        private const val MY_PI = 3.14159265358979323846
        private const val EPOCH = 2444238.5 /* 1980 January 0.0. */
        private const val SUN_ELONG_EPOCH = 278.833540 /* Ecliptic longitude of the Sun at epoch 1980.0. */
        private const val SUN_ELONG_PERIGEE = 282.596403 /* Ecliptic longitude of the Sun at perigee. */
        private const val ECCENT_EARTH_ORBIT = 0.016718 /* Eccentricity of Earth's orbit. */
        private const val MOON_MEAN_LONGITUDE_EPOCH = 64.975464 /* Moon's mean lonigitude at the epoch. */
        private const val MOON_MEAN_LONGITUDE_PERIGREE = 349.383063 /* Mean longitude of the perigee at the epoch. */
        private const val KEPLER_EPSILON = 1E-6 /* Accurancy of the Kepler equation. */
        private const val SYNMONTH = 29.53058868 /* Synodic month (new Moon to new Moon) */
        private const val orthodox_calendar = false
        val mvec = intArrayOf(
                0, 31, 59, 90, 120, 151,
                181, 212, 243, 273, 304, 334
        )
        private val greg = intArrayOf(1582, 10, 5, 14)
        private const val YEAR = 0
        private const val MONTH = 1
        const val FIRST_DAY = 2
        private const val LAST_DAY = 3
    }
}