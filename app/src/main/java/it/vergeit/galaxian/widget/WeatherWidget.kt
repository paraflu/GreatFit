package it.vergeit.galaxian.widget

import android.app.Service
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.provider.Settings
import android.util.Log
import it.vergeit.galaxian.data.DataType
import it.vergeit.galaxian.data.WeatherData
import it.vergeit.galaxian.settings.LoadSettings
import com.ingenic.iwds.slpt.view.core.SlptPictureView
import com.ingenic.iwds.slpt.view.core.SlptViewComponent
import org.json.JSONObject

class WeatherWidget() : TextWidget() {
    private var weather: WeatherData? = null

    private var weatherImages: List<Bitmap>? = null

    constructor(_settings: LoadSettings) : this() {
        settings = _settings
    }

    // Screen-on init (runs once)
    override fun init(service: Service) {
        super.init(service)

        var weatherIcon = settings.theme.weather!!.icon
        if (weatherIcon?.images != null) {
            weatherImages = preloadCollection(weatherIcon.images)
        }
    }


    // Register listener
    override fun getDataTypes(): List<DataType> {
        return listOf(DataType.WEATHER)
    }

    // Updater
    override fun onDataUpdate(type: DataType, value: Any) { //this.weather = (WeatherData) value;
        weather = slptWeather
    }

    // Screen on
    override fun draw(canvas: Canvas?, width: Float, height: Float, centerX: Float, centerY: Float) { // Draw Temperature
        var weatherIcon = settings.theme.weather!!.icon
        if (weatherIcon?.images != null) {
            if (weather != null && weather!!.weatherType != 22) {
                Log.d(TAG, "weather ${weather!!.weatherType}")
                drawBitmap(canvas!!, weatherIcon.images.imageIndex + weather!!.weatherType, Point(weatherIcon.images.x, weatherIcon.images.y))
            } else {
                drawBitmap(canvas!!, weatherIcon.noWeatherImageIndex, weatherIcon.images.x, weatherIcon.images.y)
            }
        }
        if (settings.theme.weather?.temperature != null) {
            val temperature = settings.theme.weather!!.temperature!!
            drawText(canvas!!, weather!!.tempString, temperature)
        }
    }


// Get ALL data from system
// Extract data from JSON
//weatherType = weather_data.getInt("weatherCodeFrom");
// New custom values in weather saved by Amazmod
/*
        if (weather_data.has("tempMin"))
            tempMin = weather_data.getString("tempMin");
        if (weather_data.has("tempMax"))
            tempMax = weather_data.getString("tempMax");
        */
// Unknown weather
// Normal
// Default variables
// WeatherInfo
// {"isAlert":true, "isNotification":true, "tempFormatted":"28ºC",
// "tempUnit":"C", "v":1, "weatherCode":0, "aqi":-1, "aqiLevel":0, "city":"Somewhere",
// "forecasts":[{"tempFormatted":"31ºC/21ºC","tempMax":31,"tempMin":21,"weatherCodeFrom":0,"weatherCodeTo":0,"day":1,"weatherFrom":0,"weatherTo":0},{"tempFormatted":"33ºC/23ºC","tempMax":33,"tempMin":23,"weatherCodeFrom":0,"weatherCodeTo":0,"day":2,"weatherFrom":0,"weatherTo":0},{"tempFormatted":"34ºC/24ºC","tempMax":34,"tempMin":24,"weatherCodeFrom":0,"weatherCodeTo":0,"day":3,"weatherFrom":0,"weatherTo":0},{"tempFormatted":"34ºC/23ºC","tempMax":34,"tempMin":23,"weatherCodeFrom":0,"weatherCodeTo":0,"day":4,"weatherFrom":0,"weatherTo":0},{"tempFormatted":"32ºC/22ºC","tempMax":32,"tempMin":22,"weatherCodeFrom":0,"weatherCodeTo":0,"day":5,"weatherFrom":0,"weatherTo":0}],
// "pm25":-1, "sd":"50%", //(Humidity)
// "temp":28, "time":1531292274457, "uv":"Strong",
// "weather":0, "windDirection":"NW", "windStrength":"7.4km/h"}
// WeatherCheckedSummary
// {"tempUnit":"1","temp":"31\/21","weatherCodeFrom":0}

    /* Get Weather Data on screen off
    based on HuamiWatchFaces.jar!\com\huami\watch\watchface\widget\slpt\SlptWeatherWidget.class
    and AmazfitWeather.jar!\com\huami\watch\weather\WeatherUtil.class */
    val slptWeather: WeatherData
        get() { // Default variables
            var tempUnit = "1"
            var weatherType = 22
            var temp: String?
            var city: String?
            var humidity: String?
            var uv: String?
            var windDirection: String?
            var windStrength: String?
            var pressure: String?
            var visibility: String?
            var clouds: String
            clouds = "n/a"
            visibility = clouds
            pressure = visibility
            windStrength = pressure
            windDirection = windStrength
            uv = windDirection
            humidity = uv
            city = humidity
            temp = city
            var tempMax: String?
            var tempMin: String?
            var tempFormatted: String
            tempFormatted = "-/-"
            tempMin = tempFormatted
            tempMax = tempMin
            var sunrise: Int
            var sunset: Int
            sunset = 0
            sunrise = sunset
            // WeatherInfo
            /*
    // {"isAlert":true, "isNotification":true, "tempFormatted":"28ºC",
    // "tempUnit":"C", "v":1, "weatherCode":0, "aqi":-1, "aqiLevel":0, "city":"Somewhere",
    // "forecasts":[{"tempFormatted":"31ºC/21ºC","tempMax":31,"tempMin":21,"weatherCodeFrom":0,"weatherCodeTo":0,"day":1,"weatherFrom":0,"weatherTo":0},{"tempFormatted":"33ºC/23ºC","tempMax":33,"tempMin":23,"weatherCodeFrom":0,"weatherCodeTo":0,"day":2,"weatherFrom":0,"weatherTo":0},{"tempFormatted":"34ºC/24ºC","tempMax":34,"tempMin":24,"weatherCodeFrom":0,"weatherCodeTo":0,"day":3,"weatherFrom":0,"weatherTo":0},{"tempFormatted":"34ºC/23ºC","tempMax":34,"tempMin":23,"weatherCodeFrom":0,"weatherCodeTo":0,"day":4,"weatherFrom":0,"weatherTo":0},{"tempFormatted":"32ºC/22ºC","tempMax":32,"tempMin":22,"weatherCodeFrom":0,"weatherCodeTo":0,"day":5,"weatherFrom":0,"weatherTo":0}],
    // "pm25":-1, "sd":"50%", //(Humidity)
    // "temp":28, "time":1531292274457, "uv":"Strong",
    // "weather":0, "windDirection":"NW", "windStrength":"7.4km/h"}
    // WeatherCheckedSummary
    // {"tempUnit":"1","temp":"31\/21","weatherCodeFrom":0}
             */
            try { // Get ALL data from system
                val str = Settings.System.getString(mService.applicationContext.contentResolver, "WeatherInfo")
                Log.d(TAG, "weatherData $str")
                // Extract data from JSON
                val weatherData = JSONObject(str)
                //weatherType = weather_data.getInt("weatherCodeFrom");
                if (weatherData.has("tempUnit")) tempUnit = weatherData.getString("tempUnit")
                if (weatherData.has("temp")) temp = weatherData.getString("temp")
                if (weatherData.has("weatherCode")) weatherType = weatherData.getInt("weatherCode")
                if (weatherData.has("city")) city = weatherData.getString("city")
                if (weatherData.has("sd")) humidity = weatherData.getString("sd")
                if (weatherData.has("uv")) uv = weatherData.getString("uv")
                if (weatherData.has("windDirection")) windDirection = weatherData.getString("windDirection")
                if (weatherData.has("windStrength")) windStrength = weatherData.getString("windStrength")
                // New custom values in weather saved by Amazmod
/*
        if (weather_data.has("tempMin"))
            tempMin = weather_data.getString("tempMin");
        if (weather_data.has("tempMax"))
            tempMax = weather_data.getString("tempMax");
        */if (weatherData.has("pressure")) pressure = weatherData.getString("pressure")
                if (weatherData.has("visibility")) visibility = weatherData.getString("visibility")
                if (weatherData.has("clouds")) clouds = weatherData.getString("clouds")
                if (weatherData.has("sunrise")) sunrise = weatherData.getInt("sunrise")
                if (weatherData.has("sunset")) sunset = weatherData.getInt("sunset")
                val weather_forecast = weatherData.getJSONArray("forecasts")[0] as JSONObject
                if (weather_forecast.has("tempMax")) tempMax = weather_forecast.getString("tempMax")
                if (weather_forecast.has("tempMin")) tempMin = weather_forecast.getString("tempMin")
                if (weather_forecast.has("tempFormatted")) tempFormatted = weather_forecast.getString("tempFormatted")
            } catch (e: Exception) {
                Log.e(TAG, "Weather-widget getSlptWeather: " + e.message)
            }
            // Unknown weather
            return if (weatherType < 0 || weatherType > 22) WeatherData("1", "n/a", 22) else WeatherData(tempUnit, temp, weatherType, city, humidity, uv, windDirection, windStrength, tempMax, tempMin, tempFormatted, sunrise, sunset, pressure, visibility, clouds)
            // Normal
        }

    // Screen-off (SLPT)
    override fun buildSlptViewComponent(service: Service?): List<SlptViewComponent?>? {
        return buildSlptViewComponent(service, false)
    }

    // Screen-off (SLPT) - Better screen quality
    override fun buildSlptViewComponent(service: Service?, better_resolution: Boolean): List<SlptViewComponent?>? {

        val slpt_objects = mutableListOf<SlptViewComponent?>()
        val weatherIcon = SlptPictureView()
//            weatherIcon.setImagePicture(SimpleFile.readFileFromAssets(service, String.format((if (better_resolution) "26wc_" else "slpt_") + "weather/%s.png", settings.is_white_bg + weatherImageStrList[weather!!.weatherType])))
        weatherIcon.setStart(
                settings.weather_imgIconLeft.toInt() - 2,  // the icons are 3px larger in width than other
                settings.weather_imgIconTop.toInt() - 2 // icons, thus -2 to calibrate it a little
        )
        slpt_objects.add(weatherIcon)
//        var better_resolution = better_resolution
//        better_resolution = better_resolution && settings.better_resolution_when_raising_hand
//        val slpt_objects: MutableList<SlptViewComponent?> = ArrayList()
//        // Do not show in SLPT (but show on raise of hand)
//        val show_all = !settings.clock_only_slpt || better_resolution
//        if (!show_all) return slpt_objects
//        // Get weather data
//        mService = service
//        weather = slptWeather
//        // Draw temperature
//        if (settings.temperature > 0) { // Show or Not icon
//            if (settings.temperatureIcon) {
//                val temperatureIcon = SlptPictureView()
//                temperatureIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (better_resolution) "" else "slpt_") + "icons/" + settings.is_white_bg + "temperature.png"))
//                temperatureIcon.setStart(
//                        settings.temperatureIconLeft.toInt(),
//                        settings.temperatureIconTop.toInt()
//                )
//                slpt_objects.add(temperatureIcon)
//            }
//            val temperatureLayout = SlptLinearLayout()
//            // Show temperature with units or not
//            val temperatureNum = SlptPictureView()
//            temperatureNum.setStringPicture(weather!!.tempString + if (settings.temperatureUnits) weather!!.units else "")
//            temperatureNum.setTextAttr(
//                    settings.temperatureFontSize,
//                    settings.temperatureColor,
//                    getTypeFace(service!!.resources, settings.font)
//            )
//            temperatureLayout.add(temperatureNum)
//            // Position based on screen on
//            temperatureLayout.alignX = 2
//            temperatureLayout.alignY = 0
//            var tmp_left = settings.temperatureLeft.toInt()
//            if (!settings.temperatureAlignLeft) { // If text is centered, set rectangle
//                temperatureLayout.setRect(
//                        (2 * tmp_left + 640),
//                        (settings.font_ratio.toFloat() / 100 * settings.temperatureFontSize).toInt()
//                )
//                tmp_left = -320
//            }
//            temperatureLayout.setStart(
//                    tmp_left,
//                    (settings.temperatureTop - settings.font_ratio.toFloat() / 100 * settings.temperatureFontSize).toInt()
//            )
//            slpt_objects.add(temperatureLayout)
//        }
//        // Weather Icons
//        if (settings.weather_img > 0) {
//            val weatherIcon = SlptPictureView()
//            weatherIcon.setImagePicture(SimpleFile.readFileFromAssets(service, String.format((if (better_resolution) "26wc_" else "slpt_") + "weather/%s.png", settings.is_white_bg + weatherImageStrList[weather!!.weatherType])))
//            weatherIcon.setStart(
//                    settings.weather_imgIconLeft.toInt() - 2,  // the icons are 3px larger in width than other
//                    settings.weather_imgIconTop.toInt() - 2 // icons, thus -2 to calibrate it a little
//            )
//            slpt_objects.add(weatherIcon)
//            if (settings.weather_imgIcon) { //In the weather image widget, if icon is disabled, temperature is not shown!
//                val weatherLayout = SlptLinearLayout()
//                // Show temperature with units or not
//                val weather_imgNum = SlptPictureView()
//                weather_imgNum.setStringPicture(weather!!.tempString + if (settings.weather_imgUnits) weather!!.units else "")
//                weather_imgNum.setTextAttr(
//                        settings.weather_imgFontSize,
//                        settings.weather_imgColor,
//                        getTypeFace(service!!.resources, settings.font)
//                )
//                weatherLayout.add(weather_imgNum)
//                // Position based on screen on
//                weatherLayout.alignX = 2
//                weatherLayout.alignY = 0
//                var tmp_left = settings.weather_imgLeft.toInt()
//                if (!settings.weather_imgAlignLeft) { // If text is centered, set rectangle
//                    weatherLayout.setRect(
//                            (2 * tmp_left + 640),
//                            (settings.font_ratio.toFloat() / 100 * settings.weather_imgFontSize).toInt()
//                    )
//                    tmp_left = -320
//                }
//                weatherLayout.setStart(
//                        tmp_left,
//                        (settings.weather_imgTop - settings.font_ratio.toFloat() / 100 * settings.weather_imgFontSize).toInt()
//                )
//                slpt_objects.add(weatherLayout)
//            }
//        }
//        // Draw City
//        if (settings.city > 0) { // Show or Not icon
//            if (settings.cityIcon) {
//                val cityIcon = SlptPictureView()
//                cityIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (better_resolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "city.png"))
//                cityIcon.setStart(
//                        settings.cityIconLeft.toInt(),
//                        settings.cityIconTop.toInt()
//                )
//                slpt_objects.add(cityIcon)
//            }
//            val cityLayout = SlptLinearLayout()
//            val cityText = SlptPictureView()
//            cityText.setStringPicture(weather!!.city)
//            cityText.setTextAttr(
//                    settings.cityFontSize,
//                    settings.cityColor,
//                    getTypeFace(service!!.resources, settings.font)
//            )
//            cityLayout.add(cityText)
//            cityLayout.setTextAttrForAll(
//                    settings.cityFontSize,
//                    settings.cityColor,
//                    getTypeFace(service.resources, settings.font)
//            )
//            // Position based on screen on
//            cityLayout.alignX = 2
//            cityLayout.alignY = 0
//            var tmp_left = settings.cityLeft.toInt()
//            if (!settings.cityAlignLeft) { // If text is centered, set rectangle
//                cityLayout.setRect(
//                        (2 * tmp_left + 640),
//                        (settings.font_ratio.toFloat() / 100 * settings.cityFontSize).toInt()
//                )
//                tmp_left = -320
//            }
//            cityLayout.setStart(
//                    tmp_left,
//                    (settings.cityTop - settings.font_ratio.toFloat() / 100 * settings.cityFontSize).toInt()
//            )
//            slpt_objects.add(cityLayout)
//        }
//        // Draw Humidity
//        if (settings.humidity > 0) { // Show or Not icon
//            if (settings.humidityIcon) {
//                val humidityIcon = SlptPictureView()
//                humidityIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (better_resolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "humidity.png"))
//                humidityIcon.setStart(
//                        settings.humidityIconLeft.toInt(),
//                        settings.humidityIconTop.toInt()
//                )
//                slpt_objects.add(humidityIcon)
//            }
//            val humidityLayout = SlptLinearLayout()
//            val humidityNum = SlptPictureView()
//            humidityNum.setStringPicture(weather!!.humidity)
//            humidityNum.setTextAttr(
//                    settings.humidityFontSize,
//                    settings.humidityColor,
//                    getTypeFace(service!!.resources, settings.font)
//            )
//            humidityLayout.add(humidityNum)
//            humidityLayout.setTextAttrForAll(
//                    settings.humidityFontSize,
//                    settings.humidityColor,
//                    getTypeFace(service.resources, settings.font)
//            )
//            // Position based on screen on
//            humidityLayout.alignX = 2
//            humidityLayout.alignY = 0
//            var tmp_left = settings.humidityLeft.toInt()
//            if (!settings.humidityAlignLeft) { // If text is centered, set rectangle
//                humidityLayout.setRect(
//                        (2 * tmp_left + 640),
//                        (settings.font_ratio.toFloat() / 100 * settings.humidityFontSize).toInt()
//                )
//                tmp_left = -320
//            }
//            humidityLayout.setStart(
//                    tmp_left,
//                    (settings.humidityTop - settings.font_ratio.toFloat() / 100 * settings.humidityFontSize).toInt()
//            )
//            slpt_objects.add(humidityLayout)
//        }
//        // Draw UV rays (Strong)
//        if (settings.uv > 0) { // Show or Not icon
//            if (settings.uvIcon) {
//                val uvIcon = SlptPictureView()
//                uvIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (better_resolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "uv.png"))
//                uvIcon.setStart(
//                        settings.uvIconLeft.toInt(),
//                        settings.uvIconTop.toInt()
//                )
//                slpt_objects.add(uvIcon)
//            }
//            val uvLayout = SlptLinearLayout()
//            val uvNum = SlptPictureView()
//            uvNum.setStringPicture(weather!!.uv)
//            uvNum.setTextAttr(
//                    settings.uvFontSize,
//                    settings.uvColor,
//                    getTypeFace(service!!.resources, settings.font)
//            )
//            uvLayout.add(uvNum)
//            uvLayout.setTextAttrForAll(
//                    settings.uvFontSize,
//                    settings.uvColor,
//                    getTypeFace(service.resources, settings.font)
//            )
//            // Position based on screen on
//            uvLayout.alignX = 2
//            uvLayout.alignY = 0
//            var tmp_left = settings.uvLeft.toInt()
//            if (!settings.uvAlignLeft) { // If text is centered, set rectangle
//                uvLayout.setRect(
//                        (2 * tmp_left + 640),
//                        (settings.font_ratio.toFloat() / 100 * settings.uvFontSize).toInt()
//                )
//                tmp_left = -320
//            }
//            uvLayout.setStart(
//                    tmp_left,
//                    (settings.uvTop - settings.font_ratio.toFloat() / 100 * settings.uvFontSize).toInt()
//            )
//            slpt_objects.add(uvLayout)
//        }
//        // Draw Wind Direction
//        if (settings.wind_direction > 0) { // Show or Not icon
//            if (settings.wind_directionIcon) {
//                val wind_directionIcon = SlptPictureView()
//                wind_directionIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (better_resolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "wind_direction.png"))
//                wind_directionIcon.setStart(
//                        settings.wind_directionIconLeft.toInt(),
//                        settings.wind_directionIconTop.toInt()
//                )
//                slpt_objects.add(wind_directionIcon)
//            }
//            val wind_directionLayout = SlptLinearLayout()
//            val wind_directionText = SlptPictureView()
//            // todo
//            wind_directionText.setStringPicture(if (settings.wind_direction_as_arrows) weather!!.windArrow else weather!!.windDirection)
//            wind_directionText.setTextAttr(
//                    settings.wind_directionFontSize,
//                    settings.wind_directionColor,
//                    getTypeFace(service!!.resources, settings.font)
//            )
//            wind_directionLayout.add(wind_directionText)
//            wind_directionLayout.setTextAttrForAll(
//                    settings.wind_directionFontSize,
//                    settings.wind_directionColor,
//                    getTypeFace(service.resources, settings.font)
//            )
//            // Position based on screen on
//            wind_directionLayout.alignX = 2
//            wind_directionLayout.alignY = 0
//            var tmp_left = settings.wind_directionLeft.toInt()
//            if (!settings.wind_directionAlignLeft) { // If text is centered, set rectangle
//                wind_directionLayout.setRect(
//                        (2 * tmp_left + 640),
//                        (settings.font_ratio.toFloat() / 100 * settings.wind_directionFontSize).toInt()
//                )
//                tmp_left = -320
//            }
//            wind_directionLayout.setStart(
//                    tmp_left,
//                    (settings.wind_directionTop - settings.font_ratio.toFloat() / 100 * settings.wind_directionFontSize).toInt()
//            )
//            slpt_objects.add(wind_directionLayout)
//        }
//        // Draw Wind Strength (ex. 7.4km/h)
//        if (settings.wind_strength > 0) { // Show or Not icon
//            if (settings.wind_strengthIcon) {
//                val wind_strengthIcon = SlptPictureView()
//                wind_strengthIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (better_resolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "wind_strength.png"))
//                wind_strengthIcon.setStart(
//                        settings.wind_strengthIconLeft.toInt(),
//                        settings.wind_strengthIconTop.toInt()
//                )
//                slpt_objects.add(wind_strengthIcon)
//            }
//            val wind_strengthLayout = SlptLinearLayout()
//            val wind_strengthText = SlptPictureView()
//            wind_strengthText.setStringPicture(weather!!.windStrength)
//            wind_strengthText.setTextAttr(
//                    settings.wind_strengthFontSize,
//                    settings.wind_strengthColor,
//                    getTypeFace(service!!.resources, settings.font)
//            )
//            wind_strengthLayout.add(wind_strengthText)
//            wind_strengthLayout.setTextAttrForAll(
//                    settings.wind_strengthFontSize,
//                    settings.wind_strengthColor,
//                    getTypeFace(service.resources, settings.font)
//            )
//            // Position based on screen on
//            wind_strengthLayout.alignX = 2
//            wind_strengthLayout.alignY = 0
//            var tmp_left = settings.wind_strengthLeft.toInt()
//            if (!settings.wind_strengthAlignLeft) { // If text is centered, set rectangle
//                wind_strengthLayout.setRect(
//                        (2 * tmp_left + 640),
//                        (settings.font_ratio.toFloat() / 100 * settings.wind_strengthFontSize).toInt()
//                )
//                tmp_left = -320
//            }
//            wind_strengthLayout.setStart(
//                    tmp_left,
//                    (settings.wind_strengthTop - settings.font_ratio.toFloat() / 100 * settings.wind_strengthFontSize).toInt()
//            )
//            slpt_objects.add(wind_strengthLayout)
//        }
//        // Draw min max temperatures (ex. 17/20 C)
//        if (settings.min_max_temperatures > 0) { // Show or Not icon
//            if (settings.min_max_temperaturesIcon) {
//                val min_max_temperaturesIcon = SlptPictureView()
//                min_max_temperaturesIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (better_resolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "min_max_temperatures.png"))
//                min_max_temperaturesIcon.setStart(
//                        settings.min_max_temperaturesIconLeft.toInt(),
//                        settings.min_max_temperaturesIconTop.toInt()
//                )
//                slpt_objects.add(min_max_temperaturesIcon)
//            }
//            val min_max_temperaturesLayout = SlptLinearLayout()
//            val min_max_temperaturesText = SlptPictureView()
//            min_max_temperaturesText.setStringPicture(weather!!.tempFormatted)
//            min_max_temperaturesText.setTextAttr(
//                    settings.min_max_temperaturesFontSize,
//                    settings.min_max_temperaturesColor,
//                    getTypeFace(service!!.resources, settings.font)
//            )
//            min_max_temperaturesLayout.add(min_max_temperaturesText)
//            min_max_temperaturesLayout.setTextAttrForAll(
//                    settings.min_max_temperaturesFontSize,
//                    settings.min_max_temperaturesColor,
//                    getTypeFace(service.resources, settings.font)
//            )
//            // Position based on screen on
//            min_max_temperaturesLayout.alignX = 2
//            min_max_temperaturesLayout.alignY = 0
//            var tmp_left = settings.min_max_temperaturesLeft.toInt()
//            if (!settings.min_max_temperaturesAlignLeft) { // If text is centered, set rectangle
//                min_max_temperaturesLayout.setRect(
//                        (2 * tmp_left + 640),
//                        (settings.font_ratio.toFloat() / 100 * settings.min_max_temperaturesFontSize).toInt()
//                )
//                tmp_left = -320
//            }
//            min_max_temperaturesLayout.setStart(
//                    tmp_left,
//                    (settings.min_max_temperaturesTop - settings.font_ratio.toFloat() / 100 * settings.min_max_temperaturesFontSize).toInt()
//            )
//            slpt_objects.add(min_max_temperaturesLayout)
//        }
//        // Draw sunset
//        if (settings.sunset > 0) { // Show or Not icon
//            if (settings.sunsetIcon) {
//                val sunsetIcon = SlptPictureView()
//                sunsetIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (better_resolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "sunset.png"))
//                sunsetIcon.setStart(
//                        settings.sunsetIconLeft.toInt(),
//                        settings.sunsetIconTop.toInt()
//                )
//                slpt_objects.add(sunsetIcon)
//            }
//            // Get sunset to proper format
//            val current_date = Calendar.getInstance()
//            val data_date = Calendar.getInstance()
//            data_date.timeInMillis = weather!!.sunset * 1000L
//            var sunset_time = "n/a"
//            //if (current_date.get(Calendar.DATE) == data_date.get(Calendar.DATE)) {
//            val hours = data_date[Calendar.HOUR_OF_DAY]
//            val minutes = data_date[Calendar.MINUTE]
//            sunset_time = (if (hours < 10) "0" else "") + hours + ":" + (if (minutes < 10) "0" else "") + minutes
//            //}
//// Display it
//            val sunsetLayout = SlptLinearLayout()
//            val sunsetNum = SlptPictureView()
//            sunsetNum.setStringPicture(sunset_time)
//            sunsetNum.setTextAttr(
//                    settings.sunsetFontSize,
//                    settings.sunsetColor,
//                    getTypeFace(service!!.resources, settings.font)
//            )
//            sunsetLayout.add(sunsetNum)
//            sunsetLayout.setTextAttrForAll(
//                    settings.sunsetFontSize,
//                    settings.sunsetColor,
//                    getTypeFace(service.resources, settings.font)
//            )
//            // Position based on screen on
//            sunsetLayout.alignX = 2
//            sunsetLayout.alignY = 0
//            var tmp_left = settings.sunsetLeft.toInt()
//            if (!settings.sunsetAlignLeft) { // If text is centered, set rectangle
//                sunsetLayout.setRect(
//                        (2 * tmp_left + 640),
//                        (settings.font_ratio.toFloat() / 100 * settings.sunsetFontSize).toInt()
//                )
//                tmp_left = -320
//            }
//            sunsetLayout.setStart(
//                    tmp_left,
//                    (settings.sunsetTop - settings.font_ratio.toFloat() / 100 * settings.sunsetFontSize).toInt()
//            )
//            slpt_objects.add(sunsetLayout)
//        }
//        // Draw sunrise
//        if (settings.sunrise > 0) { // Show or Not icon
//            if (settings.sunriseIcon) {
//                val sunriseIcon = SlptPictureView()
//                sunriseIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (better_resolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "sunrise.png"))
//                sunriseIcon.setStart(
//                        settings.sunriseIconLeft.toInt(),
//                        settings.sunriseIconTop.toInt()
//                )
//                slpt_objects.add(sunriseIcon)
//            }
//            // Get sunrise to proper format
//            val current_date = Calendar.getInstance()
//            val data_date = Calendar.getInstance()
//            data_date.timeInMillis = weather!!.sunrise * 1000L
//            var sunrise_time = "n/a"
//            //if (current_date.get(Calendar.DATE) == data_date.get(Calendar.DATE)) {
//            val hours = data_date[Calendar.HOUR_OF_DAY]
//            val minutes = data_date[Calendar.MINUTE]
//            sunrise_time = (if (hours < 10) "0" else "") + hours + ":" + (if (minutes < 10) "0" else "") + minutes
//            //}
//// Display it
//            val sunriseLayout = SlptLinearLayout()
//            val sunriseNum = SlptPictureView()
//            sunriseNum.setStringPicture(sunrise_time)
//            sunriseNum.setTextAttr(
//                    settings.sunriseFontSize,
//                    settings.sunriseColor,
//                    getTypeFace(service!!.resources, settings.font)
//            )
//            sunriseLayout.add(sunriseNum)
//            sunriseLayout.setTextAttrForAll(
//                    settings.sunriseFontSize,
//                    settings.sunriseColor,
//                    getTypeFace(service.resources, settings.font)
//            )
//            // Position based on screen on
//            sunriseLayout.alignX = 2
//            sunriseLayout.alignY = 0
//            var tmp_left = settings.sunriseLeft.toInt()
//            if (!settings.sunriseAlignLeft) { // If text is centered, set rectangle
//                sunriseLayout.setRect(
//                        (2 * tmp_left + 640),
//                        (settings.font_ratio.toFloat() / 100 * settings.sunriseFontSize).toInt()
//                )
//                tmp_left = -320
//            }
//            sunriseLayout.setStart(
//                    tmp_left,
//                    (settings.sunriseTop - settings.font_ratio.toFloat() / 100 * settings.sunriseFontSize).toInt()
//            )
//            slpt_objects.add(sunriseLayout)
//        }
//        // Draw visibility
//        if (settings.visibility > 0) { // Show or Not icon
//            if (settings.visibilityIcon) {
//                val visibilityIcon = SlptPictureView()
//                visibilityIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (better_resolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "visibility.png"))
//                visibilityIcon.setStart(
//                        settings.visibilityIconLeft.toInt(),
//                        settings.visibilityIconTop.toInt()
//                )
//                slpt_objects.add(visibilityIcon)
//            }
//            val visibilityLayout = SlptLinearLayout()
//            val visibilityNum = SlptPictureView()
//            visibilityNum.setStringPicture(weather!!.visibility)
//            visibilityNum.setTextAttr(
//                    settings.visibilityFontSize,
//                    settings.visibilityColor,
//                    getTypeFace(service!!.resources, settings.font)
//            )
//            visibilityLayout.add(visibilityNum)
//            visibilityLayout.setTextAttrForAll(
//                    settings.visibilityFontSize,
//                    settings.visibilityColor,
//                    getTypeFace(service.resources, settings.font)
//            )
//            // Position based on screen on
//            visibilityLayout.alignX = 2
//            visibilityLayout.alignY = 0
//            var tmp_left = settings.visibilityLeft.toInt()
//            if (!settings.visibilityAlignLeft) { // If text is centered, set rectangle
//                visibilityLayout.setRect(
//                        (2 * tmp_left + 640),
//                        (settings.font_ratio.toFloat() / 100 * settings.visibilityFontSize).toInt()
//                )
//                tmp_left = -320
//            }
//            visibilityLayout.setStart(
//                    tmp_left,
//                    (settings.visibilityTop - settings.font_ratio.toFloat() / 100 * settings.visibilityFontSize).toInt()
//            )
//            slpt_objects.add(visibilityLayout)
//        }
//        // Draw clouds
//        if (settings.clouds > 0) { // Show or Not icon
//            if (settings.cloudsIcon) {
//                val cloudsIcon = SlptPictureView()
//                cloudsIcon.setImagePicture(SimpleFile.readFileFromAssets(service, (if (better_resolution) "26wc_" else "slpt_") + "icons/" + settings.is_white_bg + "clouds.png"))
//                cloudsIcon.setStart(
//                        settings.cloudsIconLeft.toInt(),
//                        settings.cloudsIconTop.toInt()
//                )
//                slpt_objects.add(cloudsIcon)
//            }
//            val cloudsLayout = SlptLinearLayout()
//            val cloudsNum = SlptPictureView()
//            cloudsNum.setStringPicture(weather!!.clouds)
//            cloudsNum.setTextAttr(
//                    settings.cloudsFontSize,
//                    settings.cloudsColor,
//                    getTypeFace(service!!.resources, settings.font)
//            )
//            cloudsLayout.add(cloudsNum)
//            cloudsLayout.setTextAttrForAll(
//                    settings.cloudsFontSize,
//                    settings.cloudsColor,
//                    getTypeFace(service.resources, settings.font)
//            )
//            // Position based on screen on
//            cloudsLayout.alignX = 2
//            cloudsLayout.alignY = 0
//            var tmp_left = settings.cloudsLeft.toInt()
//            if (!settings.cloudsAlignLeft) { // If text is centered, set rectangle
//                cloudsLayout.setRect(
//                        (2 * tmp_left + 640),
//                        (settings.font_ratio.toFloat() / 100 * settings.cloudsFontSize).toInt()
//                )
//                tmp_left = -320
//            }
//            cloudsLayout.setStart(
//                    tmp_left,
//                    (settings.cloudsTop - settings.font_ratio.toFloat() / 100 * settings.cloudsFontSize).toInt()
//            )
//            slpt_objects.add(cloudsLayout)
//        }
//        return slpt_objects
        return null
    }

    companion object {
        private const val TAG = "VergeIT-LOG"
    }

// Constructor
//    init {
//        // Load weather icons
//        val weatherIconNames = arrayOf(
//                "sunny",  //0
//                "cloudy",  //1
//                "overcast",  //2
//                "fog",  //..
//                "smog",
//                "shower",
//                "thunder_shower",
//                "light_rain",
//                "moderate_rain",
//                "heavy_rain",
//                "rainstorm",
//                "torrential_rain",
//                "sleet",
//                "freezing_rain",
//                "hail",
//                "light_snow",
//                "moderate_snow",
//                "heavy_snow",
//                "snowstorm",
//                "dust",
//                "blowing_sand",  //..
//                "sand_storm",  //21
//                "unknown" //22
//        )
//        weatherImageStrList = Arrays.asList(*weatherIconNames)
//    }
}